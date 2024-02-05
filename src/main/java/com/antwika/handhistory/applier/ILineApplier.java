package com.antwika.handhistory.applier;

import com.antwika.handhistory.line.ILine;
import com.antwika.table.data.TableData;

public sealed interface ILineApplier permits
        AggregateLineApplier,
        BlindLineApplier,
        CollectedPotLineApplier,
        EmptyLineApplier,
        FlopHeaderLineApplier,
        GameInfoLineApplier,
        HandBeginLineApplier,
        HandEndLineApplier,
        HolecardsHeaderLineApplier,
        HolecardsLineApplier,
        PlayerBetLineApplier,
        PlayerCallLineApplier,
        PlayerCheckLineApplier,
        PlayerFoldLineApplier,
        PlayerRaiseLineApplier,
        RiverHeaderLineApplier,
        SeatInfoLineApplier,
        SummaryBoardInfoLineApplier,
        SummaryHeaderLineApplier,
        SummaryPlayerInfoLineApplier,
        SummaryPotInfoLineApplier,
        SummaryTotalChipsLineApplier,
        TableInfoLineApplier,
        TotalPotLineApplier,
        TurnHeaderLineApplier,
        UncalledBetLineApplier
{
    boolean apply(TableData tableData, ILine line);
}
