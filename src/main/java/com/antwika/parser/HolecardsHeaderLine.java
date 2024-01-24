package com.antwika.parser;

import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;

import java.util.regex.Pattern;

public class HolecardsHeaderLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        int totalChipsInPlay = TableUtil.countChipsInPlay(tableData);
        tableData.setChipsInPlay(totalChipsInPlay);
        return Pattern.compile("\\*\\*\\* HOLE CARDS \\*\\*\\*").matcher(line).find();
    }
}
