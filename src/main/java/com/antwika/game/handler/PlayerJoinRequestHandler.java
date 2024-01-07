package com.antwika.game.handler;

import com.antwika.game.data.GameData;
import com.antwika.game.data.SeatData;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.PlayerJoinEvent;
import com.antwika.game.event.PlayerJoinRequestEvent;
import com.antwika.game.player.Player;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerJoinRequestHandler implements IActionHandler {
    private static final Logger logger = LoggerFactory.getLogger(PlayerJoinRequestHandler.class);

    public boolean canHandle(IEvent event) {
        return event instanceof PlayerJoinRequestEvent;
    }

    public void handle(IEvent event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");
        final PlayerJoinRequestEvent playerJoinRequestEvent = (PlayerJoinRequestEvent) event;

        final GameData gameData = playerJoinRequestEvent.getGameData();
        final SeatData seatData = playerJoinRequestEvent.getSeatData();
        final Player player = playerJoinRequestEvent.getPlayer();
        final int amount = playerJoinRequestEvent.getAmount();

        GameDataUtil.seat(gameData, player, seatData.getSeatIndex(), amount);
    }
}
