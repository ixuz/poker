package com.antwika.game.player;

import com.antwika.game.common.Prng;
import com.antwika.game.data.GameData;
import com.antwika.game.data.PlayerData;
import com.antwika.game.data.Seat;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.EventHandler;
import com.antwika.game.event.PlayerActionRequest;
import com.antwika.game.event.PlayerActionResponse;
import com.antwika.game.util.GameUtil;
import lombok.Getter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ToString(onlyExplicitlyIncluded = true)
public class Player extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(Player.class);

    @Getter
    private final PlayerData playerData;

    public Player(long prngSeed, String playerName) {
        this.playerData = new PlayerData(playerName, new Prng(prngSeed));
    }

    @Override
    public synchronized IEvent handle(IEvent event) {
        if (event instanceof PlayerActionRequest e) {
            return onPlayerActionRequest(e);
        }

        return null;
    }

    private IEvent onPlayerActionRequest(PlayerActionRequest event) {
        if (event.getPlayer() != this) return null;
        logger.debug("event: { toCall: {}, minBet: {}, minRaise: {} }", event.getToCall(), event.getMinBet(), event.getMinRaise());

        int rand = getPlayerData().getPrng().nextInt(100);

        final GameData gameData = event.getGameData();
        final Seat seat = GameUtil.getSeat(gameData, this);
        boolean canRaise = event.getToCall() < seat.getStack();

        if (event.getToCall() == 0) {
            if (rand < 75) {
                return new PlayerActionResponse(this, gameData, PlayerActionResponse.Type.CHECK, 0);
            } else {
                return new PlayerActionResponse(this, gameData, PlayerActionResponse.Type.BET, calcBetSize(gameData, this, 0.75f));
            }
        } else {
            if (rand < 50) {
                return new PlayerActionResponse(this, gameData, PlayerActionResponse.Type.FOLD, 0);
            } else if (canRaise && rand < 75) {
                return new PlayerActionResponse(this, gameData, PlayerActionResponse.Type.RAISE, calcBetSize(gameData, this, 0.75f));
            } else {
                return new PlayerActionResponse(this, gameData, PlayerActionResponse.Type.CALL, event.getToCall());
            }
        }
    }

    public static int calcBetSize(GameData gameData, Player player, float betSizePercent) {
        final Seat seat = GameUtil.getSeat(gameData, player);
        int lastRaise = gameData.getLastRaise();
        int totalPot = GameUtil.countTotalPotAndCommitted(gameData);
        int deadMoney = totalPot - lastRaise;
        int desiredBet = (int) ((lastRaise * 3 + deadMoney) * betSizePercent);
        int minimumBet = Math.min(seat.getStack(), gameData.getBigBlind());
        int bet = Math.max(desiredBet, minimumBet);
        return Math.min(seat.getStack(), bet);
    }
}
