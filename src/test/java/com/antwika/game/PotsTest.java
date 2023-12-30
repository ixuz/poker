package com.antwika.game;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PotsTest {
    private static final Logger logger = LoggerFactory.getLogger(PotsTest.class);
    @Test
    public void collectBets_whenNoCandidates_noPots() {
        // Arrange
        final List<Seat> seats = List.of();

        // Act
        final List<Pot> pots = Pots.collectBets(seats);

        // Assert
        assertEquals(0, pots.size());
    }

    @Test
    public void collectBets_whenThereAreCandidatesButNoneCommitted_noPots() {
        // Arrange
        final Seat seat1 = new Seat();
        seat1.setSeatIndex(0);
        seat1.setCommitted(0);
        final Seat seat2 = new Seat();
        seat2.setSeatIndex(1);
        seat2.setCommitted(0);
        final List<Seat> seats = List.of(seat1, seat2);

        // Act
        final List<Pot> pots = Pots.collectBets(seats);

        // Assert
        assertEquals(0, pots.size());
    }

    @Test
    public void collectBets_whenOnlyOneCandidate_onlyMainPot() {
        // Arrange
        final Seat seat1 = new Seat();
        seat1.setSeatIndex(0);
        seat1.setCommitted(1);
        final List<Seat> seats = List.of(seat1);

        // Act
        final List<Pot> pots = Pots.collectBets(seats);

        // Assert
        assertEquals(1, pots.size());

        final Pot mainPot = pots.get(0);
        assertEquals(1, mainPot.getTotalAmount());
        assertEquals(1, mainPot.getAmountPerCandidate());
        assertEquals(1, mainPot.getCandidates().size());
        assertEquals(seat1, mainPot.getCandidates().get(0).getSeat());
    }

    @Test
    public void collectBets_whenHeadsUp_onlyMainPot() {
        // Arrange
        final Seat seat1 = new Seat();
        seat1.setSeatIndex(0);
        seat1.setCommitted(1);
        final Seat seat2 = new Seat();
        seat2.setSeatIndex(1);
        seat2.setCommitted(1);
        final List<Seat> seats = List.of(seat1, seat2);

        // Act
        final List<Pot> pots = Pots.collectBets(seats);

        // Assert
        assertEquals(1, pots.size());

        final Pot mainPot = pots.get(0);
        assertEquals(2, mainPot.getTotalAmount());
        assertEquals(1, mainPot.getAmountPerCandidate());
        assertEquals(2, mainPot.getCandidates().size());
        assertEquals(seat1, mainPot.getCandidates().get(0).getSeat());
        assertEquals(seat2, mainPot.getCandidates().get(1).getSeat());
    }

    @Test
    public void collectBets_whenThreeWay_onlyMainPot() {
        // Arrange
        final Seat seat1 = new Seat();
        seat1.setSeatIndex(0);
        seat1.setCommitted(1);
        final Seat seat2 = new Seat();
        seat2.setSeatIndex(1);
        seat2.setCommitted(1);
        final Seat seat3 = new Seat();
        seat3.setSeatIndex(2);
        seat3.setCommitted(1);
        final List<Seat> seats = List.of(seat1, seat2, seat3);

        // Act
        final List<Pot> pots = Pots.collectBets(seats);

        // Assert
        assertEquals(1, pots.size());

        final Pot mainPot = pots.get(0);
        assertEquals(3, mainPot.getTotalAmount());
        assertEquals(1, mainPot.getAmountPerCandidate());
        assertEquals(3, mainPot.getCandidates().size());
        assertEquals(seat1, mainPot.getCandidates().get(0).getSeat());
        assertEquals(seat2, mainPot.getCandidates().get(1).getSeat());
        assertEquals(seat3, mainPot.getCandidates().get(2).getSeat());
    }

    @Test
    public void collectBets_whenThreeWayAndOneAllIn_mainPotAndOneSidePot() {
        // Arrange
        final Seat seat1 = new Seat();
        seat1.setSeatIndex(0);
        seat1.setCommitted(2);
        final Seat seat2 = new Seat();
        seat2.setSeatIndex(1);
        seat2.setCommitted(2);
        final Seat seat3 = new Seat();
        seat3.setSeatIndex(2);
        seat3.setCommitted(1);
        final List<Seat> seats = List.of(seat1, seat2, seat3);

        // Act
        final List<Pot> pots = Pots.collectBets(seats);

        // Assert
        assertEquals(2, pots.size());

        final Pot mainPot = pots.get(0);
        assertEquals(3, mainPot.getTotalAmount());
        assertEquals(1, mainPot.getAmountPerCandidate());
        assertEquals(3, mainPot.getCandidates().size());
        assertEquals(seat3, mainPot.getCandidates().get(0).getSeat());
        assertEquals(seat1, mainPot.getCandidates().get(1).getSeat());
        assertEquals(seat2, mainPot.getCandidates().get(2).getSeat());

        final Pot sidePot = pots.get(1);
        assertEquals(2, sidePot.getTotalAmount());
        assertEquals(1, sidePot.getAmountPerCandidate());
        assertEquals(2, sidePot.getCandidates().size());
        assertEquals(seat1, sidePot.getCandidates().get(0).getSeat());
        assertEquals(seat2, sidePot.getCandidates().get(1).getSeat());
    }

    @Test
    public void collectBets_whenFourWayAndThreeAllIn_mainPotAndOneSidePot() {
        // Arrange
        final Seat seat1 = new Seat();
        seat1.setSeatIndex(0);
        seat1.setCommitted(2);
        final Seat seat2 = new Seat();
        seat2.setSeatIndex(1);
        seat2.setCommitted(2);
        final Seat seat3 = new Seat();
        seat3.setSeatIndex(2);
        seat3.setCommitted(5);
        final Seat seat4 = new Seat();
        seat4.setSeatIndex(3);
        seat4.setCommitted(5);
        final List<Seat> seats = List.of(seat1, seat2, seat3, seat4);

        // Act
        final List<Pot> pots = Pots.collectBets(seats);

        // Assert
        assertEquals(2, pots.size());

        final Pot mainPot = pots.get(0);
        assertEquals(8, mainPot.getTotalAmount());
        assertEquals(2, mainPot.getAmountPerCandidate());
        assertEquals(4, mainPot.getCandidates().size());
        assertEquals(seat1, mainPot.getCandidates().get(0).getSeat());
        assertEquals(seat2, mainPot.getCandidates().get(1).getSeat());
        assertEquals(seat3, mainPot.getCandidates().get(2).getSeat());
        assertEquals(seat4, mainPot.getCandidates().get(3).getSeat());

        final Pot sidePot = pots.get(1);
        assertEquals(6, sidePot.getTotalAmount());
        assertEquals(3, sidePot.getAmountPerCandidate());
        assertEquals(2, sidePot.getCandidates().size());
        assertEquals(seat3, sidePot.getCandidates().get(0).getSeat());
        assertEquals(seat4, sidePot.getCandidates().get(1).getSeat());
    }

    @Test
    public void collectBets_whenFourWayAndThreeAllIn_mainPotAndTwoSidePots() {
        // Arrange
        final Seat seat1 = new Seat();
        seat1.setSeatIndex(0);
        seat1.setCommitted(2);
        final Seat seat2 = new Seat();
        seat2.setSeatIndex(1);
        seat2.setCommitted(3);
        final Seat seat3 = new Seat();
        seat3.setSeatIndex(2);
        seat3.setCommitted(5);
        final Seat seat4 = new Seat();
        seat4.setSeatIndex(3);
        seat4.setCommitted(5);
        final List<Seat> seats = List.of(seat1, seat2, seat3, seat4);

        // Act
        final List<Pot> pots = Pots.collectBets(seats);

        // Assert
        assertEquals(3, pots.size());

        final Pot mainPot = pots.get(0);
        assertEquals(8, mainPot.getTotalAmount());
        assertEquals(2, mainPot.getAmountPerCandidate());
        assertEquals(4, mainPot.getCandidates().size());
        assertEquals(seat1, mainPot.getCandidates().get(0).getSeat());
        assertEquals(seat2, mainPot.getCandidates().get(1).getSeat());
        assertEquals(seat3, mainPot.getCandidates().get(2).getSeat());
        assertEquals(seat4, mainPot.getCandidates().get(3).getSeat());

        final Pot sidePot1 = pots.get(1);
        assertEquals(3, sidePot1.getTotalAmount());
        assertEquals(1, sidePot1.getAmountPerCandidate());
        assertEquals(3, sidePot1.getCandidates().size());
        assertEquals(seat2, sidePot1.getCandidates().get(0).getSeat());
        assertEquals(seat3, sidePot1.getCandidates().get(1).getSeat());
        assertEquals(seat4, sidePot1.getCandidates().get(2).getSeat());

        final Pot sidePot2 = pots.get(2);
        assertEquals(4, sidePot2.getTotalAmount());
        assertEquals(2, sidePot2.getAmountPerCandidate());
        assertEquals(2, sidePot2.getCandidates().size());
        assertEquals(seat3, sidePot2.getCandidates().get(0).getSeat());
        assertEquals(seat4, sidePot2.getCandidates().get(1).getSeat());
    }

    @Test
    public void determineWinners_noPots() throws NotationException {
        // Arrange
        final List<Pot> pots = List.of();

        // Act
        final List<Candidate> winners = Pots.determineWinners(pots, HandUtil.fromNotation("2c4d6h8sTc").getBitmask(), 0, 10);

        // Assert
        assertEquals(0, winners.size());
    }

    @Test
    public void determineWinners_mainPotButNoCandidates() throws NotationException {
        // Arrange
        final List<Pot> pots = List.of(new Pot(1, List.of()));

        // Act
        final List<Candidate> winners = Pots.determineWinners(pots, HandUtil.fromNotation("2c4d6h8sTc").getBitmask(), 0, 10);

        // Assert
        assertEquals(0, winners.size());
    }

    @Test
    public void determineWinners_mainPotAndOneCandidate() throws NotationException {
        // Arrange
        final Seat seat1 = new Seat();
        seat1.setSeatIndex(0);
        seat1.setCards(HandUtil.fromNotation("AcQd").getBitmask());
        final List<Candidate> candidates = List.of(new Candidate(seat1, 1));
        final List<Pot> pots = List.of(new Pot(1, candidates));

        // Act
        final List<Candidate> winners = Pots.determineWinners(pots, HandUtil.fromNotation("2c4d6h8sTc").getBitmask(), 0, 1);

        // Assert
        assertEquals(1, winners.size());
        assertEquals(seat1, winners.get(0).getSeat());
        assertEquals(1, winners.get(0).getAmount());
    }

    @Test
    public void determineWinners_singleMainPotWinner_winnerTakesAll() throws NotationException {
        // Arrange
        final Seat seat1 = new Seat();
        seat1.setSeatIndex(0);
        seat1.setCards(HandUtil.fromNotation("AcQd").getBitmask());
        final Seat seat2 = new Seat();
        seat2.setSeatIndex(1);
        seat2.setCards(HandUtil.fromNotation("AhJs").getBitmask());
        final List<Candidate> candidates = List.of(
                new Candidate(seat1, 1),
                new Candidate(seat2, 1)
        );
        final List<Pot> pots = List.of(new Pot(1, candidates));

        // Act
        final List<Candidate> winners = Pots.determineWinners(pots, HandUtil.fromNotation("2c4d6h8sTc").getBitmask(), 0, 2);

        // Assert
        assertEquals(1, winners.size());
        assertEquals(seat1, winners.get(0).getSeat());
        assertEquals(2, winners.get(0).getAmount());
    }

    @Test
    public void determineWinners_twoMainPotWinners_winnerSplitEven() throws NotationException {
        // Arrange
        final Seat seat1 = new Seat();
        seat1.setSeatIndex(0);
        seat1.setCards(HandUtil.fromNotation("AcAd").getBitmask());
        final Seat seat2 = new Seat();
        seat2.setSeatIndex(1);
        seat2.setCards(HandUtil.fromNotation("AhAs").getBitmask());
        final List<Candidate> candidates = List.of(
                new Candidate(seat1, 1),
                new Candidate(seat2, 1)
        );
        final List<Pot> pots = List.of(new Pot(1, candidates));

        // Act
        final List<Candidate> winners = Pots.determineWinners(pots, HandUtil.fromNotation("2c4d6h8sTc").getBitmask(), 0, 2);

        // Assert
        assertEquals(2, winners.size());

        assertEquals(seat1, winners.get(0).getSeat());
        assertEquals(1, winners.get(0).getAmount());

        assertEquals(seat2, winners.get(1).getSeat());
        assertEquals(1, winners.get(1).getAmount());
    }

    @Test
    public void determineWinners_mainPotAndSidePot() throws NotationException {
        // Arrange
        final Seat seat1 = new Seat();
        seat1.setSeatIndex(0);
        seat1.setCards(HandUtil.fromNotation("AcAd").getBitmask());
        final Seat seat2 = new Seat();
        seat2.setSeatIndex(1);
        seat2.setCards(HandUtil.fromNotation("KhKs").getBitmask());
        final Seat seat3 = new Seat();
        seat3.setSeatIndex(2);
        seat3.setCards(HandUtil.fromNotation("QhQs").getBitmask());
        final List<Pot> pots = List.of(
                new Pot(1,
                        List.of(
                                new Candidate(seat1, 1),
                                new Candidate(seat2, 1),
                                new Candidate(seat3, 1)
                        )
                ),
                new Pot(4,
                        List.of(
                                new Candidate(seat2, 4),
                                new Candidate(seat3, 4)
                        )
                )
        );

        // Act
        final List<Candidate> winners = Pots.determineWinners(pots, HandUtil.fromNotation("2c4d6h8sTc").getBitmask(), 0, 3);

        // Assert
        assertEquals(2, winners.size());

        assertEquals(seat1, winners.get(0).getSeat());
        assertEquals(3, winners.get(0).getAmount());

        assertEquals(seat2, winners.get(1).getSeat());
        assertEquals(8, winners.get(1).getAmount());
    }

    @Test
    public void determineWinners_complexSituation() throws NotationException {
        // Arrange
        final Seat seat1 = new Seat();
        seat1.setSeatIndex(0);
        seat1.setCards(HandUtil.fromNotation("AcAd").getBitmask());
        final Seat seat2 = new Seat();
        seat2.setSeatIndex(1);
        seat2.setCards(HandUtil.fromNotation("Td9d").getBitmask());
        final Seat seat3 = new Seat();
        seat3.setSeatIndex(2);
        seat3.setCards(HandUtil.fromNotation("Th9h").getBitmask());
        final Seat seat4 = new Seat();
        seat4.setSeatIndex(3);
        seat4.setCards(HandUtil.fromNotation("Ts9s").getBitmask());
        final Seat seat5 = new Seat();
        seat5.setSeatIndex(4);
        seat5.setCards(HandUtil.fromNotation("9c9d").getBitmask());
        final Seat seat6 = new Seat();
        seat6.setSeatIndex(5);
        seat6.setCards(HandUtil.fromNotation("7c7d").getBitmask());
        final List<Pot> pots = List.of(
                new Pot(1,
                        List.of(
                                new Candidate(seat1, 1),
                                new Candidate(seat2, 1),
                                new Candidate(seat3, 1),
                                new Candidate(seat4, 1),
                                new Candidate(seat5, 1),
                                new Candidate(seat6, 1)
                        )
                ),
                new Pot(4,
                        List.of(
                                new Candidate(seat2, 4),
                                new Candidate(seat3, 4),
                                new Candidate(seat4, 4),
                                new Candidate(seat5, 4),
                                new Candidate(seat6, 4)
                        )
                ),
                new Pot(6,
                        List.of(
                                new Candidate(seat5, 6),
                                new Candidate(seat6, 6)
                        )
                )
        );

        // Act
        final List<Candidate> winners = Pots.determineWinners(pots, HandUtil.fromNotation("2c4d6h8sTc").getBitmask(), 0, 6);

        // Assert
        assertEquals(5, winners.size());

        assertEquals(seat1, winners.get(0).getSeat());
        assertEquals(6, winners.get(0).getAmount());

        assertEquals(seat2, winners.get(1).getSeat());
        assertEquals(7, winners.get(1).getAmount());

        assertEquals(seat3, winners.get(2).getSeat());
        assertEquals(7, winners.get(2).getAmount());

        assertEquals(seat4, winners.get(3).getSeat());
        assertEquals(6, winners.get(3).getAmount());

        assertEquals(seat5, winners.get(4).getSeat());
        assertEquals(12, winners.get(4).getAmount());
    }

    @Test
    public void determineWinners_afterSeriesOfCollects() throws NotationException {
        // Arrange
        final Seat seat1 = new Seat();
        seat1.setSeatIndex(0);
        seat1.setCommitted(0);
        seat1.setCards(HandUtil.fromNotation("AcAd").getBitmask());
        final Seat seat2 = new Seat();
        seat2.setSeatIndex(1);
        seat2.setCommitted(0);
        seat2.setCards(HandUtil.fromNotation("Td9d").getBitmask());
        final Seat seat3 = new Seat();
        seat3.setSeatIndex(2);
        seat3.setCommitted(0);
        seat3.setCards(HandUtil.fromNotation("Th9h").getBitmask());
        final Seat seat4 = new Seat();
        seat4.setSeatIndex(3);
        seat4.setCommitted(0);
        seat4.setCards(HandUtil.fromNotation("4h5h").getBitmask());

        final List<Seat> seats = List.of(seat1, seat2, seat3, seat4);

        final List<Pot> pots = new ArrayList<>();

        // Betting round
        seat2.setCommitted(10); // SB: Post small blind
        seat3.setCommitted(20); // BB: Post big blind
        seat4.setCommitted(20); // CO: Limp
        seat1.setCommitted(100); // BU: Raise
        seat2.setCommitted(seat2.getCommitted() + 90); // SB: Call
        seat3.setCommitted(seat3.getCommitted() + 80); // BB: Call
        seat4.setCommitted(seat4.getCommitted() + 80); // CO: Call

        // Collect
        pots.addAll(Pots.collectBets(seats));
        assertEquals(0, seat1.getCommitted());
        assertEquals(0, seat2.getCommitted());
        assertEquals(0, seat3.getCommitted());
        assertEquals(0, seat4.getCommitted());

        assertEquals(1, pots.size());
        assertEquals(100, pots.get(0).getAmountPerCandidate());
        assertEquals(400, pots.get(0).getTotalAmount());

        // Betting round
        seat2.setCommitted(250); // SB: Bet (all-in)
        seat3.setCommitted(250); // BB: Call
        seat4.setCommitted(850); // CO: Raise
        seat1.setCommitted(500); // BU: Call (all-in)
        seat3.setCommitted(seat3.getCommitted() + 400); // BB: Call (all-in)

        // Collect
        pots.addAll(Pots.collectBets(seats));
        assertEquals(0, seat1.getCommitted());
        assertEquals(0, seat2.getCommitted());
        assertEquals(0, seat3.getCommitted());
        assertEquals(0, seat4.getCommitted());

        assertEquals(5, pots.size());
        assertEquals(100, pots.get(0).getAmountPerCandidate());
        assertEquals(400, pots.get(0).getTotalAmount());
        assertEquals(250, pots.get(1).getAmountPerCandidate());
        assertEquals(1000, pots.get(1).getTotalAmount());
        assertEquals(250, pots.get(2).getAmountPerCandidate());
        assertEquals(750, pots.get(2).getTotalAmount());
        assertEquals(150, pots.get(3).getAmountPerCandidate());
        assertEquals(300, pots.get(3).getTotalAmount());
        assertEquals(200, pots.get(4).getAmountPerCandidate());
        assertEquals(200, pots.get(4).getTotalAmount());

        // Determine winnings
        final List<Candidate> winners = Pots.determineWinners(pots, HandUtil.fromNotation("2c4d6h8sTc").getBitmask(), 0, 4);

        assertEquals(5, winners.size());

        assertEquals(400, winners.get(0).getAmount());
        assertEquals(seat1, winners.get(0).getSeat());
        assertEquals(1000, winners.get(1).getAmount());
        assertEquals(seat1, winners.get(1).getSeat());
        assertEquals(750, winners.get(2).getAmount());
        assertEquals(seat1, winners.get(2).getSeat());
        assertEquals(300, winners.get(3).getAmount());
        assertEquals(seat3, winners.get(3).getSeat());
        assertEquals(200, winners.get(4).getAmount());
        assertEquals(seat4, winners.get(4).getSeat());

        // Deliver winnings
        for (Candidate candidate : winners) {
            logger.info("Seat #{} won {}", candidate.getSeat().getSeatIndex(), candidate.getAmount());
        }
    }

    @Test
    public void equality() {
        final Seat seat = new Seat();
        final Candidate candidate1 = new Candidate(seat, 100);
        final Candidate candidate2 = new Candidate(seat, 100);
        final Candidate candidate3 = new Candidate(seat, 200);
        final Candidate candidate4 = new Candidate(seat, 200);
        final List<Candidate> candidates1 = List.of(candidate1, candidate2);
        final List<Candidate> candidates2 = List.of(candidate3, candidate4);
        final Pot pot1 = new Pot(200, candidates1);
        final Pot pot2 = new Pot(200, candidates2);
        assertTrue(Pots.hasSameCandidates(pot1, pot2));
    }

    @Test
    public void collapsePots_whenCandidateSeatsAreNotTheSameInBothPots_doNotCollapsePot() {
        // Arrange
        final Seat seat1 = new Seat();
        final Seat seat2 = new Seat();
        final Seat seat3 = new Seat();

        final List<Pot> pots = List.of(
                new Pot(100, List.of(
                        new Candidate(seat1, 100),
                        new Candidate(seat2, 100)
                )),
                new Pot(250, List.of(
                        new Candidate(seat1, 250),
                        new Candidate(seat2, 250),
                        new Candidate(seat3, 250)
                ))
        );

        // Act
        final List<Pot> collapsed = Pots.collapsePots(pots);

        // Assert
        assertEquals(2, collapsed.size());
        assertEquals(100, collapsed.get(0).getAmountPerCandidate());
        assertEquals(200, collapsed.get(0).getTotalAmount());
        assertEquals(250, collapsed.get(1).getAmountPerCandidate());
        assertEquals(750, collapsed.get(1).getTotalAmount());
    }

    @Test
    public void collapsePots_whenCandidateSeatsAreTheSameInBothPots_collapsePot() {
        // Arrange
        final Seat seat1 = new Seat();
        final Seat seat2 = new Seat();

        final List<Pot> pots = List.of(
                new Pot(100, List.of(
                        new Candidate(seat1, 100),
                        new Candidate(seat2, 100)
                )),
                new Pot(250, List.of(
                        new Candidate(seat1, 250),
                        new Candidate(seat2, 250)
                ))
        );

        // Act
        final List<Pot> collapsed = Pots.collapsePots(pots);

        // Assert
        assertEquals(1, collapsed.size());
        assertEquals(350, collapsed.get(0).getAmountPerCandidate());
        assertEquals(700, collapsed.get(0).getTotalAmount());
    }

    @Test
    public void determineWinners_forCollapsedPot() throws NotationException {
        // Arrange
        final Seat seat1 = new Seat();
        seat1.setSeatIndex(0);
        seat1.setCards(HandUtil.fromNotation("AcAd").getBitmask());
        final Seat seat2 = new Seat();
        seat2.setSeatIndex(1);
        seat2.setCards(HandUtil.fromNotation("AhAs").getBitmask());
        final List<Candidate> candidates1 = List.of(
                new Candidate(seat1, 1),
                new Candidate(seat2, 1)
        );
        final List<Candidate> candidates2 = List.of(
                new Candidate(seat1, 2),
                new Candidate(seat2, 2)
        );
        final List<Pot> pots = List.of(
                new Pot(1, candidates1),
                new Pot(2, candidates2)
        );

        // Act
        final List<Candidate> winners = Pots.determineWinners(pots, HandUtil.fromNotation("2c4d6h8sTc").getBitmask(), 0, 2);

        // Assert
        assertEquals(2, winners.size());

        assertEquals(seat1, winners.get(0).getSeat());
        assertEquals(3, winners.get(0).getAmount());

        assertEquals(seat2, winners.get(1).getSeat());
        assertEquals(3, winners.get(1).getAmount());
    }
}
