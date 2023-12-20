package com.antwika.game.util;

import com.antwika.game.actor.Dealer;
import com.antwika.game.core.IActor;
import com.antwika.game.exception.GameException;
import com.antwika.game.exception.TableException;

public class DealerUtil {
    public static boolean isSeatAvailable(Dealer dealer, int seatIndex) {
        return TableDataUtil.isSeatAvailable(dealer.getTable(), seatIndex);
    }

    public static boolean isPlayerSeated(Dealer dealer, IActor actor) {
        return TableDataUtil.isPlayerSeated(dealer.getTable(), actor);
    }

    public static void join(Dealer dealer, int seatIndex, IActor actor) throws GameException {
        try {
            TableDataUtil.seatPlayer(dealer.getTable(), actor, seatIndex);
        } catch (TableException e) {
            throw new GameException();
        }
    }

    public static void leave(Dealer dealer, IActor actor) throws GameException {
        try {
            TableDataUtil.unseatPlayer(dealer.getTable(), actor);
        } catch (TableException e) {
            throw new GameException();
        }
    }
}
