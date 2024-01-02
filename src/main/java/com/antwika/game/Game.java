package com.antwika.game;

import com.antwika.game.data.GameData;
import com.antwika.game.event.EventHandler;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class Game extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    private final GameData gameData;

    public Game(long prngSeed, String tableName, int seatCount, int smallBlind, int bigBlind) {
        this.gameData = GameDataFactory.createGameData(prngSeed, tableName, seatCount, smallBlind, bigBlind);
    }
}
