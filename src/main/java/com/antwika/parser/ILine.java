package com.antwika.parser;

import com.antwika.table.data.TableData;

public interface ILine {
    boolean parse(TableData tableData, String line);
}
