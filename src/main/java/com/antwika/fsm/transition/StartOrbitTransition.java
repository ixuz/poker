package com.antwika.fsm.transition;

import com.antwika.fsm.state.FSMState;
import com.antwika.table.data.SeatData;
import com.antwika.table.data.TableData;
import com.antwika.table.event.IEvent;
import com.antwika.table.event.player.PlayerActionRequest;
import com.antwika.table.player.Player;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class StartOrbitTransition extends Transition {
    private static final Logger logger = LoggerFactory.getLogger(StartOrbitTransition.class);

    public StartOrbitTransition(FSMState fromState, FSMState toState) {
        super("StartOrbitTransition", fromState, toState);
    }

    @Override
    public boolean checkCondition(Object data) {
        final TableData tableData = (TableData) data;

        if (TableUtil.countPlayersRemainingInHand(tableData) < 2) return false;

        final TableData.GameStage gameStage = tableData.getGameStage();

        return switch (gameStage) {
            case PREFLOP, FLOP, TURN, RIVER -> true;
            default -> false;
        };
    }

    @Override
    public void onTransition(Object data) {
        try {
            final TableData tableData = (TableData) data;
            final List<SeatData> seats = tableData.getSeats();
            final int actionAt = tableData.getActionAt();

            final SeatData seat = seats.get(actionAt);

            final SeatData nextSeatToAct = TableUtil.findNextSeatToAct(tableData, actionAt, 0, true);

            if (seat == null) {
                throw new RuntimeException("Unexpected that the seat with action is null");
            }

            if (nextSeatToAct == null) {
                throw new RuntimeException("Unexpected that the nextSeatToAct is null");
            }

            if (seat.isHasFolded()) {
                seat.setHasActed(true);
                tableData.setActionAt(nextSeatToAct.getSeatIndex());
                // return List.of(new OrbitActionRequest(tableData)); // TODO: Request player action
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
                // return List.of(new OrbitEndRequest(tableData)); // TODO: End orbit transition
            }

            // TODO: Weird should be async, no?
            final IEvent response = Player.handleEvent(new PlayerActionRequest(player, tableData, totalBet, toCall, minBet, smallestValidRaise));

            if (response != null) {
                // return List.of(response); // TODO: Weird
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
