package com.antwika.parser;

import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

public class SummaryPotInfoLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        return Pattern.compile("Total pot (\\d+) | Rake (\\d+)").matcher(line).find();
    }
}
