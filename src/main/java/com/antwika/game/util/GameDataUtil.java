package com.antwika.game.util;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.BitmaskUtil;
import com.antwika.common.util.HandUtil;
import com.antwika.game.data.*;
import com.antwika.game.event.*;
import com.antwika.game.handler.ActionHandler;
import com.antwika.game.log.GameLog;
import com.antwika.game.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameDataUtil {
    private static final Logger logger = LoggerFactory.getLogger(GameDataUtil.class);

    public static String toNotation(long cards) throws NotationException {
        final String cardsNotation = HandUtil.toNotation(cards);

        final List<String> cn = new ArrayList<>();
        for (int i = 0; i < cardsNotation.length(); i += 2) {
            final String cardNotation = cardsNotation.substring(i, i+2);
            cn.add(cardNotation);
        }

        return String.join(" ", cn);
    }

    public static boolean seat(GameData gameData, Player player, int seatIndex, int buyIn) {
        final SeatData seat = gameData.getSeats().get(seatIndex);

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

        ActionHandler.handleEvent(new PlayerJoinEvent(gameData, seat, player));
        return true;
    }

    public static void seat(GameData gameData, Player player, int buyIn) {
        final SeatData seat = findFirstAvailableSeat(gameData);
        if (seat == null) return;

        seat(gameData, player, seat.getSeatIndex(), buyIn);
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

    public static void resetAllSeats(GameData gameData) {
        gameData.getSeats().forEach(GameDataUtil::resetSeat);
    }

    public static int countAllStacks(GameData gameData) {
        return gameData.getSeats().stream().mapToInt(SeatData::getStack).sum();
    }

    public static int countTotalPot(GameData gameData) {
        return gameData.getPots().stream().mapToInt(PotData::getTotalAmount).sum();
    }

    public static int countTotalCommitted(GameData gameData) {
        return gameData.getSeats().stream().mapToInt(SeatData::getCommitted).sum();
    }

    public static int countTotalPotAndCommitted(GameData gameData) {
        return countTotalPot(gameData) + countTotalCommitted(gameData);
    }

    public static int countPlayersRemainingInHand(GameData gameData) {
        return gameData.getSeats().stream()
                .filter(seat -> seat.getPlayer() != null)
                .filter(seat -> !seat.isHasFolded())
                .toList()
                .size();
    }

    public static SeatData getSeat(GameData gameData, Player player) {
        return gameData.getSeats().stream()
                .filter(i -> i.getPlayer() == player)
                .findFirst()
                .orElse(null);
    }

    public static String getStreetName(GameData gameData) {
        return switch (Long.bitCount(gameData.getCards())) {
            case 3 -> "FLOP";
            case 4 -> "TURN";
            case 5 -> "RIVER";
            default -> "PREFLOP";
        };
    }

    public static void unseat(GameData gameData, SeatData seat) {
        final Player player = seat.getPlayer();
        seat.setPlayer(null);
        seat.setStack(0);
        if (player != null) {
            ActionHandler.handleEvent(new PlayerLeaveEvent(gameData, seat, player));
        }
    }

    public static void unseat(GameData gameData, List<SeatData> seats) {
        seats.forEach(seat -> unseat(gameData, seat));
    }

    public static void unseatAll(GameData gameData) {
        gameData.getSeats().stream()
                .filter(seat -> seat.getPlayer() != null)
                .forEach(seat -> unseat(gameData, seat));
    }

    public static SeatData findFirstAvailableSeat(GameData gameData) {
        return gameData.getSeats().stream()
                .filter(seat -> seat.getPlayer() == null)
                .findFirst()
                .orElse(null);
    }

    public static List<SeatData> findAllBustedSeats(GameData gameData) {
        return gameData.getSeats().stream()
                .filter(seat -> seat.getStack() == 0)
                .toList();
    }

    public static int findHighestCommit(GameData gameData) {
        return gameData.getSeats().stream()
                .mapToInt(SeatData::getCommitted)
                .max()
                .orElse(0);
    }

    public static SeatData findNextSeatToAct(GameData gameData, int fromSeat, int skips, boolean mustAct) {
        SeatData nextSeat = null;
        int skipped = 0;

        final List<SeatData> seats = gameData.getSeats();

        for (int i = 0; i < seats.size(); i += 1) {
            final int seatIndex = (fromSeat + i + 1) % seats.size();
            final SeatData seat = seats.get(seatIndex);

            if (seat.getPlayer() == null) continue;

            if (mustAct) {
                if (!seat.isHasActed() && !seat.isHasFolded()) {
                    nextSeat = seat;
                } else if (seat.getCommitted() < gameData.getTotalBet() && seat.getStack() > 0 && !seat.isHasFolded()) {
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

    public static boolean hasAllPlayersActed(GameData gameData) {
        int highestCommit = GameDataUtil.findHighestCommit(gameData);

        final int notActedYetCount = gameData.getSeats().stream()
                .filter(seat -> seat.getStack() > 0)
                .filter(seat -> !seat.isHasFolded())
                .filter(seat -> !seat.isHasActed())
                .toList()
                .size();

        if (notActedYetCount > 0) {
            return false;
        }

        final int notMatchedBetCount = gameData.getSeats().stream()
                .filter(seat -> seat.getStack() > 0)
                .filter(seat -> !seat.isHasFolded())
                .filter(seat -> seat.getCommitted() != highestCommit)
                .toList()
                .size();

        return notMatchedBetCount == 0;
    }

    public static int getNumberOfPlayersLeftToAct(GameData gameData) {
        final List<SeatData> playerSeats = gameData.getSeats().stream()
                .filter(i -> i.getPlayer() != null)
                .filter(i -> i.getStack() > 0)
                .filter(i -> i.getCards() > 0L)
                .filter(i -> !i.isHasFolded())
                .toList();
        return playerSeats.size();
    }

    public static void prepareBettingRound(GameData gameData) {
        if (Long.bitCount(gameData.getCards()) != 0) {
            for (SeatData seat : gameData.getSeats()) {
                seat.setHasActed(false);
            }
            gameData.setActionAt(findNextSeatToAct(gameData, gameData.getButtonAt(), 0, true).getSeatIndex());
        }
    }

    public static boolean startGame(GameData gameData) {
        final List<SeatData> seats = gameData.getSeats();
        final int playerWithStackCount = seats.stream()
                .filter(seat -> seat.getPlayer() != null)
                .filter(seat -> seat.getStack() > 0)
                .toList()
                .size();
        final boolean enoughPlayersToStartHand = playerWithStackCount > 1;

        if (enoughPlayersToStartHand) {
            drawButtonSeatIndex(gameData);
            gameData.setGameStage(GameData.GameStage.NONE);
            return true;
        }

        return false;
    }

    public static void stopGame(GameData gameData) {
        unseatAll(gameData);
    }

    public static void drawButtonSeatIndex(GameData gameData) {
        logger.debug("Drawing cards to determine button position...");
        final DeckData deckData = gameData.getDeckData();
        DeckUtil.resetAndShuffle(deckData);

        gameData.getSeats().stream()
                .filter(seat -> seat.getPlayer() != null)
                .forEach(seat -> seat.setCards(DeckUtil.draw(deckData)));

        final List<SeatData> sortedByCard = gameData.getSeats().stream()
                .filter(i -> i.getPlayer() != null)
                .sorted(Comparator.comparingInt(e -> BitmaskUtil.CARD_TO_SUIT_INDEX.get(e.getCards())))
                .sorted(Comparator.comparingInt(e -> BitmaskUtil.CARD_TO_RANK_INDEX.get(e.getCards())))
                .toList();

        final SeatData winner = sortedByCard.get(sortedByCard.size() - 1);

        if (winner == null) {
            throw new RuntimeException("Failed to draw card for the button position!");
        }

        gameData.setButtonAt(winner.getSeatIndex());

        logger.debug("Starting button position at seat #{}", winner.getSeatIndex() + 1);

        resetAllSeats(gameData);
    }

    public static boolean canStartHand(GameData gameData) {
        return gameData.getSeats().stream()
                .filter(seat -> seat.getPlayer() != null)
                .filter(seat -> seat.getStack() != 0)
                .toList()
                .size() > 1;
    }

    public static void forcePostBlind(GameData gameData, int blindIndex, int blindAmount) {

        final SeatData seat = GameDataUtil.findNextSeatToAct(gameData, gameData.getButtonAt(), blindIndex, true);
        final Player player = seat.getPlayer();

        int commitAmount = Math.min(seat.getStack(), blindAmount);
        GameDataUtil.commit(seat, commitAmount);

        gameData.setActionAt(GameDataUtil.findNextSeatToAct(gameData, seat.getSeatIndex(), 0, true).getSeatIndex());

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

    public static void forcePostBlinds(GameData gameData, List<Integer> blinds) {
        for (int i = 0; i < blinds.size(); i += 1) {
            forcePostBlind(gameData, i, blinds.get(i));
        }
    }

    public static void commit(SeatData seat, int amount) {
        if (seat.getStack() < amount) throw new RuntimeException("Commit amount is greater than the available stack");
        if (amount <= 0) throw new RuntimeException("Commit must be greater than zero");
        seat.setStack(seat.getStack() - amount);
        seat.setCommitted(seat.getCommitted() + amount);
    }

    public static void dealCommunityCards(GameData gameData, int count) {
        long prev = gameData.getCards();
        long add = 0L;
        final DeckData deckData = gameData.getDeckData();
        for (int i = 0; i < count; i += 1) {
            Long card = DeckUtil.draw(deckData);
            if (card == null) throw new RuntimeException("The card drawn from the deck is null");
            add |= card;
        }

        gameData.setCards(gameData.getCards() | add);

        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(String.format("*** %s ***", GameDataUtil.getStreetName(gameData)));
            if (prev != 0L) {
                sb.append(String.format(" [%s]", GameDataUtil.toNotation(prev)));
            }
            if (add != 0L) {
                sb.append(String.format(" [%s]", GameDataUtil.toNotation(add)));
            }
            logger.info(sb.toString());
            logger.info("Total pot: {}", GameDataUtil.countTotalPot(gameData));
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void prepareHand(GameData gameData) {
        logger.info("--- HAND BEGIN ---");
        gameData.getPots().clear();
        gameData.setHandId(gameData.getHandId() + 1L);
        gameData.setCards(0L);
        gameData.setDelivered(0);
        gameData.setTotalBet(gameData.getBigBlind());
        gameData.setLastRaise(gameData.getBigBlind());
        gameData.setChipsInPlay(GameDataUtil.countAllStacks(gameData));
    }

    public static void collect(GameData gameData) {
        final List<SeatData> seats = gameData.getSeats();
        gameData.getPots().addAll(PotsUtil.collectBets(seats));

        gameData.setLastRaise(0);
        gameData.setTotalBet(0);

        int totalStacks = GameDataUtil.countAllStacks(gameData);
        int totalPot = GameDataUtil.countTotalPot(gameData);
        if (totalStacks + totalPot != gameData.getChipsInPlay()) {
            throw new RuntimeException("Invalid amount of chips");
        }
    }

    public static void showdown(GameData gameData) throws NotationException {
        final List<PotData> pots = gameData.getPots();
        final Long cards = gameData.getCards();
        final int buttonAt = gameData.getButtonAt();
        final List<SeatData> seats = gameData.getSeats();

        logger.debug("Showdown");
        final List<PotData> collapsed = PotsUtil.collapsePots(pots);
        final List<CandidateData> winners = PotsUtil.determineWinners(collapsed, cards, buttonAt, seats.size());

        for (CandidateData winner : winners) {
            final SeatData seat = winner.getSeat();
            final Player player = seat.getPlayer();
            int amount = winner.getAmount();
            gameData.setDelivered(gameData.getDelivered() + amount);
            winner.getSeat().setStack(seat.getStack() + amount);

            logger.info("{} collected {} from {}",
                    player.getPlayerData().getPlayerName(),
                    amount,
                    winner.getPotName());
        }
        pots.clear();

        int totalStacks = seats.stream().filter(i -> i.getPlayer() != null).mapToInt(SeatData::getStack).sum();
        int totalPot = GameDataUtil.countTotalPot(gameData);
        if (totalStacks + totalPot != gameData.getChipsInPlay()) {
            throw new RuntimeException("Invalid amount of chips");
        }

        GameLog.printSummary(gameData);
    }

    public static int calcBetSize(GameData gameData, Player player, float betSizePercent) {
        final SeatData seat = GameDataUtil.getSeat(gameData, player);
        int lastRaise = gameData.getLastRaise();
        int totalPot = GameDataUtil.countTotalPotAndCommitted(gameData);
        int deadMoney = totalPot - lastRaise;
        int desiredBet = (int) ((lastRaise * 3 + deadMoney) * betSizePercent);
        int minimumBet = Math.min(seat.getStack(), gameData.getBigBlind());
        int bet = Math.max(desiredBet, minimumBet);
        return Math.min(seat.getStack(), bet);
    }

    public static boolean isHandOngoing(GameData gameData) {
        return !gameData.getGameStage().equals(GameData.GameStage.NONE);
    }
}
