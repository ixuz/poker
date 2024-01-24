package com.antwika.parser;

import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;

import java.util.regex.Pattern;

import static com.antwika.parser.Parser.findSeatByPlayerName;

public class CollectedPotLine implements ILine {
    public boolean parse(TableData tableData, String line) {
        final var m = Pattern.compile("(.+) collected (\\d+) from (.+)").matcher(line);
        if (m.find()) {
            final var playerName = m.group(1);
            final var amount = Integer.parseInt(m.group(2));
            final var potName = m.group(3);

            tableData.getSeats().forEach(seat -> seat.setCommitted(0));

            final var optionalSeat = findSeatByPlayerName(tableData, playerName);

            if (optionalSeat.isPresent()) {
                final var seat = optionalSeat.get();
                seat.setStack(seat.getStack() + amount);

                return true;
            }
            return true;
        }

        return false;
    }
}
