package com.antwika.fsm.state;

import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrbitEndedState extends FSMState {
    private static final Logger logger = LoggerFactory.getLogger(OrbitEndedState.class);

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
