package com.antwika.parser;

import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

public class SummaryBoardInfoLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        return Pattern.compile("Board \\[(..) (..) (..) (..) (..)]").matcher(line).find();
    }
}
