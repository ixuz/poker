package com.antwika.parser;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.table.data.TableData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
                "Alice: posts big blind 2";

        final var tableData = Parser.parse(LineParsers.createTexasHoldemLineParsers(), List.of(hand.split("\n")));

        assertEquals(5000, tableData.getChipsInPlay());

        assertEquals(7L, tableData.getHandId());
        assertEquals("Hold'em No Limit", tableData.getGameType());
        assertEquals(1, tableData.getSmallBlind());
        assertEquals(2, tableData.getBigBlind());
        assertEquals(TableData.GameStage.PREFLOP, tableData.getGameStage());
        assertEquals("", HandUtil.toNotation(tableData.getCards()));

        final var tableSeats = tableData.getSeats();
        assertEquals(5, tableSeats.size());
        assertEquals("Alice", tableSeats.get(0).getPlayer().getPlayerData().getPlayerName());
        assertEquals("", HandUtil.toNotation(tableSeats.get(0).getCards()));
        assertEquals(986, tableSeats.get(0).getStack());
        assertEquals(2, tableSeats.get(0).getCommitted());
        assertFalse(tableSeats.get(0).isHasFolded());
        assertFalse(tableSeats.get(0).isHasActed());
        assertEquals("Bob", tableSeats.get(1).getPlayer().getPlayerData().getPlayerName());
        assertEquals("", HandUtil.toNotation(tableSeats.get(1).getCards()));
        assertEquals(988, tableSeats.get(1).getStack());
        assertEquals(0, tableSeats.get(1).getCommitted());
        assertFalse(tableSeats.get(1).isHasFolded());
        assertFalse(tableSeats.get(1).isHasActed());
        assertEquals("Charlie", tableSeats.get(2).getPlayer().getPlayerData().getPlayerName());
        assertEquals("", HandUtil.toNotation(tableSeats.get(2).getCards()));
        assertEquals(990, tableSeats.get(2).getStack());
        assertEquals(0, tableSeats.get(2).getCommitted());
        assertFalse(tableSeats.get(2).isHasFolded());
        assertFalse(tableSeats.get(2).isHasActed());
        assertEquals("David", tableSeats.get(3).getPlayer().getPlayerData().getPlayerName());
        assertEquals("", HandUtil.toNotation(tableSeats.get(3).getCards()));
        assertEquals(998, tableSeats.get(3).getStack());
        assertEquals(0, tableSeats.get(3).getCommitted());
        assertFalse(tableSeats.get(3).isHasFolded());
        assertFalse(tableSeats.get(3).isHasActed());
        assertEquals("Eric", tableSeats.get(4).getPlayer().getPlayerData().getPlayerName());
        assertEquals("", HandUtil.toNotation(tableSeats.get(4).getCards()));
        assertEquals(1035, tableSeats.get(4).getStack());
        assertEquals(1, tableSeats.get(4).getCommitted());
        assertFalse(tableSeats.get(4).isHasFolded());
        assertFalse(tableSeats.get(4).isHasActed());
    }

    @Test
    public void parseHoldcards() throws NotationException {
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
                "Dealt to Eric [9c 9s]";

        final var tableData = Parser.parse(LineParsers.createTexasHoldemLineParsers(), List.of(hand.split("\n")));

        assertEquals(5000, tableData.getChipsInPlay());

        assertEquals(7L, tableData.getHandId());
        assertEquals("Hold'em No Limit", tableData.getGameType());
        assertEquals(1, tableData.getSmallBlind());
        assertEquals(2, tableData.getBigBlind());
        assertEquals(TableData.GameStage.PREFLOP, tableData.getGameStage());
        assertEquals("", HandUtil.toNotation(tableData.getCards()));

        final var tableSeats = tableData.getSeats();
        assertEquals(5, tableSeats.size());
        assertEquals("Alice", tableSeats.get(0).getPlayer().getPlayerData().getPlayerName());
        assertEquals("AdJh", HandUtil.toNotation(tableSeats.get(0).getCards()));
        assertEquals(986, tableSeats.get(0).getStack());
        assertEquals(2, tableSeats.get(0).getCommitted());
        assertFalse(tableSeats.get(0).isHasFolded());
        assertFalse(tableSeats.get(0).isHasActed());
        assertEquals("Bob", tableSeats.get(1).getPlayer().getPlayerData().getPlayerName());
        assertEquals("6cKh", HandUtil.toNotation(tableSeats.get(1).getCards()));
        assertEquals(988, tableSeats.get(1).getStack());
        assertEquals(0, tableSeats.get(1).getCommitted());
        assertFalse(tableSeats.get(1).isHasFolded());
        assertFalse(tableSeats.get(1).isHasActed());
        assertEquals("Charlie", tableSeats.get(2).getPlayer().getPlayerData().getPlayerName());
        assertEquals("6d8d", HandUtil.toNotation(tableSeats.get(2).getCards()));
        assertEquals(990, tableSeats.get(2).getStack());
        assertEquals(0, tableSeats.get(2).getCommitted());
        assertFalse(tableSeats.get(2).isHasFolded());
        assertFalse(tableSeats.get(2).isHasActed());
        assertEquals("David", tableSeats.get(3).getPlayer().getPlayerData().getPlayerName());
        assertEquals("Ac7d", HandUtil.toNotation(tableSeats.get(3).getCards()));
        assertEquals(998, tableSeats.get(3).getStack());
        assertEquals(0, tableSeats.get(3).getCommitted());
        assertFalse(tableSeats.get(3).isHasFolded());
        assertFalse(tableSeats.get(3).isHasActed());
        assertEquals("Eric", tableSeats.get(4).getPlayer().getPlayerData().getPlayerName());
        assertEquals("9c9s", HandUtil.toNotation(tableSeats.get(4).getCards()));
        assertEquals(1035, tableSeats.get(4).getStack());
        assertEquals(1, tableSeats.get(4).getCommitted());
        assertFalse(tableSeats.get(4).isHasFolded());
        assertFalse(tableSeats.get(4).isHasActed());
    }

    @Test
    public void parsePreFlopAction() throws NotationException {
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
                "David: folds";

        final var tableData = Parser.parse(LineParsers.createTexasHoldemLineParsers(), List.of(hand.split("\n")));

        assertEquals(5000, tableData.getChipsInPlay());

        assertEquals(7L, tableData.getHandId());
        assertEquals("Hold'em No Limit", tableData.getGameType());
        assertEquals(1, tableData.getSmallBlind());
        assertEquals(2, tableData.getBigBlind());
        assertEquals(TableData.GameStage.PREFLOP, tableData.getGameStage());
        assertEquals("", HandUtil.toNotation(tableData.getCards()));

        final var tableSeats = tableData.getSeats();
        assertEquals(5, tableSeats.size());
        assertEquals("Alice", tableSeats.get(0).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("AdJh").getBitmask(), tableSeats.get(0).getCards());
        assertEquals(972, tableSeats.get(0).getStack());
        assertEquals(16, tableSeats.get(0).getCommitted());
        assertFalse(tableSeats.get(0).isHasFolded());
        assertTrue(tableSeats.get(0).isHasActed());
        assertEquals("Bob", tableSeats.get(1).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6cKh").getBitmask(), tableSeats.get(1).getCards());
        assertEquals(986, tableSeats.get(1).getStack());
        assertEquals(2, tableSeats.get(1).getCommitted());
        assertTrue(tableSeats.get(1).isHasFolded());
        assertTrue(tableSeats.get(1).isHasActed());
        assertEquals("Charlie", tableSeats.get(2).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6d8d").getBitmask(), tableSeats.get(2).getCards());
        assertEquals(990, tableSeats.get(2).getStack());
        assertEquals(0, tableSeats.get(2).getCommitted());
        assertTrue(tableSeats.get(2).isHasFolded());
        assertTrue(tableSeats.get(2).isHasActed());
        assertEquals("David", tableSeats.get(3).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("Ac7d").getBitmask(), tableSeats.get(3).getCards());
        assertEquals(992, tableSeats.get(3).getStack());
        assertEquals(6, tableSeats.get(3).getCommitted());
        assertTrue(tableSeats.get(3).isHasFolded());
        assertTrue(tableSeats.get(3).isHasActed());
        assertEquals("Eric", tableSeats.get(4).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("9c9s").getBitmask(), tableSeats.get(4).getCards());
        assertEquals(1018, tableSeats.get(4).getStack());
        assertEquals(18, tableSeats.get(4).getCommitted());
        assertFalse(tableSeats.get(4).isHasFolded());
        assertTrue(tableSeats.get(4).isHasActed());
    }

    @Test
    public void parseFlop() throws NotationException {
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
                "Alice: calls 18\n" +
                "Bob: folds\n" +
                "David: folds\n" +
                "*** FLOP *** [3c 8c 5s]";

        final var tableData = Parser.parse(LineParsers.createTexasHoldemLineParsers(), List.of(hand.split("\n")));

        assertEquals(5000, tableData.getChipsInPlay());

        assertEquals(7L, tableData.getHandId());
        assertEquals("Hold'em No Limit", tableData.getGameType());
        assertEquals(1, tableData.getSmallBlind());
        assertEquals(2, tableData.getBigBlind());
        assertEquals(TableData.GameStage.FLOP, tableData.getGameStage());
        assertEquals("3c8c5s", HandUtil.toNotation(tableData.getCards()));

        final var tableSeats = tableData.getSeats();
        assertEquals(5, tableSeats.size());
        assertEquals("Alice", tableSeats.get(0).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("AdJh").getBitmask(), tableSeats.get(0).getCards());
        assertEquals(970, tableSeats.get(0).getStack());
        assertEquals(0, tableSeats.get(0).getCommitted());
        assertFalse(tableSeats.get(0).isHasFolded());
        assertFalse(tableSeats.get(0).isHasActed());
        assertEquals("Bob", tableSeats.get(1).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6cKh").getBitmask(), tableSeats.get(1).getCards());
        assertEquals(986, tableSeats.get(1).getStack());
        assertEquals(0, tableSeats.get(1).getCommitted());
        assertTrue(tableSeats.get(1).isHasFolded());
        assertFalse(tableSeats.get(1).isHasActed());
        assertEquals("Charlie", tableSeats.get(2).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6d8d").getBitmask(), tableSeats.get(2).getCards());
        assertEquals(990, tableSeats.get(2).getStack());
        assertEquals(0, tableSeats.get(2).getCommitted());
        assertTrue(tableSeats.get(2).isHasFolded());
        assertFalse(tableSeats.get(2).isHasActed());
        assertEquals("David", tableSeats.get(3).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("Ac7d").getBitmask(), tableSeats.get(3).getCards());
        assertEquals(992, tableSeats.get(3).getStack());
        assertEquals(0, tableSeats.get(3).getCommitted());
        assertTrue(tableSeats.get(3).isHasFolded());
        assertFalse(tableSeats.get(3).isHasActed());
        assertEquals("Eric", tableSeats.get(4).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("9c9s").getBitmask(), tableSeats.get(4).getCards());
        assertEquals(1018, tableSeats.get(4).getStack());
        assertEquals(0, tableSeats.get(4).getCommitted());
        assertFalse(tableSeats.get(4).isHasFolded());
        assertFalse(tableSeats.get(4).isHasActed());
    }

    @Test
    public void parseFlopAction() throws NotationException {
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
                "Alice: calls 18\n" +
                "Bob: folds\n" +
                "David: folds\n" +
                "*** FLOP *** [3c 8c 5s]\n" +
                "Total pot: 44\n" +
                "Alice: checks\n" +
                "Eric: checks";

        final var tableData = Parser.parse(LineParsers.createTexasHoldemLineParsers(), List.of(hand.split("\n")));

        assertEquals(5000, tableData.getChipsInPlay());

        assertEquals(7L, tableData.getHandId());
        assertEquals("Hold'em No Limit", tableData.getGameType());
        assertEquals(1, tableData.getSmallBlind());
        assertEquals(2, tableData.getBigBlind());
        assertEquals(TableData.GameStage.FLOP, tableData.getGameStage());
        assertEquals("3c8c5s", HandUtil.toNotation(tableData.getCards()));

        final var tableSeats = tableData.getSeats();
        assertEquals(5, tableSeats.size());
        assertEquals("Alice", tableSeats.get(0).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("AdJh").getBitmask(), tableSeats.get(0).getCards());
        assertEquals(970, tableSeats.get(0).getStack());
        assertEquals(0, tableSeats.get(0).getCommitted());
        assertFalse(tableSeats.get(0).isHasFolded());
        assertTrue(tableSeats.get(0).isHasActed());
        assertEquals("Bob", tableSeats.get(1).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6cKh").getBitmask(), tableSeats.get(1).getCards());
        assertEquals(986, tableSeats.get(1).getStack());
        assertEquals(0, tableSeats.get(1).getCommitted());
        assertTrue(tableSeats.get(1).isHasFolded());
        assertFalse(tableSeats.get(1).isHasActed());
        assertEquals("Charlie", tableSeats.get(2).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6d8d").getBitmask(), tableSeats.get(2).getCards());
        assertEquals(990, tableSeats.get(2).getStack());
        assertEquals(0, tableSeats.get(2).getCommitted());
        assertTrue(tableSeats.get(2).isHasFolded());
        assertFalse(tableSeats.get(2).isHasActed());
        assertEquals("David", tableSeats.get(3).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("Ac7d").getBitmask(), tableSeats.get(3).getCards());
        assertEquals(992, tableSeats.get(3).getStack());
        assertEquals(0, tableSeats.get(3).getCommitted());
        assertTrue(tableSeats.get(3).isHasFolded());
        assertFalse(tableSeats.get(3).isHasActed());
        assertEquals("Eric", tableSeats.get(4).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("9c9s").getBitmask(), tableSeats.get(4).getCards());
        assertEquals(1018, tableSeats.get(4).getStack());
        assertEquals(0, tableSeats.get(4).getCommitted());
        assertFalse(tableSeats.get(4).isHasFolded());
        assertTrue(tableSeats.get(4).isHasActed());
    }

    @Test
    public void parseTurn() throws NotationException {
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
                "Alice: calls 18\n" +
                "Bob: folds\n" +
                "David: folds\n" +
                "*** FLOP *** [3c 8c 5s]\n" +
                "Total pot: 44\n" +
                "Alice: checks\n" +
                "Eric: checks\n" +
                "*** TURN *** [3c 8c 5s] [4s]";

        final var tableData = Parser.parse(LineParsers.createTexasHoldemLineParsers(), List.of(hand.split("\n")));

        assertEquals(5000, tableData.getChipsInPlay());

        assertEquals(7L, tableData.getHandId());
        assertEquals("Hold'em No Limit", tableData.getGameType());
        assertEquals(1, tableData.getSmallBlind());
        assertEquals(2, tableData.getBigBlind());
        assertEquals(TableData.GameStage.TURN, tableData.getGameStage());
        assertEquals("3c8c4s5s", HandUtil.toNotation(tableData.getCards()));

        final var tableSeats = tableData.getSeats();
        assertEquals(5, tableSeats.size());
        assertEquals("Alice", tableSeats.get(0).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("AdJh").getBitmask(), tableSeats.get(0).getCards());
        assertEquals(970, tableSeats.get(0).getStack());
        assertEquals(0, tableSeats.get(0).getCommitted());
        assertFalse(tableSeats.get(0).isHasFolded());
        assertFalse(tableSeats.get(0).isHasActed());
        assertEquals("Bob", tableSeats.get(1).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6cKh").getBitmask(), tableSeats.get(1).getCards());
        assertEquals(986, tableSeats.get(1).getStack());
        assertEquals(0, tableSeats.get(1).getCommitted());
        assertTrue(tableSeats.get(1).isHasFolded());
        assertFalse(tableSeats.get(1).isHasActed());
        assertEquals("Charlie", tableSeats.get(2).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6d8d").getBitmask(), tableSeats.get(2).getCards());
        assertEquals(990, tableSeats.get(2).getStack());
        assertEquals(0, tableSeats.get(2).getCommitted());
        assertTrue(tableSeats.get(2).isHasFolded());
        assertFalse(tableSeats.get(2).isHasActed());
        assertEquals("David", tableSeats.get(3).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("Ac7d").getBitmask(), tableSeats.get(3).getCards());
        assertEquals(992, tableSeats.get(3).getStack());
        assertEquals(0, tableSeats.get(3).getCommitted());
        assertTrue(tableSeats.get(3).isHasFolded());
        assertFalse(tableSeats.get(3).isHasActed());
        assertEquals("Eric", tableSeats.get(4).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("9c9s").getBitmask(), tableSeats.get(4).getCards());
        assertEquals(1018, tableSeats.get(4).getStack());
        assertEquals(0, tableSeats.get(4).getCommitted());
        assertFalse(tableSeats.get(4).isHasFolded());
        assertFalse(tableSeats.get(4).isHasActed());
    }

    @Test
    public void parseTurnAction() throws NotationException {
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
                "Alice: calls 18\n" +
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
                "Eric: calls 915";

        final var tableData = Parser.parse(LineParsers.createTexasHoldemLineParsers(), List.of(hand.split("\n")));

        assertEquals(5000, tableData.getChipsInPlay());

        assertEquals(7L, tableData.getHandId());
        assertEquals("Hold'em No Limit", tableData.getGameType());
        assertEquals(1, tableData.getSmallBlind());
        assertEquals(2, tableData.getBigBlind());
        assertEquals(TableData.GameStage.TURN, tableData.getGameStage());
        assertEquals("3c8c4s5s", HandUtil.toNotation(tableData.getCards()));

        final var tableSeats = tableData.getSeats();
        assertEquals(5, tableSeats.size());
        assertEquals("Alice", tableSeats.get(0).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("AdJh").getBitmask(), tableSeats.get(0).getCards());
        assertEquals(55, tableSeats.get(0).getStack());
        assertEquals(915, tableSeats.get(0).getCommitted());
        assertFalse(tableSeats.get(0).isHasFolded());
        assertTrue(tableSeats.get(0).isHasActed());
        assertEquals("Bob", tableSeats.get(1).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6cKh").getBitmask(), tableSeats.get(1).getCards());
        assertEquals(986, tableSeats.get(1).getStack());
        assertEquals(0, tableSeats.get(1).getCommitted());
        assertTrue(tableSeats.get(1).isHasFolded());
        assertFalse(tableSeats.get(1).isHasActed());
        assertEquals("Charlie", tableSeats.get(2).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6d8d").getBitmask(), tableSeats.get(2).getCards());
        assertEquals(990, tableSeats.get(2).getStack());
        assertEquals(0, tableSeats.get(2).getCommitted());
        assertTrue(tableSeats.get(2).isHasFolded());
        assertFalse(tableSeats.get(2).isHasActed());
        assertEquals("David", tableSeats.get(3).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("Ac7d").getBitmask(), tableSeats.get(3).getCards());
        assertEquals(992, tableSeats.get(3).getStack());
        assertEquals(0, tableSeats.get(3).getCommitted());
        assertTrue(tableSeats.get(3).isHasFolded());
        assertFalse(tableSeats.get(3).isHasActed());
        assertEquals("Eric", tableSeats.get(4).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("9c9s").getBitmask(), tableSeats.get(4).getCards());
        assertEquals(103, tableSeats.get(4).getStack());
        assertEquals(915, tableSeats.get(4).getCommitted());
        assertFalse(tableSeats.get(4).isHasFolded());
        assertTrue(tableSeats.get(4).isHasActed());
    }

    @Test
    public void parseRiver() throws NotationException {
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
                "Alice: calls 18\n" +
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
                "Eric: calls 915\n" +
                "*** RIVER *** [3c 8c 4s 5s] [Tc]";

        final var tableData = Parser.parse(LineParsers.createTexasHoldemLineParsers(), List.of(hand.split("\n")));

        assertEquals(5000, tableData.getChipsInPlay());

        assertEquals(7L, tableData.getHandId());
        assertEquals("Hold'em No Limit", tableData.getGameType());
        assertEquals(1, tableData.getSmallBlind());
        assertEquals(2, tableData.getBigBlind());
        assertEquals(TableData.GameStage.RIVER, tableData.getGameStage());
        assertEquals("3c8cTc4s5s", HandUtil.toNotation(tableData.getCards()));

        final var tableSeats = tableData.getSeats();
        assertEquals(5, tableSeats.size());
        assertEquals("Alice", tableSeats.get(0).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("AdJh").getBitmask(), tableSeats.get(0).getCards());
        assertEquals(55, tableSeats.get(0).getStack());
        assertEquals(0, tableSeats.get(0).getCommitted());
        assertFalse(tableSeats.get(0).isHasFolded());
        assertFalse(tableSeats.get(0).isHasActed());
        assertEquals("Bob", tableSeats.get(1).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6cKh").getBitmask(), tableSeats.get(1).getCards());
        assertEquals(986, tableSeats.get(1).getStack());
        assertEquals(0, tableSeats.get(1).getCommitted());
        assertTrue(tableSeats.get(1).isHasFolded());
        assertFalse(tableSeats.get(1).isHasActed());
        assertEquals("Charlie", tableSeats.get(2).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6d8d").getBitmask(), tableSeats.get(2).getCards());
        assertEquals(990, tableSeats.get(2).getStack());
        assertEquals(0, tableSeats.get(2).getCommitted());
        assertTrue(tableSeats.get(2).isHasFolded());
        assertFalse(tableSeats.get(2).isHasActed());
        assertEquals("David", tableSeats.get(3).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("Ac7d").getBitmask(), tableSeats.get(3).getCards());
        assertEquals(992, tableSeats.get(3).getStack());
        assertEquals(0, tableSeats.get(3).getCommitted());
        assertTrue(tableSeats.get(3).isHasFolded());
        assertFalse(tableSeats.get(3).isHasActed());
        assertEquals("Eric", tableSeats.get(4).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("9c9s").getBitmask(), tableSeats.get(4).getCards());
        assertEquals(103, tableSeats.get(4).getStack());
        assertEquals(0, tableSeats.get(4).getCommitted());
        assertFalse(tableSeats.get(4).isHasFolded());
        assertFalse(tableSeats.get(4).isHasActed());
    }

    @Test
    public void parseRiverAction() throws NotationException {
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
                "Alice: calls 18\n" +
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
                "Eric: calls 915\n" +
                "*** RIVER *** [3c 8c 4s 5s] [Tc]\n" +
                "Total pot: 1874\n" +
                "Alice: bets 55 and is all-in\n" +
                "Eric: calls 55";

        final var tableData = Parser.parse(LineParsers.createTexasHoldemLineParsers(), List.of(hand.split("\n")));

        assertEquals(5000, tableData.getChipsInPlay());

        assertEquals(7L, tableData.getHandId());
        assertEquals("Hold'em No Limit", tableData.getGameType());
        assertEquals(1, tableData.getSmallBlind());
        assertEquals(2, tableData.getBigBlind());
        assertEquals(TableData.GameStage.RIVER, tableData.getGameStage());
        assertEquals("3c8cTc4s5s", HandUtil.toNotation(tableData.getCards()));

        final var tableSeats = tableData.getSeats();
        assertEquals(5, tableSeats.size());
        assertEquals("Alice", tableSeats.get(0).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("AdJh").getBitmask(), tableSeats.get(0).getCards());
        assertEquals(0, tableSeats.get(0).getStack());
        assertEquals(55, tableSeats.get(0).getCommitted());
        assertFalse(tableSeats.get(0).isHasFolded());
        assertTrue(tableSeats.get(0).isHasActed());
        assertEquals("Bob", tableSeats.get(1).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6cKh").getBitmask(), tableSeats.get(1).getCards());
        assertEquals(986, tableSeats.get(1).getStack());
        assertEquals(0, tableSeats.get(1).getCommitted());
        assertTrue(tableSeats.get(1).isHasFolded());
        assertFalse(tableSeats.get(1).isHasActed());
        assertEquals("Charlie", tableSeats.get(2).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6d8d").getBitmask(), tableSeats.get(2).getCards());
        assertEquals(990, tableSeats.get(2).getStack());
        assertEquals(0, tableSeats.get(2).getCommitted());
        assertTrue(tableSeats.get(2).isHasFolded());
        assertFalse(tableSeats.get(2).isHasActed());
        assertEquals("David", tableSeats.get(3).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("Ac7d").getBitmask(), tableSeats.get(3).getCards());
        assertEquals(992, tableSeats.get(3).getStack());
        assertEquals(0, tableSeats.get(3).getCommitted());
        assertTrue(tableSeats.get(3).isHasFolded());
        assertFalse(tableSeats.get(3).isHasActed());
        assertEquals("Eric", tableSeats.get(4).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("9c9s").getBitmask(), tableSeats.get(4).getCards());
        assertEquals(48, tableSeats.get(4).getStack());
        assertEquals(55, tableSeats.get(4).getCommitted());
        assertFalse(tableSeats.get(4).isHasFolded());
        assertTrue(tableSeats.get(4).isHasActed());
    }

    @Test
    public void parseCollection() throws NotationException {
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
                "Alice: calls 18\n" +
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
                "Eric: calls 915\n" +
                "*** RIVER *** [3c 8c 4s 5s] [Tc]\n" +
                "Total pot: 1874\n" +
                "Alice: bets 55 and is all-in\n" +
                "Eric: calls 55\n" +
                "Eric collected 1874 from Main pot\n" +
                "Eric collected 110 from Side pot #1";

        final var tableData = Parser.parse(LineParsers.createTexasHoldemLineParsers(), List.of(hand.split("\n")));

        assertEquals(5000, tableData.getChipsInPlay());

        assertEquals(7L, tableData.getHandId());
        assertEquals("Hold'em No Limit", tableData.getGameType());
        assertEquals(1, tableData.getSmallBlind());
        assertEquals(2, tableData.getBigBlind());
        assertEquals(TableData.GameStage.RIVER, tableData.getGameStage());
        assertEquals("3c8cTc4s5s", HandUtil.toNotation(tableData.getCards()));

        final var tableSeats = tableData.getSeats();
        assertEquals(5, tableSeats.size());
        assertEquals("Alice", tableSeats.get(0).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("AdJh").getBitmask(), tableSeats.get(0).getCards());
        assertEquals(0, tableSeats.get(0).getStack());
        assertEquals(0, tableSeats.get(0).getCommitted());
        assertFalse(tableSeats.get(0).isHasFolded());
        assertTrue(tableSeats.get(0).isHasActed());
        assertEquals("Bob", tableSeats.get(1).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6cKh").getBitmask(), tableSeats.get(1).getCards());
        assertEquals(986, tableSeats.get(1).getStack());
        assertEquals(0, tableSeats.get(1).getCommitted());
        assertTrue(tableSeats.get(1).isHasFolded());
        assertFalse(tableSeats.get(1).isHasActed());
        assertEquals("Charlie", tableSeats.get(2).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6d8d").getBitmask(), tableSeats.get(2).getCards());
        assertEquals(990, tableSeats.get(2).getStack());
        assertEquals(0, tableSeats.get(2).getCommitted());
        assertTrue(tableSeats.get(2).isHasFolded());
        assertFalse(tableSeats.get(2).isHasActed());
        assertEquals("David", tableSeats.get(3).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("Ac7d").getBitmask(), tableSeats.get(3).getCards());
        assertEquals(992, tableSeats.get(3).getStack());
        assertEquals(0, tableSeats.get(3).getCommitted());
        assertTrue(tableSeats.get(3).isHasFolded());
        assertFalse(tableSeats.get(3).isHasActed());
        assertEquals("Eric", tableSeats.get(4).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("9c9s").getBitmask(), tableSeats.get(4).getCards());
        assertEquals(2032, tableSeats.get(4).getStack());
        assertEquals(0, tableSeats.get(4).getCommitted());
        assertFalse(tableSeats.get(4).isHasFolded());
        assertTrue(tableSeats.get(4).isHasActed());
    }

    @Test
    public void parseSummary() throws NotationException {
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
                "Alice: calls 18\n" +
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
                "Eric: calls 915\n" +
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

        assertEquals(5000, tableData.getChipsInPlay());

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
        assertFalse(tableSeats.get(0).isHasFolded());
        assertFalse(tableSeats.get(0).isHasActed());
        assertEquals("Bob", tableSeats.get(1).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6cKh").getBitmask(), tableSeats.get(1).getCards());
        assertEquals(986, tableSeats.get(1).getStack());
        assertEquals(0, tableSeats.get(1).getCommitted());
        assertTrue(tableSeats.get(1).isHasFolded());
        assertFalse(tableSeats.get(1).isHasActed());
        assertEquals("Charlie", tableSeats.get(2).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("6d8d").getBitmask(), tableSeats.get(2).getCards());
        assertEquals(990, tableSeats.get(2).getStack());
        assertEquals(0, tableSeats.get(2).getCommitted());
        assertTrue(tableSeats.get(2).isHasFolded());
        assertFalse(tableSeats.get(2).isHasActed());
        assertEquals("David", tableSeats.get(3).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("Ac7d").getBitmask(), tableSeats.get(3).getCards());
        assertEquals(992, tableSeats.get(3).getStack());
        assertEquals(0, tableSeats.get(3).getCommitted());
        assertTrue(tableSeats.get(3).isHasFolded());
        assertFalse(tableSeats.get(3).isHasActed());
        assertEquals("Eric", tableSeats.get(4).getPlayer().getPlayerData().getPlayerName());
        assertEquals(HandUtil.fromNotation("9c9s").getBitmask(), tableSeats.get(4).getCards());
        assertEquals(2032, tableSeats.get(4).getStack());
        assertEquals(0, tableSeats.get(4).getCommitted());
        assertFalse(tableSeats.get(4).isHasFolded());
        assertFalse(tableSeats.get(4).isHasActed());
    }
}
