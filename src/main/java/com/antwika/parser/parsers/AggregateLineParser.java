package com.antwika.parser.parsers;

import com.antwika.table.data.TableData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AggregateLineParser implements ILineParser {
    private static final Logger logger = LoggerFactory.getLogger(AggregateLineParser.class);
    final List<ILineParser> lineParsers;

    public AggregateLineParser(List<ILineParser> lineParsers) {
        this.lineParsers = lineParsers;
    }

    @Override
    public boolean parse(TableData tableData, String line) {
        final var lines = line.split("\n");

        for (var l : lines) {
            boolean handled = false;

            for (var lineParser : lineParsers) {
                if (lineParser.parse(tableData, l)) {
                    handled = true;
                    break;
                }
            }

            if (!handled) {
                logger.warn("No suitable LineParser for: '{}'", l);
            }
        }

        return false;
    }
}
