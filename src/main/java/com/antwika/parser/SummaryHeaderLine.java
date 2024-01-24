package com.antwika.parser;

import com.antwika.table.data.TableData;

import java.util.regex.Pattern;

public class SummaryHeaderLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        tableData.setGameStage(TableData.GameStage.NONE);
        tableData.getSeats().forEach(seat -> seat.setHasActed(false));
        return Pattern.compile("\\*\\*\\* SUMMARY \\*\\*\\*").matcher(line).find();
    }
}
