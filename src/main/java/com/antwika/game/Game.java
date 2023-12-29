package com.antwika.game;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.eval.core.IEvaluation;
import com.antwika.eval.core.IHandProcessor;
import com.antwika.eval.exception.HandEvaluatorException;
import com.antwika.eval.processor.TexasHoldemProcessor;
import com.antwika.eval.util.HandEvaluatorUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class Game extends EventHandler {
    private static Logger logger = LoggerFactory.getLogger(Game.class);
    private long id = 0L;

    private List<Seat> seats = new ArrayList<>();

    private int buttonAt = 0;

    private int actionAt = 0;

    private int totalBet = 0;

    private int lastRaise = 0;

    private Long cards = 0L;

    private final List<Pot> pots = new ArrayList<>();

    private final Deck deck;

    public Game(int seatCount) {
        for (int i = 0; i < seatCount; i += 1) {
            seats.add(new Seat());
        }

        deck = new Deck();
    }

    public void join(Player player) {
        Seat available = null;
        for (Seat seat : seats) {
            if (seat.getPlayer() == null) {
                available = seat;
                break;
            }
        }

        if (available == null) return;

        available.setPlayer(player);
        available.setCards(0L);
        available.setStack(1000);
        available.setCommitted(0);
        available.setPostedSmallBlindLastRound(false);
        available.setPostedBigBlindLastRound(false);
    }

    public void start() {
        deck.resetAndShuffle();
    }

    public void blinds() {
        int smallBlindAmount = 5;
        seats.get(1).setStack(seats.get(1).getStack() - smallBlindAmount);
        seats.get(1).setCommitted(seats.get(1).getCommitted() + smallBlindAmount);
        actionAt += 1;
        logger.info("{} posted small blind {}", seats.get(1).getPlayer(), smallBlindAmount);

        int bigBlindAmount = 10;
        seats.get(2).setStack(seats.get(2).getStack() - bigBlindAmount);
        seats.get(2).setCommitted(seats.get(2).getCommitted() + bigBlindAmount);
        actionAt += 1;
        logger.info("{} posted big blind {}", seats.get(2).getPlayer(), bigBlindAmount);

        totalBet = bigBlindAmount;
        lastRaise = bigBlindAmount;
    }

    public void bettingRound() {
        for (Seat seat : seats) {
            seat.setHasActed(false);
        }

        while (true) {
            final Seat seat = nextSeat(actionAt, 0, true);
            final Seat seatAfter = nextSeat(actionAt, 1, true);
            if (seat == null) break;

            final Player player = seat.getPlayer();

            final int toCall = totalBet - seat.getCommitted();
            final int minCompleteRaise = Math.max(lastRaise, 10);
            final int minCompleteBet = totalBet + minCompleteRaise;
            final int minRaise = Math.min(seat.getStack(), Math.max(lastRaise, 10));
            final int minBet = totalBet + minRaise - seat.getCommitted();

            if (toCall > 0) {
                logger.info("{}, {} to call", seat.getPlayer(), toCall);
            } else {
                logger.info("{}, Check or bet?", seat.getPlayer());
            }

            final Event response = player.handle(new PlayerActionRequest(player, this, toCall, minBet, minRaise));

            if (response instanceof PlayerActionResponse e) {
                if (e.action.equals("CHECK")) {
                    seat.setHasActed(true);
                    logger.info("{} checked", seat.getPlayer());
                } else if (e.action.equals("BET")) {
                    seat.setHasActed(true);
                    seat.setCommitted(seat.getCommitted() + e.amount);
                    seat.setStack(seat.getStack() - e.amount);
                    totalBet = seat.getCommitted();
                    lastRaise = e.amount;
                    logger.info("{} bet {}, total bet {}", seat.getPlayer(), e.amount, totalBet);
                    if (seat.getStack() == 0) {
                        logger.info("{} is all in", seat.getPlayer());
                    }
                } else if (e.action.equals("RAISE")) {
                    seat.setHasActed(true);
                    seat.setCommitted(seat.getCommitted() + e.amount);
                    seat.setStack(seat.getStack() - e.amount);
                    if (seat.getCommitted() > totalBet) {
                        totalBet = seat.getCommitted();
                        lastRaise = e.amount;
                    }
                    logger.info("{} raise {}, total bet {}", seat.getPlayer(), e.amount, totalBet);
                    if (seat.getStack() == 0) {
                        logger.info("{} is all in", seat.getPlayer());
                    }
                } else if (e.action.equals("CALL")) {
                    seat.setHasActed(true);
                    seat.setCommitted(seat.getCommitted() + e.amount);
                    seat.setStack(seat.getStack() - e.amount);
                    logger.info("{} calls {}", seat.getPlayer(), e.amount);
                    if (seat.getStack() == 0) {
                        logger.info("{} is all in", seat.getPlayer());
                    }
                }
            }

            actionAt = seatAfter.getSeatIndex();
        }

        logger.info("Betting round ended");
    }

    public void collect() {
        pots.addAll(Pots.collectBets(seats));

        lastRaise = 0;
        totalBet = 0;

        int totalPot = pots.stream().mapToInt(Pot::getTotalAmount).sum();

        logger.info("Total pot {}", totalPot);
    }

    public Seat nextSeat(int fromSeat, int skips, boolean mustAct) {
        Seat nextSeat = null;
        int skipped = 0;

        for (int i = 0; i < seats.size(); i += 1) {
            final int seatIndex = (fromSeat + i + 1) % seats.size();
            final Seat seat = seats.get(seatIndex);

            if (seat.getPlayer() == null) continue;

            if (mustAct) {
                if (!seat.isHasActed()) {
                    nextSeat = seat;
                } else if (seat.getCommitted() < totalBet && seat.getStack() > 0) {
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

    public void dealCards() {
        for (int i = 0; i < seats.size() * 2; i += 1) {
            final int seatIndex = (actionAt + i + 1) % seats.size();
            final Seat seat = seats.get(seatIndex);
            final long card = deck.draw();
            seat.setCards(seat.getCards() | card);
            try {
                logger.info("Deal card {} to {}", HandUtil.toNotation(card), seat.getPlayer());
            } catch (NotationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void dealFlop() {
        cards |= deck.draw();
        cards |= deck.draw();
        cards |= deck.draw();
        try {
            logger.info("Deal flop {}", HandUtil.toNotation(cards));
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
    }

    public void dealTurn() {
        cards |= deck.draw();
        try {
            logger.info("Deal turn {}", HandUtil.toNotation(cards));
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
    }

    public void dealRiver() {
        cards |= deck.draw();
        try {
            logger.info("Deal river {}", HandUtil.toNotation(cards));
        } catch (NotationException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    @AllArgsConstructor
    @ToString(onlyExplicitlyIncluded = true)
    public class SeatHand {
        @ToString.Include
        public Seat seat;
        public long hand;
        @ToString.Include
        public IEvaluation evaluation;
        public String groupId;
    }

    public int getTotalPot(boolean includeCommitted) {
        int sum = 0;

        for (Pot pot : pots) {
            sum += pot.getTotalAmount();
        }

        for (Seat seat : seats) {
            sum += seat.getCommitted();
        }

        return sum;
    }

    public int getLastRaise() {
        return lastRaise;
    }

    public Seat getSeat(Player player) {
        for (Seat seat : seats) {
            if (seat.getPlayer() == player) {
                return seat;
            }
        }
        return null;
    }

    public void showdown() {
        logger.info("Showdown");
        final List<Pot> collapsed = Pots.collapsePots(pots);
        final List<Candidate> winners = Pots.determineWinners(collapsed, cards, buttonAt);

        for (Candidate winner : winners) {
            final Seat seat = winner.getSeat();
            int seatIndex = seat.getSeatIndex();
            int amount = winner.getAmount();
            winner.getSeat().setStack(seat.getStack() + amount);
            logger.info("{} at seat #{} won {}", seat, seatIndex, amount);
        }

        /*final IHandProcessor handProcessor = new TexasHoldemProcessor();

        final List<SeatHand> seatHands = new ArrayList<>();

        for (Seat seat : seats) {
            long hand = cards | seat.getCards();
            seatHands.add(new SeatHand(seat, hand, HandEvaluatorUtil.evaluate(handProcessor, hand), null));
        }

        seatHands.sort((e1, e2) -> {
            try {
                return HandEvaluatorUtil.compare(handProcessor, e1.hand, e2.hand);
            } catch (HandEvaluatorException e) {
                throw new RuntimeException(e);
            }
        });

        Collections.reverse(seatHands);

        // TODO: Group winners
        List<String> orderedGroupIds = new ArrayList<>();

        for (SeatHand seatHand : seatHands) {
            final StringBuilder groupIdBuilder = new StringBuilder();
            IEvaluation evaluation = seatHand.evaluation;
            final long handType = evaluation.getHandType();
            groupIdBuilder.append(String.format("handType=%d", handType));
            final int[] kickers = evaluation.getKickers();
            if (kickers.length > 0) {
                groupIdBuilder.append("&kickers=");
                for (int i = 0; i < kickers.length; i += 1) {
                    int kicker = kickers[i];
                    groupIdBuilder.append(kicker);
                    if (i < kickers.length - 1) {
                        groupIdBuilder.append(",");
                    }
                }
            }

            final String groupId = groupIdBuilder.toString();
            seatHand.groupId = groupId;

            if (orderedGroupIds.isEmpty() || !orderedGroupIds.get(orderedGroupIds.size() - 1).equals(groupId)) {
                orderedGroupIds.add(groupId);
            }
        }

        final Map<String, List<SeatHand>> groupedWinners = seatHands.stream().collect(Collectors.groupingBy(item -> item.groupId));

        for (String groupId : orderedGroupIds) {
            logger.info("Winner groupId: {}", groupId);
            List<SeatHand> winners = groupedWinners.get(groupId);
            for (SeatHand winner : winners) {
                logger.info("  Winner : {}", winner.seat.getPlayer());
            }
        }*/
    }

    public void gameLoop() {
        // PLAYER_JOIN_ACTION
        // PLAYER_JOIN_ACTION

        // DEAL_CARDS (two cards to each player)
            // DEAL_CARD (Player 1)
            // DEAL_CARD (Player 2)
            // DEAL_CARD (Player 1)
            // DEAL_CARD (Player 2)

        // PLAYER_ACTION ...
        // FLOP
        // PLAYER_ACTION ...
        // TURN
        // PLAYER_ACTION ...
        // RIVER
        // PLAYER_ACTION ...
        // RESOLVE_POTS
    }
}
