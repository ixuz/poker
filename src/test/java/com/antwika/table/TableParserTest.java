package com.antwika.table;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TableParserTest {
    @Test
    public void parseHeader() {
        final var handBeginTitle = "--- HAND BEGIN ---";
        final var gameInfo = "Poker Hand #1: Hold'em No Limit (1/2) - Sun Jan 21 20:59:20 CET 2024";
        final var tableInfo = "Table 'FSM' 5-max Seat #1 is the button";
        final var seatInfo = List.of(
                "Seat 1: Alice (1000 in chips)",
                "Seat 2: Bob (1000 in chips)",
                "Seat 3: Charlie (1000 in chips)",
                "Seat 4: David (1000 in chips)",
                "Seat 5: Eric (1000 in chips)"
        );
        final var blinds = List.of(
                "Bob: posts small blind 1",
                "Charlie: posts big blind 2"
        );
        final var holecardsHeader = "*** HOLE CARDS ***";
        final var holecards = List.of(
                "Dealt to Alice [6c Tc]",
                "Dealt to Bob [Kc Ad]",
                "Dealt to Charlie [6d Qs]",
                "Dealt to David [2d 8s]",
                "Dealt to Eric [8h Th]"
        );
        final var preFlopActions = List.of(
                "David: calls 2",
                "Eric: calls 2",
                "Alice: calls 2",
                "Bob: calls 1",
                "Charlie: bets 10",
                "David: folds",
                "Eric: calls 10",
                "Alice: calls 10",
                "Bob: calls 10"
        );
        final var flopTitle = "*** FLOP *** [8d 2s 5s]";
        final var flopPot = "Total pot: 50";
        final var flopActions = List.of(
                "Charlie: checks",
                "Eric: checks",
                "Alice: checks",
                "Bob: checks"
        );
        final var turnTitle = "*** TURN *** [8d 2s 5s] [3h]";
        final var turnPot = "Total pot: 50";
        final var turnActions = List.of(
                "Charlie: checks",
                "Eric: checks",
                "Alice: checks",
                "Bob: checks"
        );
        final var riverTitle = "*** RIVER *** [8d 3h 2s 5s] [2c]";
        final var riverPot = "Total pot: 50";
        final var riverActions = List.of(
                "Charlie: checks",
                "Eric: checks",
                "Alice: checks",
                "Bob: checks"
        );
        final var collects = List.of(
                "Eric collected 50 from Main pot"
        );
        final var summaryTitle = "*** SUMMARY ***";
        final var summaryPotInfo = "Total pot 50 | Rake 0";
        final var summaryBoardInfo = "Board [2c 8d 3h 2s 5s]";
        final var summarySeatInfo = List.of(
                "Seat 0: Alice stack 988",
                "Seat 1: Bob stack 988",
                "Seat 2: Charlie stack 988",
                "Seat 3: David stack 998",
                "Seat 4: Eric stack 1038"
        );
        final var summaryChipsInfo = "Total chips in play 5000";
        final var handEndTitle = "--- HAND END ---";

        final var lines = new ArrayList<String>();

        lines.add(handBeginTitle);
        lines.add(gameInfo);
        lines.add(tableInfo);
        lines.addAll(seatInfo);
        lines.addAll(blinds);
        lines.add(holecardsHeader);
        lines.addAll(holecards);
        lines.addAll(preFlopActions);
        lines.add(flopTitle);
        lines.add(flopPot);
        lines.addAll(flopActions);
        lines.add(turnTitle);
        lines.add(turnPot);
        lines.addAll(turnActions);
        lines.add(riverTitle);
        lines.add(riverPot);
        lines.addAll(riverActions);
        lines.addAll(collects);
        lines.add(summaryTitle);
        lines.add(summaryPotInfo);
        lines.add(summaryBoardInfo);
        lines.addAll(summarySeatInfo);
        lines.add(summaryChipsInfo);
        lines.add(handEndTitle);

        final var tableData = TableParser.parse(lines);

        assertEquals(1L, tableData.getHandId());
        assertEquals("Hold'em No Limit", tableData.getGameType());
        assertEquals(1, tableData.getSmallBlind());
        assertEquals(2, tableData.getBigBlind());

        final var tableSeats = tableData.getSeats();
        assertEquals(5, tableSeats.size());
        assertEquals("Alice", tableSeats.get(0).getPlayer().getPlayerData().getPlayerName());
        assertEquals(1000, tableSeats.get(0).getStack());
        assertEquals("Bob", tableSeats.get(1).getPlayer().getPlayerData().getPlayerName());
        assertEquals(1000, tableSeats.get(1).getStack());
        assertEquals("Charlie", tableSeats.get(2).getPlayer().getPlayerData().getPlayerName());
        assertEquals(1000, tableSeats.get(2).getStack());
        assertEquals("David", tableSeats.get(3).getPlayer().getPlayerData().getPlayerName());
        assertEquals(1000, tableSeats.get(3).getStack());
        assertEquals("Eric", tableSeats.get(4).getPlayer().getPlayerData().getPlayerName());
        assertEquals(1000, tableSeats.get(4).getStack());
    }
}
