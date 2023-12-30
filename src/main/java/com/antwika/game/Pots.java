package com.antwika.game;

import com.antwika.eval.core.IEvaluation;
import com.antwika.eval.core.IHandProcessor;
import com.antwika.eval.exception.HandEvaluatorException;
import com.antwika.eval.processor.TexasHoldemProcessor;
import com.antwika.eval.util.HandEvaluatorUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Pots {
    private static final Logger logger = LoggerFactory.getLogger(Pots.class);

    public static List<Pot> collectBets(List<Seat> seats) {
        final List<Pot> pots = new ArrayList<>();

        final List<Seat> sorted = new ArrayList<>(seats.stream()
                .filter(i -> i.getCommitted() > 0)
                .sorted(Comparator.comparingInt(Seat::getCommitted))
                .toList());

        while (!sorted.isEmpty()) {
            int potAmount = 0;
            final List<Candidate> candidates = new ArrayList<>();
            for (int i = 0; i < sorted.size(); i += 1) {
                final Seat seat = sorted.get(i);
                if (i == 0) {
                    potAmount = seat.getCommitted();
                }

                candidates.add(new Candidate(seat, potAmount));
                seat.setCommitted(seat.getCommitted() - potAmount);
            }

            pots.add(new Pot(potAmount, candidates));

            sorted.removeAll(sorted.stream().filter(i -> i.getCommitted() == 0).toList());
        }

        return pots;
    }

    public static boolean hasSameCandidates(Pot pot1, Pot pot2) {
        final List<Seat> potCandidates1 = pot1.getCandidates().stream().map(Candidate::getSeat).toList();
        final List<Seat> potCandidates2 = pot2.getCandidates().stream().map(Candidate::getSeat).toList();
        return potCandidates1.equals(potCandidates2);
    }

    public static List<Pot> collapsePots(List<Pot> pots) {
        final List<Pot> collapsed = new ArrayList<>(pots);

        for (int i = 0; i < collapsed.size() - 1; i++) {
            final Pot currentPot = collapsed.get(i);
            final Pot nextPot = collapsed.get(i + 1);

            if (!hasSameCandidates(currentPot, nextPot)) {
                continue;
            }

            nextPot.setTotalAmount(nextPot.getTotalAmount() + currentPot.getTotalAmount());
            nextPot.setAmountPerCandidate(nextPot.getAmountPerCandidate() + currentPot.getAmountPerCandidate());

            for (Candidate nextPotCandidate : nextPot.getCandidates()) {
                for (Candidate currentPotCandidate : currentPot.getCandidates()) {
                    if (currentPotCandidate.getSeat() == nextPotCandidate.getSeat()) {
                        nextPotCandidate.setAmount(nextPotCandidate.getAmount() + currentPotCandidate.getAmount());
                    }
                }
            }

            collapsed.remove(i);
        }

        for (int i = collapsed.size() - 1; i >= 0; i -= 1) {
            final Pot pot = collapsed.get(0);
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
        private Candidate candidate;
        private IEvaluation evaluation;
        private String groupId;
    }

    public static List<Candidate> determineWinners(List<Pot> pots, long communityCards, int buttonAt, int seatCount) {
        final List<Pot> potsCopy = new ArrayList<>(pots);
        final List<Pot> collapsedPots = collapsePots(potsCopy);

        final IHandProcessor processor = new TexasHoldemProcessor();

        final List<Candidate> winners = new ArrayList<>();

        int potIndex = -1;
        while (!collapsedPots.isEmpty()) {
            potIndex += 1;
            final Pot pot = collapsedPots.remove(0);
            final List<Candidate> candidates = pot.getCandidates().stream().filter(i -> !i.getSeat().isHasFolded()).toList();

            final List<CandidateEvaluation> evaluations = candidates.stream()
                    .map(i -> {
                        final IEvaluation evaluation = HandEvaluatorUtil.evaluate(processor, i.getSeat().getCards() | communityCards);
                        final String groupId = groupId(evaluation);
                        return new CandidateEvaluation(i, evaluation, groupId);
                    })
                    .toList();

            // evaluations.sort((e1, e2) -> HandEvaluatorUtil.compare(processor, e1.getHand(), e2.getHand()));
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
                int candidateCount = pot.getCandidates().size();
                int winnerCount = group.size();
                int delivered = 0;
                int portion = remainingPot / winnerCount;
                int rest = remainingPot - portion * winnerCount;

                List<Candidate> groupWinners = new ArrayList<>();
                for (CandidateEvaluation e : group) {
                    int candidatePortion = portion;
                    delivered += candidatePortion;

                    final Candidate candidate = new Candidate(e.candidate.getSeat(), candidatePortion);

                    if (potIndex == 0) candidate.setPotName("Main pot");
                    else candidate.setPotName("Side pot #" + potIndex);

                    groupWinners.add(candidate);
                }

                if (rest > 0) {
                    final List<Candidate> sortedWinnersBySeatIndex = groupWinners.stream()
                            .sorted(Comparator.comparingInt(e -> e.getSeat().getSeatIndex()))
                            .toList();

                    Integer firstWinnerIndexAfterButton = null;
                    for (int i = 0; i < sortedWinnersBySeatIndex.size(); i += 1) {
                        final Candidate aWinner = sortedWinnersBySeatIndex.get(i);
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

                    for (int i = 0; i < sortedWinnersBySeatIndex.size(); i += 1) {
                        if (rest == 0) break;
                        if (rest < 0) throw new RuntimeException("The rest pot must never be negative");

                        final int seatIndex = (firstWinnerIndexAfterButton + i) % sortedWinnersBySeatIndex.size();
                        final Candidate aWinner = sortedWinnersBySeatIndex.get(seatIndex);

                        aWinner.setAmount(aWinner.getAmount() + 1);
                        delivered += 1;
                        rest -= 1;
                    }
                }

                remainingPot -= delivered;

                winners.addAll(groupWinners);
            }

            // logger.info("hello");
        }

        return winners;
    }

    private static String groupId(IEvaluation evaluation) {
        final StringBuilder sb = new StringBuilder(String.valueOf(evaluation.getHandType()));
        for (int kicker : evaluation.getKickers()) {
            sb.append(":");
            sb.append(kicker);
        }
        return sb.toString();
    }
}
