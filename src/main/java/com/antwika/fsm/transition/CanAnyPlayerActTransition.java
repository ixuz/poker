package com.antwika.fsm.transition;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.fsm.state.FSMState;
import com.antwika.table.data.DeckData;
import com.antwika.table.data.SeatData;
import com.antwika.table.data.TableData;
import com.antwika.table.util.DeckUtil;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class CanAnyPlayerActTransition extends Transition {
    private static final Logger logger = LoggerFactory.getLogger(CanAnyPlayerActTransition.class);

    public CanAnyPlayerActTransition(FSMState fromState, FSMState toState) {
        super("CanAnyPlayerActTransition", fromState, toState);
    }

    @Override
    public boolean checkCondition(Object data) {
        final TableData tableData = (TableData) data;

        if (TableUtil.hasAllPlayersActed(tableData)) return false;

        return true;
    }

    @Override
    public void onTransition(Object data) {
        final TableData tableData = (TableData) data;
    }
}
