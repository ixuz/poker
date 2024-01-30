package com.antwika.fsm.state;

import com.antwika.table.data.TableData;

public class WinningsDeliveredState extends FSMState {
    public WinningsDeliveredState() {
        super("WinningsDeliveredState");
    }

    @Override
    protected void onEnter(Object data) {
        final TableData tableData = (TableData) data;
    }

    @Override
    protected void onExit(Object data) {

    }

    @Override
    protected void onStep(Object data) {

    }
}
