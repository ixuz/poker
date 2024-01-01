package com.antwika.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

@ToString(onlyExplicitlyIncluded = true)
public class Player extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(Player.class);

    @ToString.Include
    @Getter
    private final String playerName;

    @Getter
    private Prng prng;

    public Player(long prngSeed, String playerName) {
        this.prng = new Prng(prngSeed);
        this.playerName = playerName;
    }

    @Override
    public synchronized Event handle(Event event) {
        if (event instanceof PlayerActionRequest e) {
            return onPlayerActionRequest(e);
        }

        return null;
    }

    private Event onPlayerActionRequest(PlayerActionRequest event) {
        if (event.getPlayer() != this) return null;
        logger.debug("event: { toCall: {}, minBet: {}, minRaise: {} }", event.getToCall(), event.getMinBet(), event.getMinRaise());

        int rand = prng.nextInt(100);

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
        int lastRaise = game.getLastRaise();
        int totalPot = GameUtil.countTotalPotAndCommitted(game);
        int deadMoney = totalPot - lastRaise;
        int desiredBet = (int) ((lastRaise * 3 + deadMoney) * betSizePercent);
        int minimumBet = Math.min(seat.getStack(), game.getBigBlind());
        int bet = Math.max(desiredBet, minimumBet);
        return Math.min(seat.getStack(), bet);
    }
}
