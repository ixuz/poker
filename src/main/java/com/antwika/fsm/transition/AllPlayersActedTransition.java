package com.antwika.fsm.transition;

import com.antwika.common.exception.NotationException;
import com.antwika.fsm.state.FSMState;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllPlayersActedTransition extends Transition {
    private static final Logger logger = LoggerFactory.getLogger(AllPlayersActedTransition.class);

    public AllPlayersActedTransition(FSMState fromState, FSMState toState) {
        super("AllPlayersActedTransition", fromState, toState);
    }

    @Override
    public boolean checkCondition(Object data) {
        final TableData tableData = (TableData) data;

        if (TableUtil.hasAllPlayersActed(tableData)) return true;

        return false;
    }

    @Override
    public void onTransition(Object data) {
        final TableData tableData = (TableData) data;
    }
}
