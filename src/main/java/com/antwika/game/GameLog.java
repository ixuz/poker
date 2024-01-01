package com.antwika.game;

import com.antwika.common.exception.NotationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class GameLog {
    private static final Logger logger = LoggerFactory.getLogger(GameLog.class);

    public static void printGameInfo(Game game) {
        logger.info("Poker Hand #{}: {} ({}/{}) - {}",
                game.getHandId(),
                game.getGameType(),
                game.getSmallBlind(),
                game.getBigBlind(),
                new Date());
    }

    public static void printTableInfo(Game game) {
        logger.info("Table '{}' {}-max Seat #{} is the button",
                game.getTableName(),
                game.getSeats().size(),
                game.getButtonAt() + 1);
    }

    public static void printTableSeatsInfo(Game game) {
        for (Seat seat : game.getSeats()) {
            if (seat.getPlayer() == null) continue;

            logger.info("Seat {}: {} ({} in chips) ",
                    seat.getSeatIndex() + 1,
                    seat.getPlayer().getPlayerName(),
                    seat.getStack());
        }
    }

    public static void printTableSeatCardsInfo(Game game) throws NotationException {
        for (Seat seat : game.getSeats()) {
            if (seat.getPlayer() == null) continue;

            final long cards = seat.getCards();

            if (Long.bitCount(cards) != 2) throw new RuntimeException("Unexpected number of cards after deal");

            logger.info("Dealt to {} [{}]", seat.getPlayer().getPlayerName(), GameUtil.toNotation(seat.getCards()));
        }
    }

    public static void printSummary(Game game) throws NotationException {
        logger.info("*** SUMMARY ***");
        logger.info("Total pot {} | Rake {}", game.getDelivered(), 0);
        logger.info("Board [{}]", GameUtil.toNotation(game.getCards()));

        int chipsInPlay = 0;
        for (Seat seat : game.getSeats()) {
            if (seat.getPlayer() == null) continue;

            chipsInPlay += seat.getStack();

            final Player player = seat.getPlayer();

            logger.info("Seat {}: {} stack {}",
                    seat.getSeatIndex(),
                    player.getPlayerName(),
                    seat.getStack());
        }
        logger.info("Total chips in play {}", chipsInPlay);
    }
}
