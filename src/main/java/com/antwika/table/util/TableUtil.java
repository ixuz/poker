package com.antwika.table.util;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.BitmaskUtil;
import com.antwika.common.util.HandUtil;
import com.antwika.table.data.*;
import com.antwika.table.event.*;
import com.antwika.table.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    public static boolean seat(TableData tableData, Player player, int buyIn) {
        final SeatData seat = findFirstAvailableSeat(tableData);
        if (seat == null) return false;

        return seat(tableData, player, seat.getSeatIndex(), buyIn);
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

    public static void unseatAll(TableData tableData) {
        tableData.getSeats().stream()
                .filter(seat -> seat.getPlayer() != null)
                .forEach(seat -> unseat(tableData, seat));
    }

    public static SeatData findFirstAvailableSeat(TableData tableData) {
        return tableData.getSeats().stream()
                .filter(seat -> seat.getPlayer() == null)
                .findFirst()
                .orElse(null);
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

        for (int i = 0; i < seats.size(); i += 1) {
            final int seatIndex = (fromSeat + i + 1) % seats.size();
            final SeatData seat = seats.get(seatIndex);

            if (seat.getPlayer() == null) continue;

            if (mustAct) {
                if (!seat.isHasActed() && !seat.isHasFolded()) {
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

        final int notActedYetCount = tableData.getSeats().stream()
                .filter(seat -> seat.getStack() > 0)
                .filter(seat -> !seat.isHasFolded())
                .filter(seat -> !seat.isHasActed())
                .toList()
                .size();

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
            tableData.setActionAt(findNextSeatToAct(tableData, tableData.getButtonAt(), 0, true).getSeatIndex());
        }
    }

    public static boolean startGame(TableData tableData) {
        final List<SeatData> seats = tableData.getSeats();
        final int playerWithStackCount = seats.stream()
                .filter(seat -> seat.getPlayer() != null)
                .filter(seat -> seat.getStack() > 0)
                .toList()
                .size();
        final boolean enoughPlayersToStartHand = playerWithStackCount > 1;

        if (enoughPlayersToStartHand) {
            drawButtonSeatIndex(tableData);
            tableData.setGameStage(TableData.GameStage.NONE);
            return true;
        }

        return false;
    }

    public static void stopGame(TableData tableData) {
        unseatAll(tableData);
    }

    public static void drawButtonSeatIndex(TableData tableData) {
        logger.debug("Drawing cards to determine button position...");
        final DeckData deckData = tableData.getDeckData();
        DeckUtil.resetAndShuffle(deckData);

        tableData.getSeats().stream()
                .filter(seat -> seat.getPlayer() != null)
                .forEach(seat -> seat.setCards(DeckUtil.draw(deckData)));

        final List<SeatData> sortedByCard = tableData.getSeats().stream()
                .filter(i -> i.getPlayer() != null)
                .sorted(Comparator.comparingInt(e -> BitmaskUtil.CARD_TO_SUIT_INDEX.get(e.getCards())))
                .sorted(Comparator.comparingInt(e -> BitmaskUtil.CARD_TO_RANK_INDEX.get(e.getCards())))
                .toList();

        final SeatData winner = sortedByCard.get(sortedByCard.size() - 1);

        if (winner == null) {
            throw new RuntimeException("Failed to draw card for the button position!");
        }

        tableData.setButtonAt(winner.getSeatIndex());

        logger.debug("Starting button position at seat #{}", winner.getSeatIndex() + 1);

        resetAllSeats(tableData);
    }

    public static boolean canStartHand(TableData tableData) {
        if (!tableData.getGameStage().equals(TableData.GameStage.NONE)) return false;

        boolean anyPlayerHasCards = !tableData.getSeats().stream()
                .filter(seat -> seat.getCards() != null)
                .filter(seat -> seat.getCards() != 0L)
                .toList()
                .isEmpty();

        if (anyPlayerHasCards) return false;

        boolean anyPlayerHasCommittedChips = !tableData.getSeats().stream()
                .filter(seat -> seat.getCommitted() > 0)
                .toList()
                .isEmpty();

        if (anyPlayerHasCommittedChips) return false;

        return tableData.getSeats().stream()
                .filter(seat -> seat.getPlayer() != null)
                .filter(seat -> seat.getStack() != 0)
                .toList()
                .size() > 1;
    }

    public static void forcePostBlind(TableData tableData, int blindIndex, int blindAmount) {

        final SeatData seat = TableUtil.findNextSeatToAct(tableData, tableData.getButtonAt(), blindIndex, true);
        final Player player = seat.getPlayer();

        int commitAmount = Math.min(seat.getStack(), blindAmount);
        TableUtil.commit(seat, commitAmount);

        tableData.setActionAt(TableUtil.findNextSeatToAct(tableData, seat.getSeatIndex(), 0, true).getSeatIndex());

        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: posts %s %d", player.getPlayerData().getPlayerName(), getBlindName(blindIndex), commitAmount));
        if (seat.getStack() == 0) {
            sb.append(" and is all-in");
        }
        logger.info(sb.toString());
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
            final StringBuilder sb = new StringBuilder();
            sb.append(String.format("*** %s ***", TableUtil.getStreetName(tableData)));
            if (prev != 0L) {
                sb.append(String.format(" [%s]", TableUtil.toNotation(prev)));
            }
            if (add != 0L) {
                sb.append(String.format(" [%s]", TableUtil.toNotation(add)));
            }
            logger.info(sb.toString());
            logger.info("Total pot: {}", TableUtil.countTotalPot(tableData));
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

    public static boolean isHandOngoing(TableData tableData) {
        return !tableData.getGameStage().equals(TableData.GameStage.NONE);
    }
}
