package com.antwika.table.handler.orbit;

import com.antwika.table.data.TableData;
import com.antwika.table.data.SeatData;
import com.antwika.table.event.*;
import com.antwika.table.event.orbit.OrbitActionRequest;
import com.antwika.table.event.orbit.OrbitEndRequest;
import com.antwika.table.event.player.PlayerActionRequest;
import com.antwika.table.handler.IHandler;
import com.antwika.table.player.Player;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class OrbitActionRequestHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(OrbitActionRequestHandler.class);

    public boolean canHandle(IEvent event) {
        if (!(event instanceof OrbitActionRequest orbitActionRequest)) return false;

        final TableData.GameStage gameStage = orbitActionRequest.getTableData().getGameStage();

        return switch (gameStage) {
            case PREFLOP, FLOP, TURN, RIVER -> true;
            default -> false;
        };
    }

    public List<IEvent> handle(IEvent event) {
        try {
            final OrbitActionRequest orbitActionRequest = (OrbitActionRequest) event;
            final TableData tableData = orbitActionRequest.getTableData();

            if (TableUtil.countPlayersRemainingInHand(tableData) == 1) {
                logger.debug("All but one player has folded, hand must end");
                return List.of(new OrbitEndRequest(tableData));
            }

            if (TableUtil.getNumberOfPlayersLeftToAct(tableData) < 1) {
                return List.of(new OrbitEndRequest(tableData));
            }

            final List<SeatData> seats = tableData.getSeats();
            final int actionAt = tableData.getActionAt();

            final SeatData seat = seats.get(actionAt);

            final SeatData seatAfter = TableUtil.findNextSeatToAct(tableData, actionAt, 0, true);
            if (seat == null) {
                return List.of(new OrbitEndRequest(tableData));
            }

            if (seatAfter == null) {
                return List.of(new OrbitEndRequest(tableData));
            }

            if (seat.isHasFolded()) {
                seat.setHasActed(true);
                tableData.setActionAt(seatAfter.getSeatIndex());
                return List.of(new OrbitActionRequest(tableData));
            }

            final Player player = seat.getPlayer();

            final int totalBet = tableData.getTotalBet();
            final int bigBlind = tableData.getBigBlind();
            final int lastRaise = tableData.getLastRaise();
            final int toCall = Math.min(seat.getStack(), totalBet - seat.getCommitted());
            final int minRaise = Math.min(seat.getStack(), Math.max(lastRaise, bigBlind));
            final int minBet = totalBet + minRaise - seat.getCommitted();
            final int smallestValidRaise = Math.min(totalBet + bigBlind, seat.getStack());

            if (seat.getStack() == 0) {
                return List.of(new OrbitEndRequest(tableData));
            }

            final IEvent response = Player.handleEvent(new PlayerActionRequest(player, tableData, totalBet, toCall, minBet, smallestValidRaise));

            if (response != null) {
                return List.of(response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
