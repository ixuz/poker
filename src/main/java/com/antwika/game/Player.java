package com.antwika.game;

import com.antwika.game.common.Prng;
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

        final Game game = event.getGame();
        final Seat seat = GameUtil.getSeat(game, this);
        boolean canRaise = event.getToCall() < seat.getStack();

        if (event.getToCall() == 0) {
            if (rand < 75) {
                return new PlayerActionResponse(this, game, PlayerActionResponse.Type.CHECK, 0);
            } else {
                return new PlayerActionResponse(this, game, PlayerActionResponse.Type.BET, calcBetSize(game, this, 0.75f));
            }
        } else {
            if (rand < 50) {
                return new PlayerActionResponse(this, game, PlayerActionResponse.Type.FOLD, 0);
            } else if (canRaise && rand < 75) {
                return new PlayerActionResponse(this, game, PlayerActionResponse.Type.RAISE, calcBetSize(game, this, 0.75f));
            } else {
                return new PlayerActionResponse(this, game, PlayerActionResponse.Type.CALL, event.getToCall());
            }
        }
    }

    public static int calcBetSize(Game game, Player player, float betSizePercent) {
        final Seat seat = GameUtil.getSeat(game, player);
        int lastRaise = game.getGameData().getLastRaise();
        int totalPot = GameUtil.countTotalPotAndCommitted(game);
        int deadMoney = totalPot - lastRaise;
        int desiredBet = (int) ((lastRaise * 3 + deadMoney) * betSizePercent);
        int minimumBet = Math.min(seat.getStack(), game.getGameData().getBigBlind());
        int bet = Math.max(desiredBet, minimumBet);
        return Math.min(seat.getStack(), bet);
    }
}
