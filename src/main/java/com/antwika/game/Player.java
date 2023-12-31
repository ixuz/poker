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

    private final Random prng;

    public Player(long prngSeed, String playerName) {
        this.prng = new Random(prngSeed);
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
        final Seat seat = game.getSeat(this);
        final int totalPot = game.getTotalPot(true);
        boolean canRaise = event.getToCall() < seat.getStack();

        if (event.getToCall() == 0) {
            if (rand < 75) {
                return new PlayerActionResponse(this, game, "CHECK", 0);
            } else {
                int lastRaise = game.getLastRaise();
                int deadMoney = totalPot - lastRaise;
                int myFullPotSizeRaise = lastRaise * 3 + deadMoney;
                int myStack = seat.getStack();
                int myCommitted = seat.getCommitted();
                float myBetSizePercentage = 0.5f;
                int myRaise = (int) (myFullPotSizeRaise * myBetSizePercentage);
                int actualRaise = Math.min(myRaise, myStack);
                int actualBet = myCommitted + actualRaise;

                return new PlayerActionResponse(this, game, "BET", actualBet);
            }
        } else {
            if (rand < 50) {
                return new PlayerActionResponse(this, game, "FOLD", 0);
            } else if (canRaise && rand < 75) {
                int lastRaise = game.getLastRaise();
                int deadMoney = totalPot - lastRaise;
                int myFullPotSizeRaise = lastRaise * 3 + deadMoney;
                int myStack = seat.getStack();
                float myBetSizePercentage = 0.5f;
                int myRaise = (int) (myFullPotSizeRaise * myBetSizePercentage);
                int actualRaise = Math.min(myRaise, myStack);

                int finalRaise = Math.max(actualRaise, event.getMinRaise());

                return new PlayerActionResponse(this, game, "RAISE", finalRaise);
            } else {
                return new PlayerActionResponse(this, game, "CALL", event.getToCall());
            }
        }

        // return new PlayerActionResponse(this, event.game, "BET", event.minBet);

        // return new PlayerActionResponse(this, event.game, "CALL", event.toCall);
    }
}
