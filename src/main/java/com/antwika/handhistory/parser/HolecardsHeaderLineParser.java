package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.HolecardsHeaderLine;
import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class HolecardsHeaderLineParser implements ILineParser {
    final static String PATTERN = "\\*\\*\\* HOLE CARDS \\*\\*\\*";

    @Override
    public ILine parse(TableData tableData, String line) {
        final var m = Pattern.compile(PATTERN).matcher(line);
        if (m.find()) {
            return new HolecardsHeaderLine();
        }
        return null;
    }

    @Override
    public boolean write(ILine line, ByteArrayOutputStream baos) throws IOException {
        if (!(line instanceof HolecardsHeaderLine holecardsHeaderLine)) return false;
        final var a = String.format(
                "*** HOLE CARDS ***"
        );
        baos.write(a.getBytes(StandardCharsets.UTF_8));
        return true;
    }
}
