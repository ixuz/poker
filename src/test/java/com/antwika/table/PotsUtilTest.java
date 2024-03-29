package com.antwika.table;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.table.data.CandidateData;
import com.antwika.table.data.PotData;
import com.antwika.table.data.SeatData;
import com.antwika.table.player.RandomPlayer;
import com.antwika.table.util.PotsUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PotsUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(PotsUtilTest.class);
    @Test
    public void collectBets_whenNoCandidates_noPots() {
        // Arrange
        final List<SeatData> seats = List.of();

        // Act
        final List<PotData> pots = PotsUtil.collectBets(seats);

        // Assert
        assertEquals(0, pots.size());
    }

    @Test
    public void collectBets_whenThereAreCandidatesButNoneCommitted_noPots() {
        // Arrange
        final SeatData seat1 = new SeatData();
        seat1.setSeatIndex(0);
        seat1.setCommitted(0);
        final SeatData seat2 = new SeatData();
        seat2.setSeatIndex(1);
        seat2.setCommitted(0);
        final List<SeatData> seats = List.of(seat1, seat2);

        // Act
        final List<PotData> pots = PotsUtil.collectBets(seats);

        // Assert
        assertEquals(0, pots.size());
    }

    @Test
    public void collectBets_whenOnlyOneCandidate_returnUncalledBet() {
        // Arrange
        final SeatData seat1 = new SeatData();
        seat1.setPlayer(new RandomPlayer(1L, "Alice"));
        seat1.setSeatIndex(0);
        seat1.setCommitted(1);
        final List<SeatData> seats = List.of(seat1);

        // Act
        final List<PotData> pots = PotsUtil.collectBets(seats);

        // Assert
        assertEquals(0, pots.size());

        assertEquals(0, seat1.getCommitted());
        assertEquals(1, seat1.getStack());
    }

    @Test
    public void collectBets_whenHeadsUp_onlyMainPot() {
        // Arrange
        final SeatData seat1 = new SeatData();
        seat1.setSeatIndex(0);
        seat1.setCommitted(1);
        final SeatData seat2 = new SeatData();
        seat2.setSeatIndex(1);
        seat2.setCommitted(1);
        final List<SeatData> seats = List.of(seat1, seat2);

        // Act
        final List<PotData> pots = PotsUtil.collectBets(seats);

        // Assert
        assertEquals(1, pots.size());

        final PotData mainPot = pots.get(0);
        assertEquals(2, mainPot.totalAmount());
        assertEquals(1, mainPot.amountPerCandidate());
        assertEquals(2, mainPot.candidates().size());
        assertEquals(seat1, mainPot.candidates().get(0).seat());
        assertEquals(seat2, mainPot.candidates().get(1).seat());
    }

    @Test
    public void collectBets_whenThreeWay_onlyMainPot() {
        // Arrange
        final SeatData seat1 = new SeatData();
        seat1.setSeatIndex(0);
        seat1.setCommitted(1);
        final SeatData seat2 = new SeatData();
        seat2.setSeatIndex(1);
        seat2.setCommitted(1);
        final SeatData seat3 = new SeatData();
        seat3.setSeatIndex(2);
        seat3.setCommitted(1);
        final List<SeatData> seats = List.of(seat1, seat2, seat3);

        // Act
        final List<PotData> pots = PotsUtil.collectBets(seats);

        // Assert
        assertEquals(1, pots.size());

        final PotData mainPot = pots.get(0);
        assertEquals(3, mainPot.totalAmount());
        assertEquals(1, mainPot.amountPerCandidate());
        assertEquals(3, mainPot.candidates().size());
        assertEquals(seat1, mainPot.candidates().get(0).seat());
        assertEquals(seat2, mainPot.candidates().get(1).seat());
        assertEquals(seat3, mainPot.candidates().get(2).seat());
    }

    @Test
    public void collectBets_whenThreeWayAndOneAllIn_mainPotAndOneSidePot() {
        // Arrange
        final SeatData seat1 = new SeatData();
        seat1.setSeatIndex(0);
        seat1.setCommitted(2);
        final SeatData seat2 = new SeatData();
        seat2.setSeatIndex(1);
        seat2.setCommitted(2);
        final SeatData seat3 = new SeatData();
        seat3.setSeatIndex(2);
        seat3.setCommitted(1);
        final List<SeatData> seats = List.of(seat1, seat2, seat3);

        // Act
        final List<PotData> pots = PotsUtil.collectBets(seats);

        // Assert
        assertEquals(2, pots.size());

        final PotData mainPot = pots.get(0);
        assertEquals(3, mainPot.totalAmount());
        assertEquals(1, mainPot.amountPerCandidate());
        assertEquals(3, mainPot.candidates().size());
        assertEquals(seat3, mainPot.candidates().get(0).seat());
        assertEquals(seat1, mainPot.candidates().get(1).seat());
        assertEquals(seat2, mainPot.candidates().get(2).seat());

        final PotData sidePot = pots.get(1);
        assertEquals(2, sidePot.totalAmount());
        assertEquals(1, sidePot.amountPerCandidate());
        assertEquals(2, sidePot.candidates().size());
        assertEquals(seat1, sidePot.candidates().get(0).seat());
        assertEquals(seat2, sidePot.candidates().get(1).seat());
    }

    @Test
    public void collectBets_whenFourWayAndThreeAllIn_mainPotAndOneSidePot() {
        // Arrange
        final SeatData seat1 = new SeatData();
        seat1.setSeatIndex(0);
        seat1.setCommitted(2);
        final SeatData seat2 = new SeatData();
        seat2.setSeatIndex(1);
        seat2.setCommitted(2);
        final SeatData seat3 = new SeatData();
        seat3.setSeatIndex(2);
        seat3.setCommitted(5);
        final SeatData seat4 = new SeatData();
        seat4.setSeatIndex(3);
        seat4.setCommitted(5);
        final List<SeatData> seats = List.of(seat1, seat2, seat3, seat4);

        // Act
        final List<PotData> pots = PotsUtil.collectBets(seats);

        // Assert
        assertEquals(2, pots.size());

        final PotData mainPot = pots.get(0);
        assertEquals(8, mainPot.totalAmount());
        assertEquals(2, mainPot.amountPerCandidate());
        assertEquals(4, mainPot.candidates().size());
        assertEquals(seat1, mainPot.candidates().get(0).seat());
        assertEquals(seat2, mainPot.candidates().get(1).seat());
        assertEquals(seat3, mainPot.candidates().get(2).seat());
        assertEquals(seat4, mainPot.candidates().get(3).seat());

        final PotData sidePot = pots.get(1);
        assertEquals(6, sidePot.totalAmount());
        assertEquals(3, sidePot.amountPerCandidate());
        assertEquals(2, sidePot.candidates().size());
        assertEquals(seat3, sidePot.candidates().get(0).seat());
        assertEquals(seat4, sidePot.candidates().get(1).seat());
    }

    @Test
    public void collectBets_whenFourWayAndThreeAllIn_mainPotAndTwoSidePots() {
        // Arrange
        final SeatData seat1 = new SeatData();
        seat1.setSeatIndex(0);
        seat1.setCommitted(2);
        final SeatData seat2 = new SeatData();
        seat2.setSeatIndex(1);
        seat2.setCommitted(3);
        final SeatData seat3 = new SeatData();
        seat3.setSeatIndex(2);
        seat3.setCommitted(5);
        final SeatData seat4 = new SeatData();
        seat4.setSeatIndex(3);
        seat4.setCommitted(5);
        final List<SeatData> seats = List.of(seat1, seat2, seat3, seat4);

        // Act
        final List<PotData> pots = PotsUtil.collectBets(seats);

        // Assert
        assertEquals(3, pots.size());

        final PotData mainPot = pots.get(0);
        assertEquals(8, mainPot.totalAmount());
        assertEquals(2, mainPot.amountPerCandidate());
        assertEquals(4, mainPot.candidates().size());
        assertEquals(seat1, mainPot.candidates().get(0).seat());
        assertEquals(seat2, mainPot.candidates().get(1).seat());
        assertEquals(seat3, mainPot.candidates().get(2).seat());
        assertEquals(seat4, mainPot.candidates().get(3).seat());

        final PotData sidePot1 = pots.get(1);
        assertEquals(3, sidePot1.totalAmount());
        assertEquals(1, sidePot1.amountPerCandidate());
        assertEquals(3, sidePot1.candidates().size());
        assertEquals(seat2, sidePot1.candidates().get(0).seat());
        assertEquals(seat3, sidePot1.candidates().get(1).seat());
        assertEquals(seat4, sidePot1.candidates().get(2).seat());

        final PotData sidePot2 = pots.get(2);
        assertEquals(4, sidePot2.totalAmount());
        assertEquals(2, sidePot2.amountPerCandidate());
        assertEquals(2, sidePot2.candidates().size());
        assertEquals(seat3, sidePot2.candidates().get(0).seat());
        assertEquals(seat4, sidePot2.candidates().get(1).seat());
    }

    @Test
    public void determineWinners_noPots() throws NotationException {
        // Arrange
        final List<PotData> pots = List.of();

        // Act
        final List<CandidateData> winners = PotsUtil.determineWinners(pots, HandUtil.fromNotation("2c4d6h8sTc").getBitmask(), 0, 10);

        // Assert
        assertEquals(0, winners.size());
    }

    @Test
    public void determineWinners_mainPotButNoCandidates() throws NotationException {
        // Arrange
        final List<PotData> pots = List.of(new PotData("Pot", 1, List.of()));

        // Act
        final List<CandidateData> winners = PotsUtil.determineWinners(pots, HandUtil.fromNotation("2c4d6h8sTc").getBitmask(), 0, 10);

        // Assert
        assertEquals(0, winners.size());
    }

    @Test
    public void determineWinners_mainPotAndOneCandidate() throws NotationException {
        // Arrange
        final SeatData seat1 = new SeatData();
        seat1.setSeatIndex(0);
        seat1.setCards(HandUtil.fromNotation("AcQd").getBitmask());
        final List<CandidateData> candidates = List.of(new CandidateData("Pot", seat1, 1));
        final List<PotData> pots = List.of(new PotData("Pot", 1, candidates));

        // Act
        final List<CandidateData> winners = PotsUtil.determineWinners(pots, HandUtil.fromNotation("2c4d6h8sTc").getBitmask(), 0, 1);

        // Assert
        assertEquals(1, winners.size());
        assertEquals(seat1, winners.get(0).seat());
        assertEquals(1, winners.get(0).amount());
    }

    @Test
    public void determineWinners_singleMainPotWinner_winnerTakesAll() throws NotationException {
        // Arrange
        final SeatData seat1 = new SeatData();
        seat1.setSeatIndex(0);
        seat1.setCards(HandUtil.fromNotation("AcQd").getBitmask());
        final SeatData seat2 = new SeatData();
        seat2.setSeatIndex(1);
        seat2.setCards(HandUtil.fromNotation("AhJs").getBitmask());
        final List<CandidateData> candidates = List.of(
                new CandidateData("Pot", seat1, 1),
                new CandidateData("Pot", seat2, 1)
        );
        final List<PotData> pots = List.of(new PotData("Pot", 1, candidates));

        // Act
        final List<CandidateData> winners = PotsUtil.determineWinners(pots, HandUtil.fromNotation("2c4d6h8sTc").getBitmask(), 0, 2);

        // Assert
        assertEquals(1, winners.size());
        assertEquals(seat1, winners.get(0).seat());
        assertEquals(2, winners.get(0).amount());
    }

    @Test
    public void determineWinners_twoMainPotWinners_winnerSplitEven() throws NotationException {
        // Arrange
        final SeatData seat1 = new SeatData();
        seat1.setSeatIndex(0);
        seat1.setCards(HandUtil.fromNotation("AcAd").getBitmask());
        final SeatData seat2 = new SeatData();
        seat2.setSeatIndex(1);
        seat2.setCards(HandUtil.fromNotation("AhAs").getBitmask());
        final List<CandidateData> candidates = List.of(
                new CandidateData("Pot", seat1, 1),
                new CandidateData("Pot", seat2, 1)
        );
        final List<PotData> pots = List.of(new PotData("Pot", 1, candidates));

        // Act
        final List<CandidateData> winners = PotsUtil.determineWinners(pots, HandUtil.fromNotation("2c4d6h8sTc").getBitmask(), 0, 2);

        // Assert
        assertEquals(2, winners.size());

        assertEquals(seat1, winners.get(0).seat());
        assertEquals(1, winners.get(0).amount());

        assertEquals(seat2, winners.get(1).seat());
        assertEquals(1, winners.get(1).amount());
    }

    @Test
    public void determineWinners_mainPotAndSidePot() throws NotationException {
        // Arrange
        final SeatData seat1 = new SeatData();
        seat1.setSeatIndex(0);
        seat1.setCards(HandUtil.fromNotation("AcAd").getBitmask());
        final SeatData seat2 = new SeatData();
        seat2.setSeatIndex(1);
        seat2.setCards(HandUtil.fromNotation("KhKs").getBitmask());
        final SeatData seat3 = new SeatData();
        seat3.setSeatIndex(2);
        seat3.setCards(HandUtil.fromNotation("QhQs").getBitmask());
        final List<PotData> pots = List.of(
                new PotData("Pot", 1,
                        List.of(
                                new CandidateData("Pot", seat1, 1),
                                new CandidateData("Pot", seat2, 1),
                                new CandidateData("Pot", seat3, 1)
                        )
                ),
                new PotData("Pot", 4,
                        List.of(
                                new CandidateData("Pot", seat2, 4),
                                new CandidateData("Pot", seat3, 4)
                        )
                )
        );

        // Act
        final List<CandidateData> winners = PotsUtil.determineWinners(pots, HandUtil.fromNotation("2c4d6h8sTc").getBitmask(), 0, 3);

        // Assert
        assertEquals(2, winners.size());

        assertEquals(seat1, winners.get(0).seat());
        assertEquals(3, winners.get(0).amount());

        assertEquals(seat2, winners.get(1).seat());
        assertEquals(8, winners.get(1).amount());
    }

    @Test
    public void determineWinners_complexSituation() throws NotationException {
        // Arrange
        final SeatData seat1 = new SeatData();
        seat1.setSeatIndex(0);
        seat1.setCards(HandUtil.fromNotation("AcAd").getBitmask());
        final SeatData seat2 = new SeatData();
        seat2.setSeatIndex(1);
        seat2.setCards(HandUtil.fromNotation("Td9d").getBitmask());
        final SeatData seat3 = new SeatData();
        seat3.setSeatIndex(2);
        seat3.setCards(HandUtil.fromNotation("Th9h").getBitmask());
        final SeatData seat4 = new SeatData();
        seat4.setSeatIndex(3);
        seat4.setCards(HandUtil.fromNotation("Ts9s").getBitmask());
        final SeatData seat5 = new SeatData();
        seat5.setSeatIndex(4);
        seat5.setCards(HandUtil.fromNotation("9c9d").getBitmask());
        final SeatData seat6 = new SeatData();
        seat6.setSeatIndex(5);
        seat6.setCards(HandUtil.fromNotation("7c7d").getBitmask());
        final List<PotData> pots = List.of(
                new PotData("Pot", 1,
                        List.of(
                                new CandidateData("Pot", seat1, 1),
                                new CandidateData("Pot", seat2, 1),
                                new CandidateData("Pot", seat3, 1),
                                new CandidateData("Pot", seat4, 1),
                                new CandidateData("Pot", seat5, 1),
                                new CandidateData("Pot", seat6, 1)
                        )
                ),
                new PotData("Pot", 4,
                        List.of(
                                new CandidateData("Pot", seat2, 4),
                                new CandidateData("Pot", seat3, 4),
                                new CandidateData("Pot", seat4, 4),
                                new CandidateData("Pot", seat5, 4),
                                new CandidateData("Pot", seat6, 4)
                        )
                ),
                new PotData("Pot", 6,
                        List.of(
                                new CandidateData("Pot", seat5, 6),
                                new CandidateData("Pot", seat6, 6)
                        )
                )
        );

        // Act
        final List<CandidateData> winners = PotsUtil.determineWinners(pots, HandUtil.fromNotation("2c4d6h8sTc").getBitmask(), 0, 6);

        // Assert
        assertEquals(5, winners.size());

        assertEquals(seat1, winners.get(0).seat());
        assertEquals(6, winners.get(0).amount());

        assertEquals(seat2, winners.get(1).seat());
        assertEquals(7, winners.get(1).amount());

        assertEquals(seat3, winners.get(2).seat());
        assertEquals(7, winners.get(2).amount());

        assertEquals(seat4, winners.get(3).seat());
        assertEquals(6, winners.get(3).amount());

        assertEquals(seat5, winners.get(4).seat());
        assertEquals(12, winners.get(4).amount());
    }

    @Test
    public void determineWinners_afterSeriesOfCollects() throws NotationException {
        // Arrange
        final SeatData seat1 = new SeatData();
        seat1.setPlayer(new RandomPlayer(1L, "Alice"));
        seat1.setSeatIndex(0);
        seat1.setCommitted(0);
        seat1.setCards(HandUtil.fromNotation("AcAd").getBitmask());
        final SeatData seat2 = new SeatData();
        seat2.setPlayer(new RandomPlayer(2L, "Bob"));
        seat2.setSeatIndex(1);
        seat2.setCommitted(0);
        seat2.setCards(HandUtil.fromNotation("Td9d").getBitmask());
        final SeatData seat3 = new SeatData();
        seat3.setPlayer(new RandomPlayer(3L, "Charlie"));
        seat3.setSeatIndex(2);
        seat3.setCommitted(0);
        seat3.setCards(HandUtil.fromNotation("Th9h").getBitmask());
        final SeatData seat4 = new SeatData();
        seat4.setPlayer(new RandomPlayer(4L, "David"));
        seat4.setSeatIndex(3);
        seat4.setCommitted(0);
        seat4.setCards(HandUtil.fromNotation("4h5h").getBitmask());

        final List<SeatData> seats = List.of(seat1, seat2, seat3, seat4);

        // Betting round
        seat2.setCommitted(10); // SB: Post small blind
        seat3.setCommitted(20); // BB: Post big blind
        seat4.setCommitted(20); // CO: Limp
        seat1.setCommitted(100); // BU: Raise
        seat2.setCommitted(seat2.getCommitted() + 90); // SB: Call
        seat3.setCommitted(seat3.getCommitted() + 80); // BB: Call
        seat4.setCommitted(seat4.getCommitted() + 80); // CO: Call

        // Collect
        final List<PotData> pots = new ArrayList<>(PotsUtil.collectBets(seats));
        assertEquals(0, seat1.getCommitted());
        assertEquals(0, seat2.getCommitted());
        assertEquals(0, seat3.getCommitted());
        assertEquals(0, seat4.getCommitted());

        assertEquals(1, pots.size());
        assertEquals(100, pots.get(0).amountPerCandidate());
        assertEquals(400, pots.get(0).totalAmount());

        // Betting round
        seat2.setCommitted(250); // SB: Bet (all-in)
        seat3.setCommitted(250); // BB: Call
        seat4.setCommitted(850); // CO: Raise
        seat1.setCommitted(500); // BU: Call (all-in)
        seat3.setCommitted(seat3.getCommitted() + 400); // BB: Call (all-in)

        // Collect
        pots.addAll(PotsUtil.collectBets(seats));
        assertEquals(0, seat1.getCommitted());
        assertEquals(0, seat2.getCommitted());
        assertEquals(0, seat3.getCommitted());
        assertEquals(0, seat4.getCommitted());

        assertEquals(4, pots.size());
        assertEquals(100, pots.get(0).amountPerCandidate());
        assertEquals(400, pots.get(0).totalAmount());
        assertEquals(250, pots.get(1).amountPerCandidate());
        assertEquals(1000, pots.get(1).totalAmount());
        assertEquals(250, pots.get(2).amountPerCandidate());
        assertEquals(750, pots.get(2).totalAmount());
        assertEquals(150, pots.get(3).amountPerCandidate());
        assertEquals(300, pots.get(3).totalAmount());

        // Determine winnings
        final List<CandidateData> winners = PotsUtil.determineWinners(pots, HandUtil.fromNotation("2c4d6h8sTc").getBitmask(), 0, 4);

        assertEquals(3, winners.size());

        assertEquals(1400, winners.get(0).amount());
        assertEquals(seat1, winners.get(0).seat());
        assertEquals(750, winners.get(1).amount());
        assertEquals(seat1, winners.get(1).seat());
        assertEquals(300, winners.get(2).amount());
        assertEquals(seat3, winners.get(2).seat());

        // Deliver winnings
        for (CandidateData candidate : winners) {
            logger.info("Seat #{} won {}", candidate.seat().getSeatIndex(), candidate.amount());
        }
    }

    @Test
    public void equality() {
        final SeatData seat = new SeatData();
        final CandidateData candidate1 = new CandidateData("Pot", seat, 100);
        final CandidateData candidate2 = new CandidateData("Pot", seat, 100);
        final CandidateData candidate3 = new CandidateData("Pot", seat, 200);
        final CandidateData candidate4 = new CandidateData("Pot", seat, 200);
        final List<CandidateData> candidates1 = List.of(candidate1, candidate2);
        final List<CandidateData> candidates2 = List.of(candidate3, candidate4);
        final PotData pot1 = new PotData("Pot", 200, candidates1);
        final PotData pot2 = new PotData("Pot", 200, candidates2);
        assertTrue(PotsUtil.hasSameCandidates(pot1, pot2));
    }

    @Test
    public void collapsePots_whenCandidateSeatsAreNotTheSameInBothPots_doNotCollapsePot() {
        // Arrange
        final SeatData seat1 = new SeatData();
        final SeatData seat2 = new SeatData();
        final SeatData seat3 = new SeatData();

        final List<PotData> pots = List.of(
                new PotData("Pot", 100, List.of(
                        new CandidateData("Pot", seat1, 100),
                        new CandidateData("Pot", seat2, 100)
                )),
                new PotData("Pot", 250, List.of(
                        new CandidateData("Pot", seat1, 250),
                        new CandidateData("Pot", seat2, 250),
                        new CandidateData("Pot", seat3, 250)
                ))
        );

        // Act
        final List<PotData> collapsed = PotsUtil.collapsePots(pots);

        // Assert
        assertEquals(2, collapsed.size());
        assertEquals(100, collapsed.get(0).amountPerCandidate());
        assertEquals(200, collapsed.get(0).totalAmount());
        assertEquals(250, collapsed.get(1).amountPerCandidate());
        assertEquals(750, collapsed.get(1).totalAmount());
    }

    @Test
    public void collapsePots_whenCandidateSeatsAreTheSameInBothPots_collapsePot() {
        // Arrange
        final SeatData seat1 = new SeatData();
        final SeatData seat2 = new SeatData();

        final List<PotData> pots = List.of(
                new PotData("Pot", 100, List.of(
                        new CandidateData("Pot", seat1, 100),
                        new CandidateData("Pot", seat2, 100)
                )),
                new PotData("Pot", 250, List.of(
                        new CandidateData("Pot", seat1, 250),
                        new CandidateData("Pot", seat2, 250)
                ))
        );

        // Act
        final List<PotData> collapsed = PotsUtil.collapsePots(pots);

        // Assert
        assertEquals(1, collapsed.size());
        assertEquals(350, collapsed.get(0).amountPerCandidate());
        assertEquals(700, collapsed.get(0).totalAmount());
    }

    @Test
    public void determineWinners_forCollapsedPot() throws NotationException {
        // Arrange
        final SeatData seat1 = new SeatData();
        seat1.setSeatIndex(0);
        seat1.setCards(HandUtil.fromNotation("AcAd").getBitmask());
        final SeatData seat2 = new SeatData();
        seat2.setSeatIndex(1);
        seat2.setCards(HandUtil.fromNotation("AhAs").getBitmask());
        final List<CandidateData> candidates1 = List.of(
                new CandidateData("Pot", seat1, 1),
                new CandidateData("Pot", seat2, 1)
        );
        final List<CandidateData> candidates2 = List.of(
                new CandidateData("Pot", seat1, 2),
                new CandidateData("Pot", seat2, 2)
        );
        final List<PotData> pots = List.of(
                new PotData("Pot", 1, candidates1),
                new PotData("Pot", 2, candidates2)
        );

        // Act
        final List<CandidateData> winners = PotsUtil.determineWinners(pots, HandUtil.fromNotation("2c4d6h8sTc").getBitmask(), 0, 2);

        // Assert
        assertEquals(2, winners.size());

        assertEquals(seat1, winners.get(0).seat());
        assertEquals(3, winners.get(0).amount());

        assertEquals(seat2, winners.get(1).seat());
        assertEquals(3, winners.get(1).amount());
    }
}
