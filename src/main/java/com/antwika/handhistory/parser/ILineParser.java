package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface ILineParser {
    ILine parse(TableData tableData, String line);
    boolean write(ILine line, ByteArrayOutputStream baos) throws IOException;
}
