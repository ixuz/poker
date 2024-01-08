package com.antwika.game.player;

import com.antwika.game.common.Prng;
import com.antwika.game.data.PlayerData;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.EventHandler;
import com.antwika.game.event.PlayerActionRequest;
import lombok.Getter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@ToString(onlyExplicitlyIncluded = true)
public abstract class Player extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(Player.class);

    private final PlayerData playerData;

    public Player(long prngSeed, String playerName) {
        super("Player", 1000L);
        this.playerData = new PlayerData(playerName, new Prng(prngSeed));
    }

    @Override
    public synchronized void handle(IEvent event) {
        if (event instanceof PlayerActionRequest e) {
            onPlayerActionRequest(e);
        }
    }

    public static IEvent handleEvent(IEvent event) {
        if (event instanceof PlayerActionRequest e) {
            final Player player = e.getPlayer();
            return player.onPlayerActionRequest(e);
        }

        return null;
    }

    protected abstract IEvent onPlayerActionRequest(PlayerActionRequest event);
}
