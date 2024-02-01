package com.antwika.fsm.state;

import com.antwika.handhistory.HandProcessor;
import com.antwika.handhistory.helper.LineApplierFactory;
import com.antwika.handhistory.helper.LineParserFactory;
import com.antwika.handhistory.line.HandEndLine;
import com.antwika.table.data.TableData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
        tableData.setActionAt(-1);
        tableData.setTotalBet(0);
        tableData.setLastRaise(0);

        // Serialize, parse, verify equality
        final var processor = new HandProcessor(LineParserFactory.createTexasHoldemLineParser(), LineApplierFactory.createTexasHoldemLineApplier());
        final var baos = new ByteArrayOutputStream();
        try {
            processor.write(tableData, baos);
            final var serialized = baos.toString();
            final var parsedTableData = processor.process(serialized);
            if (!parsedTableData.equals(tableData)) {
                throw new RuntimeException("Detected inequality when tableData was first serialized and then deserialized!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
