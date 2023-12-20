package com.antwika.game.util;

import com.antwika.game.core.*;
import com.antwika.game.data.SeatData;
import com.antwika.game.data.TableData;
import com.antwika.game.exception.TableException;

import java.util.ArrayList;
import java.util.List;

public class TableDataUtil {
    public static ITableData createTableData(int seatCount, int smallBlind, int bigBlind, int buttonAt, int actionAt) {
        final List<ISeat> seats = new ArrayList<>();

        for (int i = 0; i < seatCount; i += 1) {
            seats.add(SeatData.builder().build());
        }

        return TableData.builder()
                .seats(seats)
                .pots(new ArrayList<>())
                .smallBlind(smallBlind)
                .bigBlind(bigBlind)
                .buttonAt(buttonAt)
                .actionAt(actionAt)
                .build();
    }

    public static boolean isSeatAvailable(ITableData table, int seatIndex) {
        return table.getSeats().get(seatIndex).getActor() == null;
    }

    public static int findFirstAvailableSeatIndex(ITableData table) {
        final List<ISeat> seats = table.getSeats();
        for (int seatIndex = 0; seatIndex < seats.size(); seatIndex += 1) {
            if (isSeatAvailable(table, seatIndex)) {
                return seatIndex;
            }
        }
        return -1;
    }

    public static boolean isPlayerSeated(ITableData table, IActor actor) {
        for (ISeat seat : table.getSeats()) {
            if (seat.getActor() == actor) {
                return true;
            }
        }
        return false;
    }

    public static void seatPlayer(ITableData table, IActor actor, int seatIndex) throws TableException {
        if (isPlayerSeated(table, actor)) throw new TableException();
        if (!isSeatAvailable(table, seatIndex)) throw new TableException();
        table.getSeats().get(seatIndex).setActor(actor);
    }

    public static void unseatPlayer(ITableData table, IActor actor) throws TableException {
        boolean unseatedPlayer = false;
        for (ISeat seat : table.getSeats()) {
            if (seat.getActor() == actor) {
                seat.setActor(null);
                unseatedPlayer = true;
            }
        }
        if (!unseatedPlayer) throw new TableException();
    }

    public static void addChipsToSeat(ITableData table, IActor actor, int seatIndex, int amount) throws TableException {
        if (!isPlayerSeated(table, actor)) throw new TableException();

        final ISeat seat = table.getSeats().get(seatIndex);
        final IActor seatedActor = seat.getActor();

        if (seatedActor != actor) throw new TableException();

        seat.setStack(seat.getStack() + amount);
    }
}
