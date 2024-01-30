package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
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
    public ILine parse(TableData tableData, String line) {
        for (var lineParser : lineParsers) {
            final var parsedLine = lineParser.parse(tableData, line);
            if (parsedLine != null) {
                return parsedLine;
            }
        }
        return null;
    }
}
