package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.HandEndLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public non-sealed class HandEndLineParser implements ILineParser {
    final static String PATTERN = "^--- HAND END ---$";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            return new HandEndLine();
        }
        return null;
    }

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        if (!(line instanceof HandEndLine handEndLine)) return false;
        final var str = "--- HAND END ---";
        baos.write(str.getBytes(StandardCharsets.UTF_8));
        return true;
    }
}
