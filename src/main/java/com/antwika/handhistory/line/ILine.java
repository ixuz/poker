package com.antwika.handhistory.line;

public sealed interface ILine permits
        BlindLine,
        CollectedPotLine,
        EmptyLine,
        FlopHeaderLine,
        GameInfoLine,
        HandBeginLine,
        HandEndLine,
        HolecardsHeaderLine,
        HolecardsLine,
        PlayerBetLine,
        PlayerCallLine,
        PlayerCheckLine,
        PlayerFoldLine,
        PlayerRaiseLine,
        RiverHeaderLine,
        SeatInfoLine,
        SummaryBoardInfoLine,
        SummaryHeaderLine,
        SummaryPlayerInfoLine,
        SummaryPotInfoLine,
        SummaryTotalChipsLine,
        TableInfoLine,
        TotalPotLine,
        TurnHeaderLine,
        UncalledBetLine
{

}
