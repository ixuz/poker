package com.antwika.game;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.BitmaskUtil;
import com.antwika.common.util.HandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameUtil {
    private static final Logger logger = LoggerFactory.getLogger(GameUtil.class);

    public static String toNotation(long cards) throws NotationException {
        final String cardsNotation = HandUtil.toNotation(cards);

        final List<String> cn = new ArrayList<>();
        for (int i = 0; i < cardsNotation.length(); i += 2) {
            final String cardNotation = cardsNotation.substring(i, i+2);
            cn.add(cardNotation);
        }

        return String.join(" ", cn);
    }

    public static void seat(Game game, Player player, int seatIndex, int buyIn) {
        final Seat seat = game.getSeats().get(seatIndex);
        resetSeat(seat);
        seat.setPlayer(player);
        seat.setStack(buyIn);

        logger.info("{}: joined the game at seat #{}", player.getPlayerName(), seat.getSeatIndex() + 1);
    }

    public static void seat(Game game, Player player, int buyIn) {
        final Seat seat = findFirstAvailableSeat(game);
        if (seat == null) return;

        seat(game, player, seat.getSeatIndex(), buyIn);
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

    public static void resetAllSeats(Game game) {
        game.getSeats().forEach(GameUtil::resetSeat);
    }

    public static int countAllStacks(Game game) {
        return game.getSeats().stream().mapToInt(seat -> seat.getStack()).sum();
    }

    public static int countTotalPot(Game game) {
        return game.getPots().stream().mapToInt(Pot::getTotalAmount).sum();
    }

    public static int countTotalCommitted(Game game) {
        return game.getSeats().stream().mapToInt(Seat::getCommitted).sum();
    }

    public static int countTotalPotAndCommitted(Game game) {
        return countTotalPot(game) + countTotalCommitted(game);
    }

    public static int countTotalInPlay(Game game) {
        return countAllStacks(game) + countTotalPotAndCommitted(game);
    }

    public static int countPlayersRemainingInHand(Game game) {
        return game.getSeats().stream()
                .filter(seat -> seat.getPlayer() != null)
                .filter(seat -> !seat.isHasFolded())
                .toList()
                .size();
    }

    public static String getStreetName(Game game) {
        return switch (Long.bitCount(game.getCards())) {
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
            logger.info("{}: left the game", player.getPlayerName());
        }
    }

    public static void unseat(List<Seat> seats) {
        seats.forEach(GameUtil::unseat);
    }

    public static void unseat(Game game, Player player) {
        game.getSeats().stream()
                .filter(seat -> seat.getPlayer() == player)
                .forEach(GameUtil::unseat);
    }

    public static void unseatAll(Game game) {
        game.getSeats().stream()
                .filter(seat -> seat.getPlayer() != null)
                .forEach(GameUtil::unseat);
    }

    public static Seat findFirstAvailableSeat(Game game) {
        return game.getSeats().stream()
                .filter(seat -> seat.getPlayer() == null)
                .findFirst()
                .orElse(null);
    }

    public static List<Seat> findAllBustedSeats(Game game) {
        return game.getSeats().stream()
                .filter(seat -> seat.getStack() == 0)
                .toList();
    }

    public static int findHighestCommit(Game game) {
        return game.getSeats().stream()
                .mapToInt(Seat::getCommitted)
                .max()
                .orElse(0);
    }

    public static Seat findNextSeatToAct(Game game, int fromSeat, int skips, boolean mustAct) {
        Seat nextSeat = null;
        int skipped = 0;

        final List<Seat> seats = game.getSeats();

        for (int i = 0; i < seats.size(); i += 1) {
            final int seatIndex = (fromSeat + i + 1) % seats.size();
            final Seat seat = seats.get(seatIndex);

            if (seat.getPlayer() == null) continue;

            if (mustAct) {
                if (!seat.isHasActed() && !seat.isHasFolded()) {
                    nextSeat = seat;
                } else if (seat.getCommitted() < game.getTotalBet() && seat.getStack() > 0 && !seat.isHasFolded()) {
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

    public static boolean hasAllPlayersActed(Game game) {
        int highestCommit = GameUtil.findHighestCommit(game);
        return game.getSeats().stream()
                .filter(seat -> seat.getStack() > 0)
                .filter(seat -> !seat.isHasFolded())
                .filter(seat -> !seat.isHasActed())
                .filter(seat -> seat.getCommitted() != highestCommit)
                .toList()
                .isEmpty();
    }

    public static int getNumberOfPlayersLeftToAct(Game game) {
        final List<Seat> playerSeats = game.getSeats().stream()
                .filter(i -> i.getPlayer() != null)
                .filter(i -> i.getStack() > 0)
                .filter(i -> i.getCards() > 0L)
                .filter(i -> !i.isHasFolded())
                .toList();
        return playerSeats.size();
    }

    public static void prepareBettingRound(Game game) {
        if (Long.bitCount(game.getCards()) != 0) {
            for (Seat seat : game.getSeats()) {
                seat.setHasActed(false);
            }
            game.setActionAt(findNextSeatToAct(game, game.getButtonAt(), 0, true).getSeatIndex());
        }
    }

    public static void startGame(Game game) {
        drawButtonSeatIndex(game);
    }

    public static void stopGame(Game game) {
        unseatAll(game);
    }

    public static void drawButtonSeatIndex(Game game) {
        logger.debug("Drawing cards to determine button position...");
        final Deck deck = game.getDeck();
        deck.resetAndShuffle();

        game.getSeats().stream()
                .filter(seat -> seat.getPlayer() != null)
                .forEach(seat -> seat.setCards(deck.draw()));

        final List<Seat> sortedByCard = game.getSeats().stream()
                .filter(i -> i.getPlayer() != null)
                .sorted(Comparator.comparingInt(e -> BitmaskUtil.CARD_TO_SUIT_INDEX.get(e.getCards())))
                .sorted(Comparator.comparingInt(e -> BitmaskUtil.CARD_TO_RANK_INDEX.get(e.getCards())))
                .toList();

        final Seat winner = sortedByCard.get(sortedByCard.size() - 1);

        if (winner == null) {
            throw new RuntimeException("Failed to draw card for the button position!");
        }

        game.setButtonAt(winner.getSeatIndex());

        resetAllSeats(game);
    }

    public static boolean canStartHand(Game game) {
        return game.getSeats().stream()
                .filter(seat -> seat.getPlayer() != null)
                .filter(seat -> seat.getStack() != 0)
                .toList()
                .size() > 1;
    }

    public static void forcePostBlind(Game game, int blindIndex, int blindAmount) {
        final Seat seat = game.nextSeat(game.getButtonAt(), blindIndex, true);
        final Player player = seat.getPlayer();

        int commitAmount = Math.min(seat.getStack(), blindAmount);
        game.commit(seat, commitAmount);

        game.setActionAt(game.nextSeat(seat.getSeatIndex(), 0, true).getSeatIndex());

        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: posts %s %d", player.getPlayerName(), getBlindName(blindIndex), commitAmount));
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

    public static void forcePostBlinds(Game game, List<Integer> blinds) {
        for (int i = 0; i < blinds.size(); i += 1) {
            forcePostBlind(game, i, blinds.get(i));
        }
    }

    public static void commit(Seat seat, int amount) {
        if (seat.getStack() < amount) throw new RuntimeException("Commit amount is greater than the available stack");
        if (amount <= 0) throw new RuntimeException("Commit must be greater than zero");
        seat.setStack(seat.getStack() - amount);
        seat.setCommitted(seat.getCommitted() + amount);
    }

    public static void pushButton(Game game) {
        game.setButtonAt(findNextSeatToAct(game, game.getButtonAt(), 0, false).getSeatIndex());
    }
}
