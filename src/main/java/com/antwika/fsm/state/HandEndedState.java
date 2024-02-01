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

        // Verify consistency, by serializing, parsing, and checking equality.
        // TODO: Figure out how to deal with prng in state, perhaps make tableData state not include prng at all...
        boolean serializeDeserializeVerification = false;
        if (serializeDeserializeVerification) {
            final var tempDeckData = tableData.getDeckData();
            tableData.setDeckData(null);
            final var processor = new HandProcessor(LineParserFactory.createTexasHoldemLineParser(), LineApplierFactory.createTexasHoldemLineApplier());
            final var baos = new ByteArrayOutputStream();
            try {
                processor.write(tableData, baos);
                final var serialized = baos.toString();
                final var parsedTableData = processor.process(serialized);
                if (!parsedTableData.getHistory().equals(tableData.getHistory())) {
                    throw new RuntimeException("Detected inequality when tableData.history was first serialized and then deserialized!");
                }
                if (!parsedTableData.getTableName().equals(tableData.getTableName())) {
                    throw new RuntimeException("Detected inequality when tableData.tableName was first serialized and then deserialized!");
                }
                if (!parsedTableData.getGameType().equals(tableData.getGameType())) {
                    throw new RuntimeException("Detected inequality when tableData.gameType was first serialized and then deserialized!");
                }
                if (!parsedTableData.getGameStage().equals(tableData.getGameStage())) {
                    throw new RuntimeException("Detected inequality when tableData.gameStage was first serialized and then deserialized!");
                }
                if (!parsedTableData.getSeats().equals(tableData.getSeats())) {
                    throw new RuntimeException("Detected inequality when tableData.seats was first serialized and then deserialized!");
                }
                if (!parsedTableData.getPots().equals(tableData.getPots())) {
                    throw new RuntimeException("Detected inequality when tableData.pots was first serialized and then deserialized!");
                }
                if (!parsedTableData.getDeckData().equals(tableData.getDeckData())) {
                    throw new RuntimeException("Detected inequality when tableData.deckData was first serialized and then deserialized!");
                }
                if (parsedTableData.getSmallBlind() != tableData.getSmallBlind()) {
                    throw new RuntimeException("Detected inequality when tableData.smallBlind was first serialized and then deserialized!");
                }
                if (parsedTableData.getBigBlind() != tableData.getBigBlind()) {
                    throw new RuntimeException("Detected inequality when tableData.bigBlind was first serialized and then deserialized!");
                }
                if (parsedTableData.getHandId() != tableData.getHandId()) {
                    throw new RuntimeException("Detected inequality when tableData.handId was first serialized and then deserialized!");
                }
                if (parsedTableData.getButtonAt() != tableData.getButtonAt()) {
                    throw new RuntimeException("Detected inequality when tableData.buttonAt was first serialized and then deserialized!");
                }
                if (parsedTableData.getActionAt() != tableData.getActionAt()) {
                    throw new RuntimeException("Detected inequality when tableData.actionAt was first serialized and then deserialized!");
                }
                if (parsedTableData.getTotalBet() != tableData.getTotalBet()) {
                    throw new RuntimeException("Detected inequality when tableData.totalBet was first serialized and then deserialized!");
                }
                if (parsedTableData.getLastRaise() != tableData.getLastRaise()) {
                    throw new RuntimeException("Detected inequality when tableData.lastRaise was first serialized and then deserialized!");
                }
                if (!parsedTableData.getCards().equals(tableData.getCards())) {
                    throw new RuntimeException("Detected inequality when tableData.cards was first serialized and then deserialized!");
                }
                if (parsedTableData.getDelivered() != tableData.getDelivered()) {
                    throw new RuntimeException("Detected inequality when tableData.delivered was first serialized and then deserialized!");
                }
                if (parsedTableData.getChipsInPlay() != tableData.getChipsInPlay()) {
                    throw new RuntimeException("Detected inequality when tableData.chipsInPlay was first serialized and then deserialized!");
                }
                if (!parsedTableData.equals(tableData)) {
                    throw new RuntimeException("Detected inequality when tableData was first serialized and then deserialized!");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                tableData.setDeckData(tempDeckData);
            }
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
