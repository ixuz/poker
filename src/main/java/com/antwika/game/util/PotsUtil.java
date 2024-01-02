package com.antwika.game.util;

import com.antwika.eval.core.IEvaluation;
import com.antwika.eval.core.IHandProcessor;
import com.antwika.eval.exception.HandEvaluatorException;
import com.antwika.eval.processor.TexasHoldemProcessor;
import com.antwika.eval.util.HandEvaluatorUtil;
import com.antwika.game.data.CandidateData;
import com.antwika.game.player.Player;
import com.antwika.game.data.PotData;
import com.antwika.game.data.SeatData;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PotsUtil {
    private static final Logger logger = LoggerFactory.getLogger(PotsUtil.class);

    public static List<PotData> collectBets(List<SeatData> seats) {
        final List<PotData> pots = new ArrayList<>();

        final List<SeatData> sorted = new ArrayList<>(seats.stream()
                .filter(i -> i.getCommitted() > 0)
                .sorted(Comparator.comparingInt(SeatData::getCommitted))
                .toList());

        while (!sorted.isEmpty()) {
            int potAmount = 0;
            final List<CandidateData> candidates = new ArrayList<>();
            for (int i = 0; i < sorted.size(); i += 1) {
                final SeatData seat = sorted.get(i);
                if (i == 0) {
                    potAmount = seat.getCommitted();
                }

                candidates.add(new CandidateData(seat, potAmount));
                seat.setCommitted(seat.getCommitted() - potAmount);
            }

            pots.add(new PotData(potAmount, candidates));

            sorted.removeAll(sorted.stream().filter(i -> i.getCommitted() == 0).toList());
        }

        if (!pots.isEmpty()) {
            final int lastSidePotIndex = pots.size() - 1;
            final PotData lastSidePot = pots.get(lastSidePotIndex);

            if (lastSidePot.getCandidates().size() == 1) {
                final CandidateData candidate = lastSidePot.getCandidates().get(0);
                final SeatData seat = candidate.getSeat();
                final Player player = seat.getPlayer();
                final int amount = lastSidePot.getTotalAmount();
                seat.setStack(seat.getStack() + amount);
                pots.remove(lastSidePot);
                logger.info("Uncalled bet ({}) returned to {}", lastSidePot.getTotalAmount(), player.getPlayerData().getPlayerName());
            }
        }

        return pots;
    }

    public static boolean hasSameCandidates(PotData pot1, PotData pot2) {
        final List<SeatData> potCandidates1 = pot1.getCandidates().stream().map(CandidateData::getSeat).filter(i -> !i.isHasFolded()).toList();
        final List<SeatData> potCandidates2 = pot2.getCandidates().stream().map(CandidateData::getSeat).filter(i -> !i.isHasFolded()).toList();

        if (potCandidates1.size() != potCandidates2.size()) return false;

        final List<SeatData> scratch = new ArrayList<>(potCandidates1);
        scratch.removeAll(potCandidates2);

        return scratch.isEmpty();
    }

    public static List<PotData> collapsePots(List<PotData> pots) {
        final List<PotData> collapsed = new ArrayList<>();

        for (PotData pot : pots) {
            final List<CandidateData> ineligible = new ArrayList<>();
            final List<CandidateData> eligible = new ArrayList<>(pot.getCandidates());
            for (CandidateData candidate : pot.getCandidates()) {
                if (candidate.getSeat().isHasFolded()) {
                    ineligible.add(candidate);
                }
            }
            eligible.removeAll(ineligible);
            final PotData p = new PotData(pot.getAmountPerCandidate(), eligible);
            p.setTotalAmount(pot.getTotalAmount());
            collapsed.add(p);
        }

        for (int i = 0; i < collapsed.size() - 1; i++) {
            final PotData currentPot = collapsed.get(i);
            final PotData nextPot = collapsed.get(i + 1);

            if (!hasSameCandidates(currentPot, nextPot)) {
                continue;
            }

            nextPot.setTotalAmount(nextPot.getTotalAmount() + currentPot.getTotalAmount());
            nextPot.setAmountPerCandidate(nextPot.getAmountPerCandidate() + currentPot.getAmountPerCandidate());

            for (CandidateData nextPotCandidate : nextPot.getCandidates()) {
                for (CandidateData currentPotCandidate : currentPot.getCandidates()) {
                    if (currentPotCandidate.getSeat() == nextPotCandidate.getSeat()) {
                        nextPotCandidate.setAmount(nextPotCandidate.getAmount() + currentPotCandidate.getAmount());
                    }
                }
            }

            collapsed.remove(i);
        }

        for (int i = collapsed.size() - 1; i >= 0; i -= 1) {
            final PotData pot = collapsed.get(0);
            if (i == 0) {
                pot.setName("Main pot");
            } else {
                pot.setName("Side pot #" + i);
            }
        }

        return collapsed;
    }

    @Data
    @AllArgsConstructor
    public static class CandidateEvaluation {
        private CandidateData candidate;
        private IEvaluation evaluation;
        private String groupId;
    }

    public static List<CandidateData> determineWinners(List<PotData> pots, long communityCards, int buttonAt, int seatCount) {
        final List<PotData> potsCopy = new ArrayList<>(pots);
        final List<PotData> collapsedPots = collapsePots(potsCopy);

        final IHandProcessor processor = new TexasHoldemProcessor();

        final List<CandidateData> winners = new ArrayList<>();

        int potIndex = -1;
        while (!collapsedPots.isEmpty()) {
            potIndex += 1;
            final PotData pot = collapsedPots.remove(0);
            final List<CandidateData> candidates = pot.getCandidates().stream().filter(i -> !i.getSeat().isHasFolded()).toList();

            final List<CandidateEvaluation> evaluations = candidates.stream()
                    .map(i -> {
                        final IEvaluation evaluation = HandEvaluatorUtil.evaluate(processor, i.getSeat().getCards() | communityCards);
                        final String groupId = HandEvaluatorUtil.toId(evaluation);
                        return new CandidateEvaluation(i, evaluation, groupId);
                    })
                    .toList();

            final List<CandidateEvaluation> sortedEvaluations = evaluations.stream().sorted((e1, e2) -> {
                try {
                    return -1 * HandEvaluatorUtil.compare(processor, e1.getEvaluation().getHand(), e2.getEvaluation().getHand());
                } catch (HandEvaluatorException e) {
                    throw new RuntimeException(e);
                }
            }).toList();

            final List<String> sortedGroupIds = sortedEvaluations.stream().map(i -> i.groupId).distinct().toList();

            final Map<String, List<CandidateEvaluation>> groups = evaluations.stream().collect(Collectors.groupingBy(i -> i.groupId));

            int remainingPot = pot.getTotalAmount();
            for (String groupId : sortedGroupIds) {
                if (remainingPot == 0) break;
                if (remainingPot < 0) throw new RuntimeException("The remaining pot must never be negative");
                final List<CandidateEvaluation> group = groups.get(groupId);
                int winnerCount = group.size();
                int delivered = 0;
                int portion = remainingPot / winnerCount;
                int rest = remainingPot - portion * winnerCount;

                List<CandidateData> groupWinners = new ArrayList<>();
                for (CandidateEvaluation e : group) {
                    delivered += portion;

                    final CandidateData candidate = new CandidateData(e.candidate.getSeat(), portion);

                    if (potIndex == 0) candidate.setPotName("Main pot");
                    else candidate.setPotName("Side pot #" + potIndex);

                    groupWinners.add(candidate);
                }

                if (rest > 0) {
                    final List<CandidateData> sortedWinnersBySeatIndex = groupWinners.stream()
                            .sorted(Comparator.comparingInt(e -> e.getSeat().getSeatIndex()))
                            .toList();

                    int firstWinnerIndexAfterButton = getFirstWinnerIndexAfterButton(buttonAt, seatCount, sortedWinnersBySeatIndex);

                    for (int i = 0; i < sortedWinnersBySeatIndex.size(); i += 1) {
                        if (rest == 0) break;
                        if (rest < 0) throw new RuntimeException("The rest pot must never be negative");

                        final int seatIndex = (firstWinnerIndexAfterButton + i) % sortedWinnersBySeatIndex.size();
                        final CandidateData aWinner = sortedWinnersBySeatIndex.get(seatIndex);

                        aWinner.setAmount(aWinner.getAmount() + 1);
                        delivered += 1;
                        rest -= 1;
                    }
                }

                remainingPot -= delivered;

                winners.addAll(groupWinners);
            }
        }

        return winners;
    }

    private static Integer getFirstWinnerIndexAfterButton(int buttonAt, int seatCount, List<CandidateData> sortedWinnersBySeatIndex) {
        Integer firstWinnerIndexAfterButton = null;
        for (int i = 0; i < sortedWinnersBySeatIndex.size(); i += 1) {
            final CandidateData aWinner = sortedWinnersBySeatIndex.get(i);
            final int seatIndex = aWinner.getSeat().getSeatIndex();
            int seatIndexAfterButton = (buttonAt + 1) % seatCount;
            if (seatIndex >= seatIndexAfterButton) {
                firstWinnerIndexAfterButton = i;
                break;
            }
        }

        if (firstWinnerIndexAfterButton == null) {
            throw new RuntimeException("Could not find the first candidate after the button");
        }
        return firstWinnerIndexAfterButton;
    }
}
