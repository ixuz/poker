package com.antwika.parser;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.table.data.TableData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {
    @Test
    public void parseHeader() throws NotationException {
        final var hand = "--- HAND BEGIN ---\n" +
                "Poker Hand #7: Hold'em No Limit (1/2) - Mon Jan 22 17:57:37 CET 2024\n" +
                "Table 'FSM' 5-max Seat #4 is the button\n" +
                "Seat 1: Alice (988 in chips)\n" +
                "Seat 2: Bob (988 in chips)\n" +
                "Seat 3: Charlie (990 in chips)\n" +
                "Seat 4: David (998 in chips)\n" +
                "Seat 5: Eric (1036 in chips)\n" +
                "Eric: posts small blind 1\n" +
                "Alice: posts big blind 2\n" +
                "*** HOLE CARDS ***\n" +
                "Dealt to Alice [Ad Jh]\n" +
                "Dealt to Bob [6c Kh]\n" +
                "Dealt to Charlie [6d 8d]\n" +
                "Dealt to David [Ac 7d]\n" +
                "Dealt to Eric [9c 9s]\n" +
                "Bob: calls 2\n" +
                "Charlie: folds\n" +
                "David: raises to 6\n" +
                "Eric: raises to 18\n" +
                "Alice: calls 16\n" +
                "Bob: folds\n" +
                "David: folds\n" +
                "*** FLOP *** [3c 8c 5s]\n" +
                "Total pot: 44\n" +
                "Alice: checks\n" +
                "Eric: checks\n" +
                "*** TURN *** [3c 8c 5s] [4s]\n" +
                "Total pot: 44\n" +
                "Alice: checks\n" +
                "Eric: bets 33\n" +
                "Alice: raises to 107\n" +
                "Eric: raises to 331\n" +
                "Alice: raises to 915\n" +
                "Eric: calls 584\n" +
                "*** RIVER *** [3c 8c 4s 5s] [Tc]\n" +
                "Total pot: 1874\n" +
                "Alice: bets 55 and is all-in\n" +
                "Eric: calls 55\n" +
                "Eric collected 1874 from Main pot\n" +
                "Eric collected 110 from Side pot #1\n" +
                "*** SUMMARY ***\n" +
                "Total pot 1984 | Rake 0\n" +
                "Board [3c 8c Tc 4s 5s]\n" +
                "Seat 0: Alice stack 0\n" +
                "Seat 1: Bob stack 986\n" +
                "Seat 2: Charlie stack 990\n" +
                "Seat 3: David stack 992\n" +
                "Seat 4: Eric stack 2032\n" +
                "Total chips in play 5000\n" +
                "--- HAND END ---";

        final var tableData = Parser.parse(LineParsers.createTexasHoldemLineParsers(), List.of(hand.split("\n")));

        assertEquals(7L, tableData.getHandId());
        assertEquals("Hold'em No Limit", tableData.getGameType());
        assertEquals(1, tableData.getSmallBlind());
        assertEquals(2, tableData.getBigBlind());
        assertEquals(TableData.GameStage.NONE, tableData.getGameStage());
        assertEquals("3c8cTc4s5s", HandUtil.toNotation(tableData.getCards()));

        final var tableSeats = tableData.getSeats();
        assertEquals(5, tableSeats.size());
        assertEquals("Alice", tableSeats.get(0).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("AdJh").getBitmask(), tableSeats.get(0).getCards());
        assertEquals(0, tableSeats.get(0).getStack());
        assertEquals(0, tableSeats.get(0).getCommitted());
        assertEquals("Bob", tableSeats.get(1).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6cKh").getBitmask(), tableSeats.get(1).getCards());
        assertEquals(986, tableSeats.get(1).getStack());
        assertEquals(0, tableSeats.get(1).getCommitted());
        assertEquals("Charlie", tableSeats.get(2).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6d8d").getBitmask(), tableSeats.get(2).getCards());
        assertEquals(990, tableSeats.get(2).getStack());
        assertEquals(0, tableSeats.get(2).getCommitted());
        assertEquals("David", tableSeats.get(3).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("Ac7d").getBitmask(), tableSeats.get(3).getCards());
        assertEquals(992, tableSeats.get(3).getStack());
        assertEquals(0, tableSeats.get(3).getCommitted());
        assertEquals("Eric", tableSeats.get(4).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("9c9s").getBitmask(), tableSeats.get(4).getCards());
        assertEquals(2032, tableSeats.get(4).getStack());
        assertEquals(0, tableSeats.get(4).getCommitted());
    }
}
