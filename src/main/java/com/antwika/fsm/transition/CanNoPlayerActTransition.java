package com.antwika.fsm.transition;

import com.antwika.fsm.state.FSMState;
import com.antwika.table.data.SeatData;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CanNoPlayerActTransition extends Transition {
    private static final Logger logger = LoggerFactory.getLogger(CanNoPlayerActTransition.class);

    public CanNoPlayerActTransition(FSMState fromState, FSMState toState) {
        super("CanAnyPlayerActTransition", fromState, toState);
    }

    private boolean _checkCondition(Object data) {
        final TableData tableData = (TableData) data;

        final int withStackNotFoldedAndNotActedYetCount = tableData.getSeats().stream()
                .filter(seat -> seat.getStack() > 0)
                .filter(seat -> !seat.isHasFolded())
                .filter(seat -> !seat.isHasActed())
                .toList()
                .size();

        final int withStackNotFoldedCount = tableData.getSeats().stream()
                .filter(seat -> seat.getStack() > 0)
                .filter(seat -> !seat.isHasFolded())
                .toList()
                .size();

        final SeatData currentSeat = tableData.getSeats().get(tableData.getActionAt());

        int highestCommit = TableUtil.findHighestCommit(tableData);
        if (currentSeat.getStack() > 0 && !currentSeat.isHasFolded() && !currentSeat.isHasActed() && withStackNotFoldedCount > 1) {
            return true;
        }

        if (currentSeat.getStack() > 0 && !currentSeat.isHasFolded() && currentSeat.getCommitted() != highestCommit) {
            return true;
        }

        if (withStackNotFoldedAndNotActedYetCount < 2) return false;

        final int numberOfPlayersWithNonFoldedAndNonZeroStack = TableUtil.numberOfPlayersWithNonFoldedAndNonZeroStack(tableData);
        final int countPlayersRemainingInHand = TableUtil.countPlayersRemainingInHand(tableData);

        if (numberOfPlayersWithNonFoldedAndNonZeroStack < 2 && countPlayersRemainingInHand < 2) {
            return false;
        }

        return true;
    }

    @Override
    public boolean checkCondition(Object data) {
        return !_checkCondition(data);
    }

    @Override
    public void onTransition(Object data) {
        final TableData tableData = (TableData) data;
    }
}
