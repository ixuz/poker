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
        if (event.player != this) return null;
        logger.debug("event: { toCall: {}, minBet: {}, minRaise: {} }", event.toCall, event.minBet, event.minRaise);

        if (event.toCall == 0) {
            int rand = prng.nextInt(100);
            if (rand < 75) {
                return new PlayerActionResponse(this, event.game, "CHECK", 0);
            } else {
                Seat seat = event.game.getSeat(this);
                int totalPot = event.game.getTotalPot(true);
                int lastRaise = event.game.getLastRaise();
                int deadMoney = totalPot - lastRaise;
                int myFullPotSizeRaise = lastRaise * 3 + deadMoney;
                int myStack = seat.getStack();
                int myCommitted = seat.getCommitted();
                float myBetSizePercentage = 0.5f;
                int myRaise = (int) (myFullPotSizeRaise * myBetSizePercentage);
                int actualRaise = Math.min(myRaise, myStack);
                int actualBet = myCommitted + actualRaise;

                return new PlayerActionResponse(this, event.game, "BET", actualBet);
            }
        } else {
            int rand = prng.nextInt(100);
            if (rand < 75) {
                return new PlayerActionResponse(this, event.game, "CALL", event.toCall);
            } else {
                Seat seat = event.game.getSeat(this);
                int totalPot = event.game.getTotalPot(true);
                int lastRaise = event.game.getLastRaise();
                int deadMoney = totalPot - lastRaise;
                int myFullPotSizeRaise = lastRaise * 3 + deadMoney;
                int myStack = seat.getStack();
                float myBetSizePercentage = 0.5f;
                int myRaise = (int) (myFullPotSizeRaise * myBetSizePercentage);
                int actualRaise = Math.min(myRaise, myStack);

                return new PlayerActionResponse(this, event.game, "RAISE", actualRaise);
            }
        }

        // return new PlayerActionResponse(this, event.game, "BET", event.minBet);

        // return new PlayerActionResponse(this, event.game, "CALL", event.toCall);
    }
}
