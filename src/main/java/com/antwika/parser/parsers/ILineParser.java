package com.antwika.parser.parsers;

import com.antwika.table.data.TableData;

public interface ILineParser {
    boolean parse(TableData tableData, String line);
}
