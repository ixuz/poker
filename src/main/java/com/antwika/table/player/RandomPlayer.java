package com.antwika.table.player;

import com.antwika.table.data.TableData;
import com.antwika.table.data.SeatData;
import com.antwika.table.event.IEvent;
import com.antwika.table.event.player.PlayerActionRequest;
import com.antwika.table.event.player.PlayerActionResponse;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomPlayer extends Player {
    private static final Logger logger = LoggerFactory.getLogger(RandomPlayer.class);

    public RandomPlayer(long prngSeed, String playerName) {
        super(prngSeed, playerName);
    }

    @Override
    protected IEvent onPlayerActionRequest(PlayerActionRequest event) {
        if (event.getPlayer() != this) return null;
        logger.debug("event: { toCall: {}, minBet: {}, minRaise: {} }", event.getToCall(), event.getMinBet(), event.getMinRaise());

        int rand = getPlayerData().getPrng().nextInt(100);

        final TableData tableData = event.getTableData();
        final SeatData seat = TableUtil.getSeat(tableData, this);
        boolean canRaise = event.getToCall() < seat.getStack();

        if (event.getToCall() == 0) {
            if (rand < 75) {
                return new PlayerActionResponse(this, tableData, PlayerActionResponse.Type.CHECK, 0);
            } else {
                return new PlayerActionResponse(this, tableData, PlayerActionResponse.Type.BET, TableUtil.calcBetSize(tableData, this, 0.75f));
            }
        } else {
            if (rand < 50) {
                return new PlayerActionResponse(this, tableData, PlayerActionResponse.Type.FOLD, 0);
            } else if (canRaise && rand < 75) {
                return new PlayerActionResponse(this, tableData, PlayerActionResponse.Type.RAISE, TableUtil.calcBetSize(tableData, this, 0.75f));
            } else {
                return new PlayerActionResponse(this, tableData, PlayerActionResponse.Type.CALL, event.getToCall());
            }
        }
    }

    @Override
    protected void preEventHandle() {

    }

    @Override
    protected void noEventHandle() {

    }
}
