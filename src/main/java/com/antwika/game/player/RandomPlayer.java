package com.antwika.game.player;

import com.antwika.game.data.GameData;
import com.antwika.game.data.SeatData;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.PlayerActionRequest;
import com.antwika.game.event.PlayerActionResponse;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomPlayer extends Player {
    private static final Logger logger = LoggerFactory.getLogger(RandomPlayer.class);

    public RandomPlayer(long prngSeed, String playerName) {
        super(prngSeed, playerName);
    }

    @Override
    protected IEvent onPlayerActionRequest(PlayerActionRequest event) {
        if (event.getPlayer() != this) return null;
        logger.debug("event: { toCall: {}, minBet: {}, minRaise: {} }", event.getToCall(), event.getMinBet(), event.getMinRaise());

        int rand = getPlayerData().getPrng().nextInt(100);

        final GameData gameData = event.getGameData();
        final SeatData seat = GameDataUtil.getSeat(gameData, this);
        boolean canRaise = event.getToCall() < seat.getStack();

        if (event.getToCall() == 0) {
            if (rand < 75) {
                return new PlayerActionResponse(this, gameData, PlayerActionResponse.Type.CHECK, 0);
            } else {
                return new PlayerActionResponse(this, gameData, PlayerActionResponse.Type.BET, GameDataUtil.calcBetSize(gameData, this, 0.75f));
            }
        } else {
            if (rand < 50) {
                return new PlayerActionResponse(this, gameData, PlayerActionResponse.Type.FOLD, 0);
            } else if (canRaise && rand < 75) {
                return new PlayerActionResponse(this, gameData, PlayerActionResponse.Type.RAISE, GameDataUtil.calcBetSize(gameData, this, 0.75f));
            } else {
                return new PlayerActionResponse(this, gameData, PlayerActionResponse.Type.CALL, event.getToCall());
            }
        }
    }

    @Override
    protected void preEventHandle() {

    }

    @Override
    protected void noEventHandle() {

    }
}
