package com.antwika.handhistory.parser;

import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public sealed interface ILineParser permits
        AggregateLineParser,
        BlindLineParser,
        CollectedPotLineParser,
        EmptyLineParser,
        FlopHeaderLineParser,
        GameInfoLineParser,
        HandBeginLineParser,
        HandEndLineParser,
        HolecardsHeaderLineParser,
        HolecardsLineParser,
        PlayerBetLineParser,
        PlayerCallLineParser,
        PlayerCheckLineParser,
        PlayerFoldLineParser,
        PlayerRaiseLineParser,
        RiverHeaderLineParser,
        SeatInfoLineParser,
        SummaryBoardInfoLineParser,
        SummaryHeaderLineParser,
        SummaryPlayerInfoLineParser,
        SummaryPotInfoLineParser,
        SummaryTotalChipsLineParser,
        TableInfoLineParser,
        TotalPotLineParser,
        TurnHeaderLineParser,
        UncalledBetLineParser
{
    ILine parse(TableData tableData, String line);
    boolean write(ILine line, ByteArrayOutputStream baos) throws IOException;
}
