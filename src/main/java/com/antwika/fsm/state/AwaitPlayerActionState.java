package com.antwika.fsm.state;

import com.antwika.table.data.SeatData;
import com.antwika.table.data.TableData;
import com.antwika.table.event.IEvent;
import com.antwika.table.event.orbit.OrbitActionRequest;
import com.antwika.table.event.orbit.OrbitEndRequest;
import com.antwika.table.event.player.PlayerActionRequest;
import com.antwika.table.event.player.PlayerActionResponse;
import com.antwika.table.player.Player;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AwaitPlayerActionState extends FSMState {
    private static final Logger logger = LoggerFactory.getLogger(AwaitPlayerActionState.class);

    public AwaitPlayerActionState() {
        super("AwaitPlayerActionState");
    }

    @Override
    protected void onEnter(Object data) {

    }

    @Override
    protected void onExit(Object data) {
        final TableData tableData = (TableData) data;

        final SeatData nextSeatToAct = TableUtil.findNextSeatToAct(tableData, tableData.getActionAt(), 0, true);
        if (nextSeatToAct != null) {
            tableData.setActionAt(nextSeatToAct.getSeatIndex());
        }
    }

    @Override
    protected void onStep(Object data) {
        final TableData tableData = (TableData) data;

        final SeatData seat = tableData.getSeats().get(tableData.getActionAt());
        final Player player = seat.getPlayer();
        // logger.info("Action now at: {}", player.getPlayerData().getPlayerName());

        final int totalBet = tableData.getTotalBet();
        final int bigBlind = tableData.getBigBlind();
        final int lastRaise = tableData.getLastRaise();
        final int toCall = Math.min(seat.getStack(), totalBet - seat.getCommitted());
        final int minRaise = Math.min(seat.getStack(), Math.max(lastRaise, bigBlind));
        final int minBet = totalBet + minRaise - seat.getCommitted();
        final int smallestValidRaise = Math.min(totalBet + bigBlind, seat.getStack());

        final PlayerActionResponse playerActionResponse = (PlayerActionResponse) Player.handleEvent(new PlayerActionRequest(player, tableData, totalBet, toCall, minBet, smallestValidRaise));

        List<IEvent> additionalEvents = null;

        if (playerActionResponse.action.equals(PlayerActionResponse.Type.BET)) {
            additionalEvents = handleBetAction(playerActionResponse);
        } else if (playerActionResponse.action.equals(PlayerActionResponse.Type.CALL)) {
            additionalEvents = handleCall(playerActionResponse);
        } else if (playerActionResponse.action.equals(PlayerActionResponse.Type.CHECK)) {
            additionalEvents = handleCheck(playerActionResponse);
        } else if (playerActionResponse.action.equals(PlayerActionResponse.Type.FOLD)) {
            additionalEvents = handleFold(playerActionResponse);
        } else if (playerActionResponse.action.equals(PlayerActionResponse.Type.RAISE)) {
            additionalEvents = handleRaise(playerActionResponse);
        }
    }

    private List<IEvent> handleBetAction(IEvent event) {
        if (!(event instanceof PlayerActionResponse)) throw new RuntimeException("Can't handle this type of event");

        final PlayerActionResponse playerActionResponse = (PlayerActionResponse) event;

        final TableData tableData = playerActionResponse.getTableData();
        final SeatData seat = TableUtil.getSeat(tableData, playerActionResponse.player);
        if (playerActionResponse.amount > seat.getStack()) {
            throw new RuntimeException("Player can not bet a greater amount than his remaining stack!");
        }
        if (playerActionResponse.amount == 0) {
            throw new RuntimeException("Player can not bet a zero amount!");
        }

        TableUtil.commit(seat, playerActionResponse.amount);
        tableData.setTotalBet(seat.getCommitted());
        tableData.setLastRaise(playerActionResponse.amount);
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: bets %d", seat.getPlayer().getPlayerData().getPlayerName(), playerActionResponse.amount));
        if (seat.getStack() == 0) {
            sb.append(" and is all-in");
        }
        logger.info(sb.toString());
        seat.setHasActed(true);

        if (TableUtil.hasAllPlayersActed(tableData)) {
            return List.of(new OrbitEndRequest(tableData));
        }

        final SeatData theNextSeat = TableUtil.findNextSeatToAct(tableData, tableData.getActionAt(), 0, true);
        if (theNextSeat == null) {
            return List.of(new OrbitEndRequest(tableData));
        }

        // tableData.setActionAt(theNextSeat.getSeatIndex());

        return List.of(new OrbitActionRequest(tableData));
    }

    public List<IEvent> handleCall(IEvent event) {
        if (!(event instanceof PlayerActionResponse)) throw new RuntimeException("Can't handle this type of event");

        final PlayerActionResponse action = (PlayerActionResponse) event;

        final TableData tableData = action.getTableData();
        final SeatData seat = TableUtil.getSeat(tableData, action.player);
        if (action.amount > seat.getStack()) {
            throw new RuntimeException("Player can not call a greater amount than his remaining stack!");
        }

        TableUtil.commit(seat, action.amount);
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: calls %d", seat.getPlayer().getPlayerData().getPlayerName(), action.amount));
        if (seat.getStack() == 0) {
            sb.append(" and is all-in");
        }
        logger.info(sb.toString());
        seat.setHasActed(true);

        if (TableUtil.hasAllPlayersActed(tableData)) {
            return List.of(new OrbitEndRequest(tableData));
        }

        final SeatData theNextSeat = TableUtil.findNextSeatToAct(tableData, tableData.getActionAt(), 0, true);
        if (theNextSeat == null) {
            return List.of(new OrbitEndRequest(tableData));
        }

        // tableData.setActionAt(theNextSeat.getSeatIndex());

        return List.of(new OrbitActionRequest(tableData));
    }

    private List<IEvent> handleCheck(IEvent event) {
        if (!(event instanceof PlayerActionResponse)) throw new RuntimeException("Can't handle this type of event");

        final PlayerActionResponse action = (PlayerActionResponse) event;

        final TableData tableData = action.getTableData();
        final SeatData seat = TableUtil.getSeat(tableData, action.player);
        seat.setHasActed(true);
        logger.info("{}: checks", seat.getPlayer().getPlayerData().getPlayerName());
        seat.setHasActed(true);

        if (TableUtil.hasAllPlayersActed(tableData)) {
            return List.of(new OrbitEndRequest(tableData));
        }

        final SeatData theNextSeat = TableUtil.findNextSeatToAct(tableData, tableData.getActionAt(), 0, true);
        if (theNextSeat == null) {
            return List.of(new OrbitEndRequest(tableData));
        }

        // tableData.setActionAt(theNextSeat.getSeatIndex());

        return List.of(new OrbitActionRequest(tableData));
    }

    private List<IEvent> handleFold(IEvent event) {
        if (!(event instanceof PlayerActionResponse)) throw new RuntimeException("Can't handle this type of event");

        final PlayerActionResponse action = (PlayerActionResponse) event;

        final TableData tableData = action.getTableData();
        final SeatData seat = TableUtil.getSeat(tableData, action.player);
        seat.setHasFolded(true);
        logger.info("{}: folds", seat.getPlayer().getPlayerData().getPlayerName());
        seat.setHasActed(true);

        if (TableUtil.hasAllPlayersActed(tableData)) {
            return List.of(new OrbitEndRequest(tableData));
        }

        final SeatData theNextSeat = TableUtil.findNextSeatToAct(tableData, tableData.getActionAt(), 0, true);
        if (theNextSeat == null) {
            return List.of(new OrbitEndRequest(tableData));
        }

        // tableData.setActionAt(theNextSeat.getSeatIndex());

        return List.of(new OrbitActionRequest(tableData));
    }

    private List<IEvent> handleRaise(IEvent event) {
        if (!(event instanceof PlayerActionResponse)) throw new RuntimeException("Can't handle this type of event");

        final PlayerActionResponse action = (PlayerActionResponse) event;

        final TableData tableData = action.getTableData();
        final SeatData seat = TableUtil.getSeat(tableData, action.player);
        if (action.amount > seat.getStack()) {
            throw new RuntimeException("Player can not raise a greater amount than his remaining stack!");
        }

        final int smallestValidRaise = Math.min(tableData.getTotalBet() + tableData.getBigBlind(), seat.getStack());
        if (action.amount < smallestValidRaise) {
            throw new RuntimeException("Player must at least raise by one full big blind, or raise all-in for less");
        }

        TableUtil.commit(seat, action.amount);
        if (seat.getCommitted() > tableData.getTotalBet()) {
            tableData.setTotalBet(seat.getCommitted());
            tableData.setLastRaise(action.amount);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s: raises to %d", seat.getPlayer().getPlayerData().getPlayerName(), seat.getCommitted()));
        if (seat.getStack() == 0) {
            sb.append(" and is all-in");
        }
        logger.info(sb.toString());
        seat.setHasActed(true);

        if (TableUtil.hasAllPlayersActed(tableData)) {
            return List.of(new OrbitEndRequest(tableData));
        }

        final SeatData theNextSeat = TableUtil.findNextSeatToAct(tableData, tableData.getActionAt(), 0, true);
        if (theNextSeat == null) {
            return List.of(new OrbitEndRequest(tableData));
        }

        // tableData.setActionAt(theNextSeat.getSeatIndex());

        return List.of(new OrbitActionRequest(tableData));
    }
}
