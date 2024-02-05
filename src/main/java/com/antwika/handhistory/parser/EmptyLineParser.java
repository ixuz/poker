package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.EmptyLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;
import java.io.ByteArrayOutputStream;
import java.util.regex.Pattern;

public non-sealed class EmptyLineParser implements ILineParser {
    final static String PATTERN = "^$";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            return new EmptyLine();
        }
        return null;
    }

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) {
        if (!(line instanceof EmptyLine emptyLine)) return false;
        return true;
    }
}
