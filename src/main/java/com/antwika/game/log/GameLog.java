package com.antwika.game.log;

import com.antwika.common.exception.NotationException;
import com.antwika.game.Game;
import com.antwika.game.player.Player;
import com.antwika.game.data.Seat;
import com.antwika.game.util.GameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class GameLog {
    private static final Logger logger = LoggerFactory.getLogger(GameLog.class);

    public static void printGameInfo(Game game) {
        logger.info("Poker Hand #{}: {} ({}/{}) - {}",
                game.getGameData().getHandId(),
                game.getGameData().getGameType(),
                game.getGameData().getSmallBlind(),
                game.getGameData().getBigBlind(),
                new Date());
    }

    public static void printTableInfo(Game game) {
        logger.info("Table '{}' {}-max Seat #{} is the button",
                game.getGameData().getTableName(),
                game.getGameData().getSeats().size(),
                game.getGameData().getButtonAt() + 1);
    }

    public static void printTableSeatsInfo(Game game) {
        for (Seat seat : game.getGameData().getSeats()) {
            if (seat.getPlayer() == null) continue;

            logger.info("Seat {}: {} ({} in chips) ",
                    seat.getSeatIndex() + 1,
                    seat.getPlayer().getPlayerData().getPlayerName(),
                    seat.getStack());
        }
    }

    public static void printTableSeatCardsInfo(Game game) throws NotationException {
        for (Seat seat : game.getGameData().getSeats()) {
            if (seat.getPlayer() == null) continue;

            final long cards = seat.getCards();

            if (Long.bitCount(cards) != 2) throw new RuntimeException("Unexpected number of cards after deal");

            logger.info("Dealt to {} [{}]", seat.getPlayer().getPlayerData().getPlayerName(), GameUtil.toNotation(seat.getCards()));
        }
    }

    public static void printSummary(Game game) throws NotationException {
        logger.info("*** SUMMARY ***");
        logger.info("Total pot {} | Rake {}", game.getGameData().getDelivered(), 0);
        logger.info("Board [{}]", GameUtil.toNotation(game.getGameData().getCards()));

        int chipsInPlay = 0;
        for (Seat seat : game.getGameData().getSeats()) {
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
}
