package com.antwika.table.player;

import com.antwika.eval.core.IEvaluation;
import com.antwika.eval.core.IHandProcessor;
import com.antwika.eval.processor.TexasHoldemProcessor;
import com.antwika.eval.util.HandEvaluatorUtil;
import com.antwika.table.data.TableData;
import com.antwika.table.data.SeatData;
import com.antwika.table.event.IEvent;
import com.antwika.table.event.PlayerActionRequest;
import com.antwika.table.event.PlayerActionResponse;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.antwika.common.util.BitmaskUtil.*;

public class PremiumPlayer extends Player {
    private static final Logger logger = LoggerFactory.getLogger(PremiumPlayer.class);

    private final IHandProcessor handProcessor = new TexasHoldemProcessor();

    public PremiumPlayer(long prngSeed, String playerName) {
        super(prngSeed, playerName);
    }

    @Override
    protected IEvent onPlayerActionRequest(PlayerActionRequest event) {
        final TableData tableData = event.getTableData();
        final SeatData seat = TableUtil.getSeat(tableData, this);

        final long cards = seat.getCards();
        final int stack = seat.getStack();

        if (hasPremium(cards)) {
            if (event.getToCall() == 0) {
                if (hasGoodHand(tableData, cards)) {
                    return new PlayerActionResponse(this, tableData, PlayerActionResponse.Type.BET, TableUtil.calcBetSize(tableData, this, 1.0f));
                } else {
                    return new PlayerActionResponse(this, tableData, PlayerActionResponse.Type.CHECK, 0);
                }
            } else {
                if (hasGoodHand(tableData, cards)) {
                    return new PlayerActionResponse(this, tableData, PlayerActionResponse.Type.RAISE, TableUtil.calcBetSize(tableData, this, 1.0f));
                } else {
                    return new PlayerActionResponse(this, tableData, PlayerActionResponse.Type.FOLD, 0);
                }
            }
        } else {
            return new PlayerActionResponse(this, tableData, PlayerActionResponse.Type.FOLD, 0);
        }
    }

    private boolean hasPremium(long cards) {
        boolean hasPocketAces = Long.bitCount(cards & ACES) == 2;
        boolean hasPocketKings = Long.bitCount(cards & KINGS) == 2;
        boolean hasPocketQueens = Long.bitCount(cards & QUEENS) == 2;
        boolean hasPocketJacks = Long.bitCount(cards & JACKS) == 2;
        boolean hasPocketTens = Long.bitCount(cards & TENS) == 2;
        boolean hasAceKing = Long.bitCount(cards & ACES) == 1 && Long.bitCount(cards & KINGS) == 1;
        boolean hasAceQueen = Long.bitCount(cards & ACES) == 1 && Long.bitCount(cards & QUEENS) == 1;
        boolean hasAceJack = Long.bitCount(cards & ACES) == 1 && Long.bitCount(cards & JACKS) == 1;
        boolean hasKingQueen = Long.bitCount(cards & KINGS) == 1 && Long.bitCount(cards & QUEENS) == 1;

        if (hasPocketAces) return true;
        if (hasPocketKings) return true;
        if (hasPocketQueens) return true;
        if (hasPocketJacks) return true;
        if (hasPocketTens) return true;
        if (hasAceKing) return true;
        if (hasAceQueen) return true;
        if (hasAceJack) return true;
        if (hasKingQueen) return true;

        return false;
    }

    private boolean hasGoodHand(TableData tableData, long cards) {
        final long allCards = tableData.getCards() | cards;
        final IEvaluation evaluation = HandEvaluatorUtil.evaluate(handProcessor, allCards);
        if (evaluation.getHandType() >= PAIR) {
            return true;
        }

        if (Long.bitCount(tableData.getCards()) == 3) {
            return Long.bitCount(allCards & CLUBS) >= 3;
        } else if (Long.bitCount(tableData.getCards()) >= 4) {
            return Long.bitCount(allCards & CLUBS) >= 4;
        }

        return false;
    }

    @Override
    protected void preEventHandle() {

    }

    @Override
    protected void noEventHandle() {

    }
}
