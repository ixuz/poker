package com.antwika.parser;

import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;

import java.util.regex.Pattern;

public class SummaryHeaderLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        tableData.setGameStage(TableData.GameStage.NONE);
        tableData.getSeats().forEach(seat -> seat.setHasActed(false));
        tableData.getPots().clear();
        tableData.getSeats().forEach(seat -> seat.setCommitted(0));
        tableData.setChipsInPlay(TableUtil.countChipsInPlay(tableData));
        return Pattern.compile("\\*\\*\\* SUMMARY \\*\\*\\*").matcher(line).find();
    }
}
