package com.antwika.parser;

import java.util.List;

public class LineParsers {
    public static List<ILine> createTexasHoldemLineParsers() {
        return List.of(
                new HandBeginLine(),
                new GameInfoLine(),
                new TableInfoLine(),
                new SeatInfoLine(),
                new BlindLine(),
                new HolecardsHeaderLine(),
                new HolecardsLine(),
                new PlayerCheckLine(),
                new PlayerBetLine(),
                new PlayerCallLine(),
                new PlayerRaiseLine(),
                new PlayerFoldLine(),
                new UncalledBetLine(),
                new FlopHeaderLine(),
                new TurnHeaderLine(),
                new RiverHeaderLine(),
                new TotalPotLine(),
                new CollectedPotLine(),
                new SummaryHeaderLine(),
                new SummaryPotInfoLine(),
                new SummaryBoardInfoLine(),
                new SummaryPlayerInfoLine(),
                new SummaryTotalChipsLine(),
                new HandEndLine()
        );
    }
}
