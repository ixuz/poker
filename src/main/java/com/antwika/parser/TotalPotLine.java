package com.antwika.parser;

import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

public class TotalPotLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("Total pot: (\\d+)").matcher(line);
        if (m.find()) {
            return true;
        }

        return false;
    }
}
