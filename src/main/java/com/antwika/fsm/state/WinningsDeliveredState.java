package com.antwika.fsm.state;

import com.antwika.common.exception.NotationException;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WinningsDeliveredState extends FSMState {
    private static final Logger logger = LoggerFactory.getLogger(WinningsDeliveredState.class);

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
