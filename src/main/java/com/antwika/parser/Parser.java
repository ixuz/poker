package com.antwika.parser;

import com.antwika.parser.parsers.ILineParser;
import com.antwika.table.data.SeatData;
import com.antwika.table.data.TableData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class Parser {
    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    public static TableData parse(ILineParser lineParser, String line) {
        final var tableData = new TableData();

        boolean handled = false;

        if (lineParser.parse(tableData, line)) {
            handled = true;
        }

        if (!handled) {
            logger.warn("No suitable LineParser for: '{}'", line);
        }

        return tableData;
    }

    public static Optional<SeatData> findSeatByPlayerName(TableData tableData, String playerName) {
        return tableData.getSeats().stream()
                .filter(seat -> seat.getPlayer().getPlayerData().getPlayerName().equals(playerName))
                .findFirst();
    }
}
