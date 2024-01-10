package com.antwika.table.handler.player;

import com.antwika.table.data.TableData;
import com.antwika.table.data.SeatData;
import com.antwika.table.event.IEvent;
import com.antwika.table.event.player.PlayerJoinRequest;
import com.antwika.table.handler.IHandler;
import com.antwika.table.player.Player;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PlayerJoinRequestHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(PlayerJoinRequestHandler.class);

    public boolean canHandle(IEvent event) {
        return event instanceof PlayerJoinRequest;
    }

    public List<IEvent> handle(IEvent event) {
        if (!canHandle(event)) throw new RuntimeException("Can't handle this type of event");
        final PlayerJoinRequest playerJoinRequest = (PlayerJoinRequest) event;

        final TableData tableData = playerJoinRequest.getTableData();
        final SeatData seatData = playerJoinRequest.getSeatData();
        final Player player = playerJoinRequest.getPlayer();
        final int amount = playerJoinRequest.getAmount();

        final boolean seated = TableUtil.seat(tableData, player, seatData.getSeatIndex(), amount);

        if (seated) {
            logger.info("{}: joined the game at seat #{}", player.getPlayerData().getPlayerName(), seatData.getSeatIndex() + 1);
        }

        return null;
    }
}
