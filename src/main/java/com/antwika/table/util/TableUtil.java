package com.antwika.table.util;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.handhistory.line.*;
import com.antwika.table.data.*;
import com.antwika.table.event.*;
import com.antwika.table.event.player.PlayerLeaveRequest;
import com.antwika.table.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TableUtil {
    private static final Logger logger = LoggerFactory.getLogger(TableUtil.class);

    public static String toNotation(long cards) throws NotationException {
        final String cardsNotation = HandUtil.toNotation(cards);

        final List<String> cn = new ArrayList<>();
        for (int i = 0; i < cardsNotation.length(); i += 2) {
            final String cardNotation = cardsNotation.substring(i, i+2);
            cn.add(cardNotation);
        }

        return String.join(" ", cn);
    }

    public static boolean seat(TableData tableData, Player player, int seatIndex, int buyIn) {
        final SeatData seat = tableData.getSeats().get(seatIndex);

        if (seat.getPlayer() == player) {
            logger.debug("{} is already seated at #{}", player, seatIndex);
            return false;
        }

        if (seat.getPlayer() != null) {
            logger.warn("{} can't join game at seat #{}, there's already another player there", player, seatIndex);
            return false;
        }

        resetSeat(seat);
        seat.setPlayer(player);
        seat.setStack(buyIn);

        return true;
    }

    public static void resetSeat(SeatData seat) {
        seat.setCards(0L);
        seat.setCommitted(0);
        seat.setTotalCommitted(0);
        seat.setPostedSmallBlindLastRound(false);
        seat.setPostedBigBlindLastRound(false);
        seat.setHasActed(false);
        seat.setHasFolded(false);
    }

    public static void resetAllSeats(TableData tableData) {
        tableData.getSeats().forEach(TableUtil::resetSeat);
    }

    public static int countAllStacks(TableData tableData) {
        return tableData.getSeats().stream().mapToInt(SeatData::getStack).sum();
    }

    public static int countTotalPot(TableData tableData) {
        return tableData.getPots().stream().mapToInt(PotData::getTotalAmount).sum();
    }

    public static int countChipsInPlay(TableData tableData) {
        final var totalStacks = TableUtil.countAllStacks(tableData);
        final var totalCommitted = TableUtil.countTotalCommitted(tableData);
        final var totalPot = TableUtil.countTotalPot(tableData);
        return totalStacks + totalCommitted + totalPot;
    }

    public static int countTotalCommitted(TableData tableData) {
        return tableData.getSeats().stream().mapToInt(SeatData::getCommitted).sum();
    }

    public static int countTotalPotAndCommitted(TableData tableData) {
        return countTotalPot(tableData) + countTotalCommitted(tableData);
    }

    public static int countPlayersRemainingInHand(TableData tableData) {
        return tableData.getSeats().stream()
                .filter(seat -> seat.getPlayer() != null)
                .filter(seat -> !seat.isHasFolded())
                .toList()
                .size();
    }

    public static SeatData getSeat(TableData tableData, Player player) {
        return tableData.getSeats().stream()
                .filter(i -> i.getPlayer() == player)
                .findFirst()
                .orElse(null);
    }

    public static String getStreetName(TableData tableData) {
        return switch (Long.bitCount(tableData.getCards())) {
            case 3 -> "FLOP";
            case 4 -> "TURN";
            case 5 -> "RIVER";
            default -> "PREFLOP";
        };
    }

    public static IEvent unseat(TableData tableData, SeatData seat) {
        final Player player = seat.getPlayer();
        if (player != null) {
            seat.setPlayer(null);
            seat.setStack(0);
            return new PlayerLeaveRequest(tableData, seat, player);
        }
        return null;
    }

    public static List<IEvent> unseat(TableData tableData, List<SeatData> seats) {
        final List<IEvent> additionalEvents = new ArrayList<>();

        for (SeatData seat : seats) {
            final IEvent additionalEvent = unseat(tableData, seat);
            if (additionalEvent != null) {
                additionalEvents.add(additionalEvent);
            }
        }

        return additionalEvents;
    }

    public static List<SeatData> findAllBustedSeats(TableData tableData) {
        return tableData.getSeats().stream()
                .filter(seat -> seat.getStack() == 0)
                .toList();
    }

    public static int findHighestCommit(TableData tableData) {
        return tableData.getSeats().stream()
                .mapToInt(SeatData::getCommitted)
                .max()
                .orElse(0);
    }

    public static SeatData findNextSeatToAct(TableData tableData, int fromSeat, int skips, boolean mustAct) {
        SeatData nextSeat = null;
        int skipped = 0;

        final List<SeatData> seats = tableData.getSeats();

        final int numberOfPlayersWithNonFoldedAndNonZeroStack = TableUtil.numberOfPlayersWithNonFoldedAndNonZeroStack(tableData);
        final int countPlayersRemainingInHand = TableUtil.countPlayersRemainingInHand(tableData);

        if (numberOfPlayersWithNonFoldedAndNonZeroStack < 2 && countPlayersRemainingInHand < 2) {
            return null;
        }

        for (int i = 0; i < seats.size(); i += 1) {
            final int seatIndex = (fromSeat + i + 1) % seats.size();
            final SeatData seat = seats.get(seatIndex);

            if (seat.getPlayer() == null) continue;

            if (mustAct) {
                if (!seat.isHasActed() && !seat.isHasFolded() && seat.getStack() > 0) {
                    nextSeat = seat;
                } else if (seat.getCommitted() < tableData.getTotalBet() && seat.getStack() > 0 && !seat.isHasFolded()) {
                    nextSeat = seat;
                }
            } else {
                nextSeat = seat;
            }

            if (nextSeat != null) {
                if (skipped < skips) {
                    skipped += 1;
                    continue;
                }
                break;
            }
        }

        return nextSeat;
    }

    public static boolean hasAllPlayersActed(TableData tableData) {
        int highestCommit = TableUtil.findHighestCommit(tableData);

        final int countPlayersRemainingInHand = TableUtil.countPlayersRemainingInHand(tableData);

        final int notActedYetCount = tableData.getSeats().stream()
                .filter(seat -> seat.getStack() > 0)
                .filter(seat -> !seat.isHasFolded())
                .filter(seat -> !seat.isHasActed())
                .toList()
                .size();

        if (countPlayersRemainingInHand == 1) {
            return true;
        }

        if (notActedYetCount > 0) {
            return false;
        }

        final int notMatchedBetCount = tableData.getSeats().stream()
                .filter(seat -> seat.getStack() > 0)
                .filter(seat -> !seat.isHasFolded())
                .filter(seat -> seat.getCommitted() != highestCommit)
                .toList()
                .size();

        return notMatchedBetCount == 0;
    }

    public static int getNumberOfPlayersLeftToAct(TableData tableData) {
        final List<SeatData> playerSeats = tableData.getSeats().stream()
                .filter(i -> i.getPlayer() != null)
                .filter(i -> i.getStack() > 0)
                .filter(i -> i.getCards() > 0L)
                .filter(i -> !i.isHasFolded())
                .toList();
        return playerSeats.size();
    }

    public static void prepareBettingRound(TableData tableData) {
        if (Long.bitCount(tableData.getCards()) != 0) {
            for (SeatData seat : tableData.getSeats()) {
                seat.setHasActed(false);
            }

            final var nextSeatToAct = findNextSeatToAct(tableData, tableData.getButtonAt(), 0, true);

            if (nextSeatToAct == null) {
                logger.error("Unexpected that the next seat to act to be null");
                return;
            }

            tableData.setActionAt(nextSeatToAct.getSeatIndex());
        }
    }

    public static boolean anyPlayerHasCards(TableData tableData) {
        return !tableData.getSeats().stream()
                .filter(seat -> seat.getCards() != null)
                .filter(seat -> seat.getCards() != 0L)
                .toList()
                .isEmpty();
    }

    public static boolean anyPlayerHasCommittedChips(TableData tableData) {
        return !tableData.getSeats().stream()
                .filter(seat -> seat.getCommitted() > 0)
                .toList()
                .isEmpty();
    }

    public static int numberOfPlayersWithNonZeroStack(TableData tableData) {
        return tableData.getSeats().stream()
                .filter(seat -> seat.getPlayer() != null)
                .filter(seat -> seat.getStack() != 0)
                .toList()
                .size();
    }

    public static int numberOfPlayersWithNonFoldedAndNonZeroStack(TableData tableData) {
        return tableData.getSeats().stream()
                .filter(seat -> seat.getPlayer() != null)
                .filter(seat -> !seat.isHasFolded())
                .filter(seat -> seat.getStack() != 0)
                .toList()
                .size();
    }

    public static boolean canStartHand(TableData tableData) {
        if (!tableData.getGameStage().equals(TableData.GameStage.NONE)) return false;

        if (anyPlayerHasCards(tableData)) return false;

        if (anyPlayerHasCommittedChips(tableData)) return false;

        if (numberOfPlayersWithNonZeroStack(tableData) < 2) return false;

        return true;
    }

    public static void forcePostBlind(TableData tableData, int blindIndex, int blindAmount) {
        final SeatData nextSeatToAct = TableUtil.findNextSeatToAct(tableData, tableData.getButtonAt(), blindIndex, true);

        if (nextSeatToAct == null) {
            logger.error("Unexpected that the nextSeatToAct to act to be null");
            return;
        }

        final Player player = nextSeatToAct.getPlayer();

        int commitAmount = Math.min(nextSeatToAct.getStack(), blindAmount);
        TableUtil.commit(nextSeatToAct, commitAmount);

        final SeatData nextNextSeatToAct = TableUtil.findNextSeatToAct(tableData, nextSeatToAct.getSeatIndex(), 0, true);

        if (nextNextSeatToAct == null) {
            logger.error("Unexpected that the nextNextSeatToAct to act to be null");
            return;
        }

        tableData.setActionAt(nextNextSeatToAct.getSeatIndex());

        final var playerName = player.getPlayerData().getPlayerName();
        final var blindName = getBlindName(blindIndex);

        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: posts %s %d", playerName, blindName, commitAmount));
        if (nextSeatToAct.getStack() == 0) {
            sb.append(" and is all-in");
        }
        logger.info(sb.toString());

        tableData.getHistory().add(new BlindLine(
                playerName,
                blindName,
                commitAmount
        ));
    }

    private static String getBlindName(int blindIndex) {
        return switch (blindIndex) {
            case 0 -> "small blind";
            case 1 -> "big blind";
            default -> "blind";
        };
    }

    public static void forcePostBlinds(TableData tableData, List<Integer> blinds) {
        for (int i = 0; i < blinds.size(); i += 1) {
            forcePostBlind(tableData, i, blinds.get(i));
        }
    }

    public static void commit(SeatData seat, int amount) {
        if (seat.getStack() < amount) throw new RuntimeException("Commit amount is greater than the available stack");
        if (amount <= 0) throw new RuntimeException("Commit must be greater than zero");
        seat.setStack(seat.getStack() - amount);
        seat.setCommitted(seat.getCommitted() + amount);
    }

    public static void dealCommunityCards(TableData tableData, int count) {
        long prev = tableData.getCards();
        long add = 0L;
        final DeckData deckData = tableData.getDeckData();
        for (int i = 0; i < count; i += 1) {
            Long card = DeckUtil.draw(deckData);
            if (card == null) throw new RuntimeException("The card drawn from the deck is null");
            add |= card;
        }

        tableData.setCards(tableData.getCards() | add);

        try {
            final var streetName = TableUtil.getStreetName(tableData);

            final StringBuilder sb = new StringBuilder();
            sb.append(String.format("*** %s ***", streetName));

            if (prev != 0L) {
                sb.append(String.format(" [%s]", TableUtil.toNotation(prev)));
            }
            if (add != 0L) {
                sb.append(String.format(" [%s]", TableUtil.toNotation(add)));
            }
            logger.info(sb.toString());
            logger.info("Total pot: {}", TableUtil.countTotalPot(tableData));

            switch (streetName) {
                case "FLOP" -> {
                    final var notation = HandUtil.toNotation(tableData.getCards());
                    final var card1 = HandUtil.fromNotation(notation.substring(0, 2)).getBitmask();
                    final var card2 = HandUtil.fromNotation(notation.substring(2, 4)).getBitmask();
                    final var card3 = HandUtil.fromNotation(notation.substring(4, 6)).getBitmask();

                    tableData.getHistory().add(new FlopHeaderLine(
                            card1,
                            card2,
                            card3
                    ));
                }
                case "TURN" -> {
                    final var notation = HandUtil.toNotation(tableData.getCards());
                    final var card1 = HandUtil.fromNotation(notation.substring(0, 2)).getBitmask();
                    final var card2 = HandUtil.fromNotation(notation.substring(2, 4)).getBitmask();
                    final var card3 = HandUtil.fromNotation(notation.substring(4, 6)).getBitmask();
                    final var card4 = HandUtil.fromNotation(notation.substring(6, 8)).getBitmask();

                    tableData.getHistory().add(new TurnHeaderLine(
                            card1,
                            card2,
                            card3,
                            card4
                    ));
                }
                case "RIVER" -> {
                    final var notation = HandUtil.toNotation(tableData.getCards());
                    final var card1 = HandUtil.fromNotation(notation.substring(0, 2)).getBitmask();
                    final var card2 = HandUtil.fromNotation(notation.substring(2, 4)).getBitmask();
                    final var card3 = HandUtil.fromNotation(notation.substring(4, 6)).getBitmask();
                    final var card4 = HandUtil.fromNotation(notation.substring(6, 8)).getBitmask();
                    final var card5 = HandUtil.fromNotation(notation.substring(8, 10)).getBitmask();

                    tableData.getHistory().add(new RiverHeaderLine(
                            card1,
                            card2,
                            card3,
                            card4,
                            card5
                    ));
                }
            }
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void prepareHand(TableData tableData) {
        tableData.getPots().clear();
        tableData.setHandId(tableData.getHandId() + 1L);
        tableData.setCards(0L);
        tableData.setDelivered(0);
        tableData.setTotalBet(tableData.getBigBlind());
        tableData.setLastRaise(tableData.getBigBlind());
        tableData.setChipsInPlay(TableUtil.countAllStacks(tableData));
    }

    public static void collect(TableData tableData) {
        final List<SeatData> seats = tableData.getSeats();
        tableData.getPots().addAll(PotsUtil.collectBets(seats));

        tableData.setLastRaise(0);
        tableData.setTotalBet(0);

        int totalStacks = TableUtil.countAllStacks(tableData);
        int totalPot = TableUtil.countTotalPot(tableData);
        if (totalStacks + totalPot != tableData.getChipsInPlay()) {
            throw new RuntimeException("Invalid amount of chips");
        }
    }

    public static void showdown(TableData tableData) throws NotationException {
        final List<PotData> pots = tableData.getPots();
        final Long cards = tableData.getCards();
        final int buttonAt = tableData.getButtonAt();
        final List<SeatData> seats = tableData.getSeats();

        logger.debug("Showdown");
        final List<PotData> collapsed = PotsUtil.collapsePots(pots);
        final List<CandidateData> winners = PotsUtil.determineWinners(collapsed, cards, buttonAt, seats.size());

        for (CandidateData winner : winners) {
            final SeatData seat = winner.getSeat();
            final Player player = seat.getPlayer();
            int amount = winner.getAmount();
            tableData.setDelivered(tableData.getDelivered() + amount);
            winner.getSeat().setStack(seat.getStack() + amount);

            logger.info("{} collected {} from {}",
                    player.getPlayerData().getPlayerName(),
                    amount,
                    winner.getPotName());

            tableData.getHistory().add(new CollectedPotLine(
                    player.getPlayerData().getPlayerName(),
                    amount,
                    winner.getPotName()
            ));
        }
        pots.clear();

        int totalStacks = seats.stream().filter(i -> i.getPlayer() != null).mapToInt(SeatData::getStack).sum();
        int totalPot = TableUtil.countTotalPot(tableData);
        if (totalStacks + totalPot != tableData.getChipsInPlay()) {
            throw new RuntimeException("Invalid amount of chips");
        }

        logger.info("*** SUMMARY ***");
        logger.info("Total pot {} | Rake {}", tableData.getDelivered(), 0);
        logger.info("Board [{}]", TableUtil.toNotation(tableData.getCards()));

        int chipsInPlay = 0;
        for (SeatData seat : tableData.getSeats()) {
            if (seat.getPlayer() == null) continue;

            chipsInPlay += seat.getStack();

            final Player player = seat.getPlayer();

            logger.info("Seat {}: {} stack {}",
                    seat.getSeatIndex(),
                    player.getPlayerData().getPlayerName(),
                    seat.getStack());
        }
        logger.info("Total chips in play {}", chipsInPlay);
    }

    public static void deliverWinnings(TableData tableData) throws NotationException {
        final List<PotData> pots = tableData.getPots();
        final Long cards = tableData.getCards();
        final int buttonAt = tableData.getButtonAt();
        final List<SeatData> seats = tableData.getSeats();

        logger.debug("Showdown");
        final List<PotData> collapsed = PotsUtil.collapsePots(pots);
        final List<CandidateData> winners = PotsUtil.determineWinners(collapsed, cards, buttonAt, seats.size());

        for (CandidateData winner : winners) {
            final SeatData seat = winner.getSeat();
            final Player player = seat.getPlayer();
            int amount = winner.getAmount();
            tableData.setDelivered(tableData.getDelivered() + amount);
            winner.getSeat().setStack(seat.getStack() + amount);

            logger.info("{} collected {} from {}",
                    player.getPlayerData().getPlayerName(),
                    amount,
                    winner.getPotName());

            tableData.getHistory().add(new CollectedPotLine(
                    player.getPlayerData().getPlayerName(),
                    amount,
                    winner.getPotName()
            ));
        }
        pots.clear();

        int totalStacks = seats.stream().filter(i -> i.getPlayer() != null).mapToInt(SeatData::getStack).sum();
        int totalPot = TableUtil.countTotalPot(tableData);
        if (totalStacks + totalPot != tableData.getChipsInPlay()) {
            throw new RuntimeException("Invalid amount of chips");
        }

        logger.info("*** SUMMARY ***");

        tableData.getHistory().add(new SummaryHeaderLine());

        logger.info("Total pot {} | Rake {}", tableData.getDelivered(), 0);

        tableData.getHistory().add(new TotalPotLine(tableData.getDelivered()));

        logger.info("Board [{}]", TableUtil.toNotation(tableData.getCards()));

        final var boardNotation = TableUtil.toNotation(tableData.getCards());
        final var boardCardCount = Long.bitCount(tableData.getCards());

        var card1 = 0L;
        var card2 = 0L;
        var card3 = 0L;
        if (boardCardCount >= 3) {
            card1 = HandUtil.fromNotation(boardNotation.substring(0, 2)).getBitmask();
            card2 = HandUtil.fromNotation(boardNotation.substring(3, 5)).getBitmask();
            card3 = HandUtil.fromNotation(boardNotation.substring(6, 8)).getBitmask();
        }

        var card4 = 0L;
        if (boardCardCount >= 4) {
            card4 = HandUtil.fromNotation(boardNotation.substring(9, 11)).getBitmask();
        }

        var card5 = 0L;
        if (boardCardCount >= 5) {
            card5 = HandUtil.fromNotation(boardNotation.substring(12, 14)).getBitmask();
        }

        tableData.getHistory().add(new SummaryBoardInfoLine(
                card1,
                card2,
                card3,
                card4,
                card5
        ));

        int chipsInPlay = 0;
        for (SeatData seat : tableData.getSeats()) {
            if (seat.getPlayer() == null) continue;

            chipsInPlay += seat.getStack();

            final Player player = seat.getPlayer();

            logger.info("Seat {}: {} stack {}",
                    seat.getSeatIndex(),
                    player.getPlayerData().getPlayerName(),
                    seat.getStack());

            tableData.getHistory().add(new SummaryPlayerInfoLine(
                    seat.getSeatIndex(),
                    player.getPlayerData().getPlayerName(),
                    seat.getStack()
            ));
        }

        logger.info("Total chips in play {}", chipsInPlay);

        tableData.getHistory().add(new SummaryTotalChipsLine(
                chipsInPlay
        ));
    }

    public static int calcBetSize(TableData tableData, Player player, float betSizePercent) {
        final SeatData seat = TableUtil.getSeat(tableData, player);
        int lastRaise = tableData.getLastRaise();
        int totalPot = TableUtil.countTotalPotAndCommitted(tableData);
        int deadMoney = totalPot - lastRaise;
        int desiredBet = (int) ((lastRaise * 3 + deadMoney) * betSizePercent);
        int minimumBet = Math.min(seat.getStack(), tableData.getBigBlind());
        int bet = Math.max(desiredBet, minimumBet);
        return Math.min(seat.getStack(), bet);
    }

    public static Optional<SeatData> findSeatByPlayerName(TableData tableData, String playerName) {
        return tableData.getSeats().stream()
                .filter(seat -> seat.getPlayer().getPlayerData().getPlayerName().equals(playerName))
                .findFirst();
    }
}
