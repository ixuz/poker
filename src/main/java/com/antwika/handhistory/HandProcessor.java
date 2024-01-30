package com.antwika.handhistory;

import com.antwika.handhistory.applier.ILineApplier;
import com.antwika.handhistory.parser.ILineParser;
import com.antwika.table.data.TableData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandProcessor {
    private static final Logger logger = LoggerFactory.getLogger(HandProcessor.class);
    private final ILineParser parser;

    private final ILineApplier applier;

    public HandProcessor(ILineParser parser, ILineApplier applier) {
        this.parser = parser;
        this.applier = applier;
    }

    public TableData process(String hand) {
        final var tableData = new TableData();
        final var handLines = hand.split("\n");

        for (var handLine : handLines) {
            final var line = parser.parse(tableData, handLine);
            if (line == null) {
                logger.error("No suitable LineParser for: '{}'", handLine);
                return null;
            }

            final var applied = applier.apply(tableData, line);
            if (!applied) {
                logger.error("No suitable LineApplier for: '{}'", line);
                return null;
            }
        }

        return tableData;
    }
}
