package com.antwika.parser;

import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

public class HandBeginLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("--- HAND BEGIN ---").matcher(line);
        if (m.find()) {
            tableData.setGameStage(TableData.GameStage.PREFLOP);
            return true;
        }
        return false;
    }
}
