package com.antwika.parser;

import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

public class SummaryTotalChipsLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        return Pattern.compile("Total chips in play (\\d+)").matcher(line).find();
    }
}
