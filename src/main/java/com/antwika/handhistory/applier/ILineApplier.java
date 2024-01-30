package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;

public interface ILineApplier {
    boolean apply(TableData tableData, ILine line);
}
