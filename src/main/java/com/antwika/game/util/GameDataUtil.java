package com.antwika.game.util;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.BitmaskUtil;
import com.antwika.common.util.HandUtil;
import com.antwika.game.data.*;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.PlayerActionRequest;
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

    public static void seat(GameData gameData, Player player, int seatIndex, int buyIn) {
        final Seat seat = gameData.getSeats().get(seatIndex);
        resetSeat(seat);
        seat.setPlayer(player);
        seat.setStack(buyIn);

        logger.info("{}: joined the game at seat #{}", player.getPlayerData().getPlayerName(), seat.getSeatIndex() + 1);
    }

    public static void seat(GameData gameData, Player player, int buyIn) {
        final Seat seat = findFirstAvailableSeat(gameData);
        if (seat == null) return;

        seat(gameData, player, seat.getSeatIndex(), buyIn);
    }

    public static void resetSeat(Seat seat) {
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
        return gameData.getSeats().stream().mapToInt(Seat::getStack).sum();
    }

    public static int countTotalPot(GameData gameData) {
        return gameData.getPots().stream().mapToInt(Pot::getTotalAmount).sum();
    }

    public static int countTotalCommitted(GameData gameData) {
        return gameData.getSeats().stream().mapToInt(Seat::getCommitted).sum();
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

    public static Seat getSeat(GameData gameData, Player player) {
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
    public static void unseat(Seat seat) {
        final Player player = seat.getPlayer();
        seat.setPlayer(null);
        seat.setStack(0);
        if (player != null) {
            logger.info("{}: left the game", player.getPlayerData().getPlayerName());
        }
    }

    public static void unseat(List<Seat> seats) {
        seats.forEach(GameDataUtil::unseat);
    }

    public static void unseatAll(GameData gameData) {
        gameData.getSeats().stream()
                .filter(seat -> seat.getPlayer() != null)
                .forEach(GameDataUtil::unseat);
    }

    public static Seat findFirstAvailableSeat(GameData gameData) {
        return gameData.getSeats().stream()
                .filter(seat -> seat.getPlayer() == null)
                .findFirst()
                .orElse(null);
    }

    public static List<Seat> findAllBustedSeats(GameData gameData) {
        return gameData.getSeats().stream()
                .filter(seat -> seat.getStack() == 0)
                .toList();
    }

    public static int findHighestCommit(GameData gameData) {
        return gameData.getSeats().stream()
                .mapToInt(Seat::getCommitted)
                .max()
                .orElse(0);
    }

    public static Seat findNextSeatToAct(GameData gameData, int fromSeat, int skips, boolean mustAct) {
        Seat nextSeat = null;
        int skipped = 0;

        final List<Seat> seats = gameData.getSeats();

        for (int i = 0; i < seats.size(); i += 1) {
            final int seatIndex = (fromSeat + i + 1) % seats.size();
            final Seat seat = seats.get(seatIndex);

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
        final List<Seat> playerSeats = gameData.getSeats().stream()
                .filter(i -> i.getPlayer() != null)
                .filter(i -> i.getStack() > 0)
                .filter(i -> i.getCards() > 0L)
                .filter(i -> !i.isHasFolded())
                .toList();
        return playerSeats.size();
    }

    public static void prepareBettingRound(GameData gameData) {
        if (Long.bitCount(gameData.getCards()) != 0) {
            for (Seat seat : gameData.getSeats()) {
                seat.setHasActed(false);
            }
            gameData.setActionAt(findNextSeatToAct(gameData, gameData.getButtonAt(), 0, true).getSeatIndex());
        }
    }

    public static void startGame(GameData gameData) {
        drawButtonSeatIndex(gameData);
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

        final List<Seat> sortedByCard = gameData.getSeats().stream()
                .filter(i -> i.getPlayer() != null)
                .sorted(Comparator.comparingInt(e -> BitmaskUtil.CARD_TO_SUIT_INDEX.get(e.getCards())))
                .sorted(Comparator.comparingInt(e -> BitmaskUtil.CARD_TO_RANK_INDEX.get(e.getCards())))
                .toList();

        final Seat winner = sortedByCard.get(sortedByCard.size() - 1);

        if (winner == null) {
            throw new RuntimeException("Failed to draw card for the button position!");
        }

        gameData.setButtonAt(winner.getSeatIndex());

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

        final Seat seat = GameDataUtil.findNextSeatToAct(gameData, gameData.getButtonAt(), blindIndex, true);
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

    public static void commit(Seat seat, int amount) {
        if (seat.getStack() < amount) throw new RuntimeException("Commit amount is greater than the available stack");
        if (amount <= 0) throw new RuntimeException("Commit must be greater than zero");
        seat.setStack(seat.getStack() - amount);
        seat.setCommitted(seat.getCommitted() + amount);
    }

    public static void pushButton(GameData gameData) {
        gameData.setButtonAt(findNextSeatToAct(gameData, gameData.getButtonAt(), 0, false).getSeatIndex());
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

    public static void dealCards(GameData gameData) throws NotationException {
        final List<Seat> seats = gameData.getSeats();
        final DeckData deckData = gameData.getDeckData();
        logger.info("*** HOLE CARDS ***");
        for (int i = 0; i < seats.size() * 2; i += 1) {
            final int seatIndex = (gameData.getActionAt() + i + 1) % seats.size();
            final Seat seat = seats.get(seatIndex);
            if (seat.getPlayer() == null) continue;
            Long card = DeckUtil.draw(deckData);
            if (card == null) throw new RuntimeException("The card drawn from the deck is null");
            seat.setCards(seat.getCards() | card);
            try {
                logger.debug("Deal card {} to {}", HandUtil.toNotation(card), seat.getPlayer());
            } catch (NotationException e) {
                throw new RuntimeException(e);
            }
        }

        GameLog.printTableSeatCardsInfo(gameData);
    }

    public static void bettingRound(GameData gameData) {
        GameDataUtil.prepareBettingRound(gameData);

        while (true) {
            if (GameDataUtil.countPlayersRemainingInHand(gameData) == 1) {
                logger.debug("All but one player has folded, hand must end");
                break;
            }

            if (GameDataUtil.getNumberOfPlayersLeftToAct(gameData) < 1) {
                break;
            }

            final List<Seat> seats = gameData.getSeats();
            final int actionAt = gameData.getActionAt();

            final Seat seat = seats.get(actionAt);

            final Seat seatAfter = GameDataUtil.findNextSeatToAct(gameData, actionAt, 0, true);
            if (seat == null) break;

            if (seat.isHasFolded()) {
                seat.setHasActed(true);
                gameData.setActionAt(seatAfter.getSeatIndex());
                continue;
            }

            final Player player = seat.getPlayer();

            final int totalBet = gameData.getTotalBet();
            final int bigBlind = gameData.getBigBlind();
            final int lastRaise = gameData.getLastRaise();
            final int toCall = Math.min(seat.getStack(), totalBet - seat.getCommitted());
            final int minRaise = Math.min(seat.getStack(), Math.max(lastRaise, bigBlind));
            final int minBet = totalBet + minRaise - seat.getCommitted();
            final int smallestValidRaise = Math.min(totalBet + bigBlind, seat.getStack());

            if (toCall > 0) {
                logger.debug("{}, {} to call", seat.getPlayer(), toCall);
            } else {
                logger.debug("{}, Check or bet?", seat.getPlayer());
            }

            if (seat.getStack() == 0) {
                break;
            }

            final IEvent response = player.handle(new PlayerActionRequest(player, gameData, totalBet, toCall, minBet, smallestValidRaise));

            ActionHandler.handleEvent(response);

            if (GameDataUtil.hasAllPlayersActed(gameData)) {
                break;
            }

            final Seat theNextSeat = GameDataUtil.findNextSeatToAct(gameData, actionAt, 0, true);
            if (theNextSeat == null) {
                break;
            }

            gameData.setActionAt(theNextSeat.getSeatIndex());
        }

        logger.debug("Betting round ended");
    }

    public static void collect(GameData gameData) {
        final List<Seat> seats = gameData.getSeats();
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
        final List<Pot> pots = gameData.getPots();
        final Long cards = gameData.getCards();
        final int buttonAt = gameData.getButtonAt();
        final List<Seat> seats = gameData.getSeats();

        logger.debug("Showdown");
        final List<Pot> collapsed = PotsUtil.collapsePots(pots);
        final List<CandidateData> winners = PotsUtil.determineWinners(collapsed, cards, buttonAt, seats.size());

        for (CandidateData winner : winners) {
            final Seat seat = winner.getSeat();
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

        int totalStacks = seats.stream().filter(i -> i.getPlayer() != null).mapToInt(Seat::getStack).sum();
        int totalPot = GameDataUtil.countTotalPot(gameData);
        if (totalStacks + totalPot != gameData.getChipsInPlay()) {
            throw new RuntimeException("Invalid amount of chips");
        }

        GameLog.printSummary(gameData);
    }

    public static void hand(GameData gameData) throws NotationException {
        GameDataUtil.prepareHand(gameData);
        GameDataUtil.unseat(GameDataUtil.findAllBustedSeats(gameData));
        GameDataUtil.resetAllSeats(gameData);

        GameLog.printGameInfo(gameData);
        DeckUtil.resetAndShuffle(gameData.getDeckData());
        GameLog.printTableInfo(gameData);
        GameLog.printTableSeatsInfo(gameData);
        GameDataUtil.forcePostBlinds(gameData, List.of(gameData.getSmallBlind(), gameData.getBigBlind()));
        GameDataUtil.dealCards(gameData);

        GameDataUtil.bettingRound(gameData);
        GameDataUtil.collect(gameData);
        if (GameDataUtil.countPlayersRemainingInHand(gameData) > 1) {
            GameDataUtil.dealCommunityCards(gameData, 3);
            GameDataUtil.bettingRound(gameData);
            GameDataUtil.collect(gameData);
        }
        if (GameDataUtil.countPlayersRemainingInHand(gameData) > 1) {
            GameDataUtil.dealCommunityCards(gameData, 1);
            GameDataUtil.bettingRound(gameData);
            GameDataUtil.collect(gameData);
        }
        if (GameDataUtil.countPlayersRemainingInHand(gameData) > 1) {
            GameDataUtil.dealCommunityCards(gameData, 1);
            GameDataUtil.bettingRound(gameData);
            GameDataUtil.collect(gameData);
        }
        GameDataUtil.showdown(gameData);
        GameDataUtil.pushButton(gameData);
        logger.info("--- HAND END ---");
    }

    public static int calcBetSize(GameData gameData, Player player, float betSizePercent) {
        final Seat seat = GameDataUtil.getSeat(gameData, player);
        int lastRaise = gameData.getLastRaise();
        int totalPot = GameDataUtil.countTotalPotAndCommitted(gameData);
        int deadMoney = totalPot - lastRaise;
        int desiredBet = (int) ((lastRaise * 3 + deadMoney) * betSizePercent);
        int minimumBet = Math.min(seat.getStack(), gameData.getBigBlind());
        int bet = Math.max(desiredBet, minimumBet);
        return Math.min(seat.getStack(), bet);
    }
}
