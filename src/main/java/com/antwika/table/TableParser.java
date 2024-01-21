package com.antwika.table;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.table.data.SeatData;
import com.antwika.table.data.TableData;
import com.antwika.table.player.RandomPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class TableParser {
    public static TableData parse(List<String> lines) throws NotationException {
        final var builder = TableData.builder();

        for (var line : lines) {
            tryParseHandBeginTitle(builder, line);
            tryParseGameInfo(builder, line);
            tryParseTableInfo(builder, line);
        }

        final var tableData = builder.build();

        for (var line : lines) {
            tryParseSeatInfo(tableData, line);
            tryParsePostedBlind(tableData, line);
            tryParseHolecardsHeader(tableData, line);
            tryParseHolecards(tableData, line);
        }

        return tableData;
    }

    public static void tryParseHandBeginTitle(TableData.TableDataBuilder builder, String line) {
        final var m = Pattern.compile("--- HAND BEGIN ---").matcher(line);
        if (m.find()) {
            builder.gameStage(TableData.GameStage.PREFLOP);
        }
    }

    public static void tryParseGameInfo(TableData.TableDataBuilder builder, String line) {
        final var m = Pattern.compile("Poker Hand #(\\d+): (.+) \\((\\d+)/(\\d+)\\) - (.*)").matcher(line);
        if (m.find()) {
            final var handId = m.group(1);
            final var gameType = m.group(2);
            final var smallBlind = m.group(3);
            final var bigBlind = m.group(4);
            final var timestamp = m.group(5);
            builder.handId(Integer.parseInt(handId));
            builder.gameType(gameType);
            builder.smallBlind(Integer.parseInt(smallBlind));
            builder.bigBlind(Integer.parseInt(bigBlind));
        }
    }

    public static void tryParseTableInfo(TableData.TableDataBuilder builder, String line) {
        final var m = Pattern.compile("Table '(.+)' (\\d+)-max Seat #(\\d+) is the button").matcher(line);
        if (m.find()) {
            final var tableName = m.group(1);
            final var seatCount = m.group(2);
            final var buttonAt = m.group(3);

            final var seats = new ArrayList<SeatData>();
            for (int i = 0; i < Integer.parseInt(seatCount); i += 1) {
                final var seat = new SeatData();
                seat.setSeatIndex(i);
                seats.add(seat);
            }

            builder.tableName(tableName);
            builder.seats(seats);
            builder.buttonAt(Integer.parseInt(buttonAt));
        }
    }

    public static void tryParseSeatInfo(TableData tableData, String line) {
        final var m = Pattern.compile("Seat (\\d+): (.+) \\((\\d+) in chips\\)").matcher(line);
        if (m.find()) {
            final var seatId = m.group(1);
            final var playerName = m.group(2);
            final var stack = m.group(3);

            final var player = new RandomPlayer(1L, playerName);
            final var seat = tableData.getSeats().get(Integer.parseInt(seatId) - 1);
            seat.setPlayer(player);
            seat.setStack(Integer.parseInt(stack));
        }
    }

    public static void tryParsePostedBlind(TableData tableData, String line) {
        final var m = Pattern.compile("(.+): posts (.+) (\\d+)").matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            final var blindName = m.group(2);
            final var amount = Integer.parseInt(m.group(3));

            final var optionalSeat = findSeatByPlayerName(tableData, playerName);

            if (optionalSeat.isPresent()) {
                final var seat = optionalSeat.get();
                seat.setStack(seat.getStack() - amount);
                seat.setCommitted(seat.getCommitted() + amount);
            }
        }
    }

    public static boolean tryParseHolecardsHeader(TableData tableData, String line) {
        return Pattern.compile("\\*\\*\\* HOLE CARDS \\*\\*\\*").matcher(line).find();
    }

    public static void tryParseHolecards(TableData tableData, String line) throws NotationException {
        final var m = Pattern.compile("Dealt to (.+) \\[(..) (..)]").matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            final var firstCardNotation = m.group(2);
            final var secondCardNotation = m.group(3);

            final var optionalSeat = findSeatByPlayerName(tableData, playerName);

            if (optionalSeat.isPresent()) {
                final var seat = optionalSeat.get();
                seat.setCards(HandUtil.fromNotation(firstCardNotation.concat(secondCardNotation)).getBitmask());
            }
        }
    }

    private static Optional<SeatData> findSeatByPlayerName(TableData tableData, String playerName) {
        return tableData.getSeats().stream()
                .filter(seat -> seat.getPlayer().getPlayerData().getPlayerName().equals(playerName))
                .findFirst();
    }
}
