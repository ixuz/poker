package com.antwika.table.util;

import com.antwika.eval.core.IEvaluation;
import com.antwika.eval.core.IHandProcessor;
import com.antwika.eval.exception.HandEvaluatorException;
import com.antwika.eval.processor.TexasHoldemProcessor;
import com.antwika.eval.util.HandEvaluatorUtil;
import com.antwika.table.data.CandidateData;
import com.antwika.table.data.CandidateEvaluationData;
import com.antwika.table.player.Player;
import com.antwika.table.data.PotData;
import com.antwika.table.data.SeatData;
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

                candidates.add(new CandidateData("Pot", seat, potAmount));
                seat.setCommitted(seat.getCommitted() - potAmount);
            }

            pots.add(new PotData("Pot", potAmount, candidates));

            sorted.removeAll(sorted.stream().filter(i -> i.getCommitted() == 0).toList());
        }

        if (!pots.isEmpty()) {
            final int lastSidePotIndex = pots.size() - 1;
            final PotData lastSidePot = pots.get(lastSidePotIndex);

            if (lastSidePot.candidates().size() == 1) {
                final CandidateData candidate = lastSidePot.candidates().get(0);
                final SeatData seat = candidate.seat();
                final Player player = seat.getPlayer();
                final int amount = lastSidePot.totalAmount();
                seat.setStack(seat.getStack() + amount);
                pots.remove(lastSidePot);
                logger.info("Uncalled bet ({}) returned to {}", lastSidePot.totalAmount(), player.getPlayerData().getPlayerName());
            }
        }

        return pots;
    }

    public static boolean hasSameCandidates(PotData pot1, PotData pot2) {
        final List<SeatData> potCandidates1 = pot1.candidates().stream().map(CandidateData::seat).filter(i -> !i.isHasFolded()).toList();
        final List<SeatData> potCandidates2 = pot2.candidates().stream().map(CandidateData::seat).filter(i -> !i.isHasFolded()).toList();

        if (potCandidates1.size() != potCandidates2.size()) return false;

        final List<SeatData> scratch = new ArrayList<>(potCandidates1);
        scratch.removeAll(potCandidates2);

        return scratch.isEmpty();
    }

    public static List<PotData> collapsePots(List<PotData> pots) {
        final List<PotData> collapsed = new ArrayList<>();

        for (PotData pot : pots) {
            final List<CandidateData> ineligible = new ArrayList<>();
            final List<CandidateData> eligible = new ArrayList<>(pot.candidates());
            for (CandidateData candidate : pot.candidates()) {
                if (candidate.seat().isHasFolded()) {
                    ineligible.add(candidate);
                }
            }
            eligible.removeAll(ineligible);
            collapsed.add(new PotData("Pot", pot.amountPerCandidate(), eligible));
        }

        final var removePots = new ArrayList<PotData>();
        for (int i = 1; i < collapsed.size(); i++) {
            final PotData currentPot = collapsed.get(i - 1);
            final PotData nextPot = collapsed.get(i);

            if (!hasSameCandidates(currentPot, nextPot)) {
                continue;
            }

            nextPot.amountPerCandidate(nextPot.amountPerCandidate() + currentPot.amountPerCandidate());

            final var nextPotCandidates = nextPot.candidates();
            for (var nextPotCandidateIndex = 0; nextPotCandidateIndex < nextPotCandidates.size(); nextPotCandidateIndex++) {
                final var nextPotCandidate = nextPotCandidates.get(nextPotCandidateIndex);

                for (CandidateData currentPotCandidate : currentPot.candidates()) {
                    if (currentPotCandidate.seat() == nextPotCandidate.seat()) {

                        final var combinedAmount = nextPotCandidate.amount() + currentPotCandidate.amount();
                        final var combined = new CandidateData(nextPotCandidate.potName(), nextPotCandidate.seat(), combinedAmount);

                        nextPotCandidates.set(nextPotCandidateIndex, combined);
                    }
                }
            }

            removePots.add(currentPot);
        }

        collapsed.removeAll(removePots);

        namePots(collapsed);

        return collapsed;
    }

    private static void namePots(List<PotData> collapsed) {
        for (int i = collapsed.size() - 1; i >= 0; i -= 1) {
            final PotData pot = collapsed.get(0);
            if (i == 0) {
                pot.name("Main pot");
            } else {
                pot.name("Side pot #" + i);
            }
        }
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
            final List<CandidateData> candidates = pot.candidates().stream().filter(i -> !i.seat().isHasFolded()).toList();

            final List<CandidateEvaluationData> evaluations = candidates.stream()
                    .map(i -> {
                        final IEvaluation evaluation = HandEvaluatorUtil.evaluate(processor, i.seat().getCards() | communityCards);
                        final String groupId = HandEvaluatorUtil.toId(evaluation);
                        return new CandidateEvaluationData(i, evaluation, groupId);
                    })
                    .toList();

            final List<CandidateEvaluationData> sortedEvaluations = evaluations.stream().sorted((e1, e2) -> {
                try {
                    return -1 * HandEvaluatorUtil.compare(processor, e1.evaluation().getHand(), e2.evaluation().getHand());
                } catch (HandEvaluatorException e) {
                    throw new RuntimeException(e);
                }
            }).toList();

            final List<String> sortedGroupIds = sortedEvaluations.stream().map(CandidateEvaluationData::groupId).distinct().toList();

            final Map<String, List<CandidateEvaluationData>> groups = evaluations.stream().collect(Collectors.groupingBy(CandidateEvaluationData::groupId));

            int remainingPot = pot.totalAmount();
            for (String groupId : sortedGroupIds) {
                if (remainingPot == 0) break;
                if (remainingPot < 0) throw new RuntimeException("The remaining pot must never be negative");
                final List<CandidateEvaluationData> group = groups.get(groupId);
                int winnerCount = group.size();
                int delivered = 0;
                int portion = remainingPot / winnerCount;
                int rest = remainingPot - portion * winnerCount;

                List<CandidateData> groupWinners = getCandidateDataList(group, portion, potIndex);

                delivered += groupWinners.stream().mapToInt(CandidateData::amount).sum();

                if (rest > 0) {
                    final List<CandidateData> sortedWinnersBySeatIndex = new ArrayList<>(groupWinners.stream()
                            .sorted(Comparator.comparingInt(e -> e.seat().getSeatIndex()))
                            .toList());

                    Integer firstWinnerIndexAfterButton = getFirstWinnerIndexAfterButton(buttonAt, seatCount, sortedWinnersBySeatIndex);

                    if (firstWinnerIndexAfterButton == null) {
                        throw new RuntimeException("Could not find the first candidate after the button");
                    }

                    for (int i = 0; i < sortedWinnersBySeatIndex.size(); i += 1) {
                        if (rest == 0) break;
                        if (rest < 0) throw new RuntimeException("The rest pot must never be negative");

                        final int seatIndex = (firstWinnerIndexAfterButton + i) % sortedWinnersBySeatIndex.size();
                        final CandidateData aWinner = sortedWinnersBySeatIndex.get(seatIndex);

                        final var winner = new CandidateData(aWinner.potName(), aWinner.seat(), aWinner.amount() + 1);
                        sortedWinnersBySeatIndex.set(seatIndex, winner);

                        delivered += 1;
                        rest -= 1;
                    }

                    groupWinners = sortedWinnersBySeatIndex;
                }

                remainingPot -= delivered;

                winners.addAll(groupWinners);
            }
        }

        return winners;
    }

    private static List<CandidateData> getCandidateDataList(List<CandidateEvaluationData> group, int portion, int potIndex) {
        final List<CandidateData> groupWinners = new ArrayList<>();
        for (CandidateEvaluationData e : group) {
            final var potName = (potIndex == 0 ? "Main pot" : "Side pot #" + potIndex);
            groupWinners.add(new CandidateData(potName, e.candidate().seat(), portion));
        }
        return groupWinners;
    }

    private static Integer getFirstWinnerIndexAfterButton(int buttonAt, int seatCount, List<CandidateData> sortedWinnersBySeatIndex) {
        Integer firstWinnerIndexAfterButton = null;
        for (int i = 0; i < sortedWinnersBySeatIndex.size(); i += 1) {
            final CandidateData aWinner = sortedWinnersBySeatIndex.get(i);
            final int seatIndex = aWinner.seat().getSeatIndex();
            int seatIndexAfterButton = (buttonAt + 1) % seatCount;
            if (seatIndex >= seatIndexAfterButton) {
                firstWinnerIndexAfterButton = i;
                break;
            }
        }

        return firstWinnerIndexAfterButton;
    }
}
