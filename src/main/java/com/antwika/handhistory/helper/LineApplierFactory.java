package com.antwika.handhistory.helper;

import com.antwika.handhistory.applier.*;

import java.util.List;

public class LineApplierFactory {
    public static ILineApplier createTexasHoldemLineApplier() {
        return new AggregateLineApplier(List.of(
                new EmptyLineApplier(),
                new HandBeginLineApplier(),
                new GameInfoLineApplier(),
                new TableInfoLineApplier(),
                new SeatInfoLineApplier(),
                new BlindLineApplier(),
                new HolecardsHeaderLineApplier(),
                new HolecardsLineApplier(),
                new PlayerCheckLineApplier(),
                new PlayerBetLineApplier(),
                new PlayerCallLineApplier(),
                new PlayerRaiseLineApplier(),
                new PlayerFoldLineApplier(),
                new UncalledBetLineApplier(),
                new FlopHeaderLineApplier(),
                new TurnHeaderLineApplier(),
                new RiverHeaderLineApplier(),
                new TotalPotLineApplier(),
                new CollectedPotLineApplier(),
                new SummaryHeaderLineApplier(),
                new SummaryPotInfoLineApplier(),
                new SummaryBoardInfoLineApplier(),
                new SummaryPlayerInfoLineApplier(),
                new SummaryTotalChipsLineApplier(),
                new HandEndLineApplier()
        ));
    }
}
