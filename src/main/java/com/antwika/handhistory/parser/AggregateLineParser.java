package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AggregateLineParser implements ILineParser {
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

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        for (final ILineParser lineParser : lineParsers) {
            boolean written = lineParser.write(line, baos);
            if (!written) continue;

            baos.write("\n".getBytes(StandardCharsets.UTF_8));
        }
        return true;
    }
}
