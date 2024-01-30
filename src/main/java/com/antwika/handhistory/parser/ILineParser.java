package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;

public interface ILineParser {
    ILine parse(TableData tableData, String line);
}
