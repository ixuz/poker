package com.antwika.parser;

import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

public class HandEndLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("--- HAND END ---").matcher(line);
        if (m.find()) {
            tableData.setGameStage(TableData.GameStage.NONE);
            return true;
        }
        return false;
    }
}
