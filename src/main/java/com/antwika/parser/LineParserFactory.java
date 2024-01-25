package com.antwika.parser;

import com.antwika.parser.parsers.*;

import java.util.List;

public class LineParserFactory {
    public static ILineParser createTexasHoldemLineParser() {
        return new AggregateLineParser(List.of(
                new HandBeginLineParser(),
                new GameInfoLineParser(),
                new TableInfoLineParser(),
                new SeatInfoLineParser(),
                new BlindLineParser(),
                new HolecardsHeaderLineParser(),
                new HolecardsLineParser(),
                new PlayerCheckLineParser(),
                new PlayerBetLineParser(),
                new PlayerCallLineParser(),
                new PlayerRaiseLineParser(),
                new PlayerFoldLineParser(),
                new UncalledBetLineParser(),
                new FlopHeaderLineParser(),
                new TurnHeaderLineParser(),
                new RiverHeaderLineParser(),
                new TotalPotLineParser(),
                new CollectedPotLineParser(),
                new SummaryHeaderLineParser(),
                new SummaryPotInfoLineParser(),
                new SummaryBoardInfoLineParser(),
                new SummaryPlayerInfoLineParser(),
                new SummaryTotalChipsLineParser(),
                new HandEndLineParser()
        ));
    }
}
