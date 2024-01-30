package com.antwika.fsm.state;

import com.antwika.handhistory.line.HandEndLine;
import com.antwika.table.data.TableData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandEndedState extends FSMState {
    private static final Logger logger = LoggerFactory.getLogger(HandEndedState.class);

    public HandEndedState() {
        super("HandEndedState");
    }

    @Override
    protected void onEnter(Object data) {
        logger.info("--- HAND END ---");
        final TableData tableData = (TableData) data;
        tableData.getHistory().add(new HandEndLine());

        // TODO: Write the hand history to external store.
        // TODO: Prepare for next hand, by clearing the hand history.
    }

    @Override
    protected void onExit(Object data) {

    }

    @Override
    protected void onStep(Object data) {

    }
}
