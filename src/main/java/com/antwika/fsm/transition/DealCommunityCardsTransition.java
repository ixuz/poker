package com.antwika.fsm.transition;

import com.antwika.fsm.state.FSMState;
import com.antwika.table.data.TableData;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DealCommunityCardsTransition extends Transition {
    private static final Logger logger = LoggerFactory.getLogger(DealCommunityCardsTransition.class);

    public DealCommunityCardsTransition(FSMState fromState, FSMState toState) {
        super("DealCommunityCardsTransition", fromState, toState);
    }

    @Override
    public boolean checkCondition(Object data) {
        final TableData tableData = (TableData) data;

        if (!TableUtil.hasAllPlayersActed(tableData)) return false;
        if (TableUtil.getNumberOfPlayersLeftToAct(tableData) == 0) return false;
        if (TableUtil.countPlayersRemainingInHand(tableData) < 2) return false;
        if (tableData.getGameStage() == TableData.GameStage.NONE) return false;
        if (tableData.getGameStage() == TableData.GameStage.SHOWDOWN) return false;
        if (tableData.getGameStage() == TableData.GameStage.HAND_BEGUN) return false;
        if (tableData.getGameStage() == TableData.GameStage.RIVER) return false;

        return true;
    }

    @Override
    public void onTransition(Object data) {
        final TableData tableData = (TableData) data;

        if (tableData.getGameStage() == TableData.GameStage.PREFLOP) {
            TableUtil.dealCommunityCards(tableData, 3);
            tableData.setGameStage(TableData.GameStage.FLOP);
            TableUtil.prepareBettingRound(tableData);
        } else if (tableData.getGameStage() == TableData.GameStage.FLOP) {
            TableUtil.dealCommunityCards(tableData, 1);
            tableData.setGameStage(TableData.GameStage.TURN);
            TableUtil.prepareBettingRound(tableData);
        } else if (tableData.getGameStage() == TableData.GameStage.TURN) {
            TableUtil.dealCommunityCards(tableData, 1);
            tableData.setGameStage(TableData.GameStage.RIVER);
            TableUtil.prepareBettingRound(tableData);
        }
    }
}
