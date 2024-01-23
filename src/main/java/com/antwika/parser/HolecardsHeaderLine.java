package com.antwika.parser;

import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

public class HolecardsHeaderLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        return Pattern.compile("\\*\\*\\* HOLE CARDS \\*\\*\\*").matcher(line).find();
    }
}
