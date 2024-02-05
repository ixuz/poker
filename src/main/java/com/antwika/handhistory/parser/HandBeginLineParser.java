package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.HandBeginLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public non-sealed class HandBeginLineParser implements ILineParser {
    final static String PATTERN = "^--- HAND BEGIN ---$";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            return new HandBeginLine();
        }
        return null;
    }

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        if (!(line instanceof HandBeginLine handBeginLine)) return false;
        final var str = "--- HAND BEGIN ---";
        baos.write(str.getBytes(StandardCharsets.UTF_8));
        return true;
    }
}
