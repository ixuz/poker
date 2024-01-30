package com.antwika.fsm.state;

import com.antwika.table.data.TableData;
public class OrbitEndedState extends FSMState {
    public OrbitEndedState() {
        super("OrbitEndedState");
    }

    @Override
    protected void onEnter(Object data) {

    }

    @Override
    protected void onExit(Object data) {
        final TableData tableData = (TableData) data;
    }

    @Override
    protected void onStep(Object data) {

    }
}
