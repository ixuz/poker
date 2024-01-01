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
        return game.getSeats().stream().mapToInt(Seat::getStack).sum();
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

    public static int countPlayersRemainingInHand(Game game) {
        return game.getSeats().stream()
                .filter(seat -> seat.getPlayer() != null)
                .filter(seat -> !seat.isHasFolded())
                .toList()
                .size();
    }

    public static Seat getSeat(Game game, Player player) {
        return game.getSeats().stream()
                .filter(i -> i.getPlayer() == player)
                .findFirst()
                .orElse(null);
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
        Deck.resetAndShuffle(deck);

        game.getSeats().stream()
                .filter(seat -> seat.getPlayer() != null)
                .forEach(seat -> seat.setCards(Deck.draw(deck)));

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

        final Seat seat = GameUtil.findNextSeatToAct(game, game.getButtonAt(), blindIndex, true);
        final Player player = seat.getPlayer();

        int commitAmount = Math.min(seat.getStack(), blindAmount);
        GameUtil.commit(seat, commitAmount);

        game.setActionAt(GameUtil.findNextSeatToAct(game, seat.getSeatIndex(), 0, true).getSeatIndex());

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

    public static void dealCommunityCards(Game game, int count) {
        long prev = game.getCards();
        long add = 0L;
        for (int i = 0; i < count; i += 1) {
            add |= Deck.draw(game.getDeck());
        }

        game.setCards(game.getCards() | add);

        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(String.format("*** %s ***", GameUtil.getStreetName(game)));
            if (prev != 0L) {
                sb.append(String.format(" [%s]", GameUtil.toNotation(prev)));
            }
            if (add != 0L) {
                sb.append(String.format(" [%s]", GameUtil.toNotation(add)));
            }
            logger.info(sb.toString());
            logger.info("Total pot: {}", GameUtil.countTotalPot(game));
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void prepareHand(Game game) {
        logger.info("--- HAND BEGIN ---");
        game.getPots().clear();
        game.setHandId(game.getHandId() + 1L);
        game.setCards(0L);
        game.setDelivered(0);
        game.setTotalBet(game.getBigBlind());
        game.setLastRaise(game.getBigBlind());
        game.setChipsInPlay(GameUtil.countAllStacks(game));
    }

    public static void dealCards(Game game) throws NotationException {
        final List<Seat> seats = game.getSeats();
        logger.info("*** HOLE CARDS ***");
        for (int i = 0; i < seats.size() * 2; i += 1) {
            final int seatIndex = (game.getActionAt() + i + 1) % seats.size();
            final Seat seat = seats.get(seatIndex);
            if (seat.getPlayer() == null) continue;
            final long card = Deck.draw(game.getDeck());
            seat.setCards(seat.getCards() | card);
            try {
                logger.debug("Deal card {} to {}", HandUtil.toNotation(card), seat.getPlayer());
            } catch (NotationException e) {
                throw new RuntimeException(e);
            }
        }

        GameLog.printTableSeatCardsInfo(game);
    }

    public static void bettingRound(Game game) {
        GameUtil.prepareBettingRound(game);

        while (true) {
            if (GameUtil.countPlayersRemainingInHand(game) == 1) {
                logger.debug("All but one player has folded, hand must end");
                break;
            }

            if (GameUtil.getNumberOfPlayersLeftToAct(game) < 2) {
                break;
            }

            final List<Seat> seats = game.getSeats();
            final int actionAt = game.getActionAt();

            final Seat seat = seats.get(actionAt);

            final Seat seatAfter = GameUtil.findNextSeatToAct(game, actionAt, 0, true);
            if (seat == null) break;

            if (seat.isHasFolded()) {
                seat.setHasActed(true);
                game.setActionAt(seatAfter.getSeatIndex());
                continue;
            }

            final Player player = seat.getPlayer();

            final int totalBet = game.getTotalBet();
            final int bigBlind = game.getBigBlind();
            final int lastRaise = game.getLastRaise();
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

            final Event response = player.handle(new PlayerActionRequest(player, game, totalBet, toCall, minBet, smallestValidRaise));

            game.handleEvent(response);

            if (GameUtil.hasAllPlayersActed(game)) {
                break;
            }

            final Seat theNextSeat = GameUtil.findNextSeatToAct(game, actionAt, 0, true);
            if (theNextSeat == null) {
                break;
            }

            game.setActionAt(theNextSeat.getSeatIndex());
        }

        logger.debug("Betting round ended");
    }

    public static void collect(Game game) {
        final List<Seat> seats = game.getSeats();
        game.getPots().addAll(Pots.collectBets(seats));

        game.setLastRaise(0);
        game.setTotalBet(0);

        int totalStacks = GameUtil.countAllStacks(game);
        int totalPot = GameUtil.countTotalPot(game);
        if (totalStacks + totalPot != game.getChipsInPlay()) {
            throw new RuntimeException("Invalid amount of chips");
        }
    }

    public static void showdown(Game game) throws NotationException {
        final List<Pot> pots = game.getPots();
        final Long cards = game.getCards();
        final int buttonAt = game.getButtonAt();
        final List<Seat> seats = game.getSeats();

        logger.debug("Showdown");
        final List<Pot> collapsed = Pots.collapsePots(pots);
        final List<Candidate> winners = Pots.determineWinners(collapsed, cards, buttonAt, seats.size());

        for (Candidate winner : winners) {
            final Seat seat = winner.getSeat();
            final Player player = seat.getPlayer();
            int amount = winner.getAmount();
            game.setDelivered(game.getDelivered() + amount);
            winner.getSeat().setStack(seat.getStack() + amount);

            logger.info("{} collected {} from {}",
                    player.getPlayerName(),
                    amount,
                    winner.getPotName());
        }
        pots.clear();

        int totalStacks = seats.stream().filter(i -> i.getPlayer() != null).mapToInt(Seat::getStack).sum();
        int totalPot = GameUtil.countTotalPot(game);
        if (totalStacks + totalPot != game.getChipsInPlay()) {
            throw new RuntimeException("Invalid amount of chips");
        }

        GameLog.printSummary(game);
    }

    public static void hand(Game game) throws NotationException {
        GameUtil.prepareHand(game);
        GameUtil.unseat(GameUtil.findAllBustedSeats(game));
        GameUtil.resetAllSeats(game);

        GameLog.printGameInfo(game);
        Deck.resetAndShuffle(game.getDeck());
        GameLog.printTableInfo(game);
        GameLog.printTableSeatsInfo(game);
        GameUtil.forcePostBlinds(game, List.of(game.getSmallBlind(), game.getBigBlind()));
        GameUtil.dealCards(game);

        GameUtil.bettingRound(game);
        GameUtil.collect(game);
        if (GameUtil.countPlayersRemainingInHand(game) > 1) {
            GameUtil.dealCommunityCards(game, 3);
            GameUtil.bettingRound(game);
            GameUtil.collect(game);
        }
        if (GameUtil.countPlayersRemainingInHand(game) > 1) {
            GameUtil.dealCommunityCards(game, 1);
            GameUtil.bettingRound(game);
            GameUtil.collect(game);
        }
        if (GameUtil.countPlayersRemainingInHand(game) > 1) {
            GameUtil.dealCommunityCards(game, 1);
            GameUtil.bettingRound(game);
            GameUtil.collect(game);
        }
        GameUtil.showdown(game);
        GameUtil.pushButton(game);
        logger.info("--- HAND END ---");
    }
}
