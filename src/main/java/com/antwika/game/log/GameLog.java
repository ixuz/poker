package com.antwika.game.log;

import com.antwika.common.exception.NotationException;
import com.antwika.game.data.GameData;
import com.antwika.game.player.Player;
import com.antwika.game.data.SeatData;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class GameLog {
    private static final Logger logger = LoggerFactory.getLogger(GameLog.class);

    public static void printGameInfo(GameData gameData) {
        logger.info("Poker Hand #{}: {} ({}/{}) - {}",
                gameData.getHandId(),
                gameData.getGameType(),
                gameData.getSmallBlind(),
                gameData.getBigBlind(),
                new Date());
    }

    public static void printTableInfo(GameData gameData) {
        logger.info("Table '{}' {}-max Seat #{} is the button",
                gameData.getTableName(),
                gameData.getSeats().size(),
                gameData.getButtonAt() + 1);
    }

    public static void printTableSeatsInfo(GameData gameData) {
        for (SeatData seat : gameData.getSeats()) {
            if (seat.getPlayer() == null) continue;

            logger.info("Seat {}: {} ({} in chips) ",
                    seat.getSeatIndex() + 1,
                    seat.getPlayer().getPlayerData().getPlayerName(),
                    seat.getStack());
        }
    }

    public static void printTableSeatCardsInfo(GameData gameData) throws NotationException {
        for (SeatData seat : gameData.getSeats()) {
            if (seat.getPlayer() == null) continue;

            final long cards = seat.getCards();

            if (Long.bitCount(cards) != 2) throw new RuntimeException("Unexpected number of cards after deal");

            logger.info("Dealt to {} [{}]", seat.getPlayer().getPlayerData().getPlayerName(), GameDataUtil.toNotation(seat.getCards()));
        }
    }

    public static void printSummary(GameData gameData) throws NotationException {
        logger.info("*** SUMMARY ***");
        logger.info("Total pot {} | Rake {}", gameData.getDelivered(), 0);
        logger.info("Board [{}]", GameDataUtil.toNotation(gameData.getCards()));

        int chipsInPlay = 0;
        for (SeatData seat : gameData.getSeats()) {
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
