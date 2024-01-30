package com.antwika.fsm.transition;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.fsm.state.FSMState;
import com.antwika.handhistory.line.*;
import com.antwika.table.data.DeckData;
import com.antwika.table.data.SeatData;
import com.antwika.table.data.TableData;
import com.antwika.table.event.IEvent;
import com.antwika.table.util.DeckUtil;
import com.antwika.table.util.TableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StartHandTransition extends Transition {
    private static final Logger logger = LoggerFactory.getLogger(StartHandTransition.class);

    public StartHandTransition(FSMState fromState, FSMState toState) {
        super("StartHandTransition", fromState, toState);
    }

    @Override
    public boolean checkCondition(Object data) {
        final TableData tableData = (TableData) data;

        if (!tableData.getGameStage().equals(TableData.GameStage.NONE)) return false;
        if (TableUtil.anyPlayerHasCards(tableData)) return false;
        if (TableUtil.anyPlayerHasCommittedChips(tableData)) return false;
        if (TableUtil.numberOfPlayersWithNonZeroStack(tableData) < 2) return false;

        return true;
    }

    @Override
    public void onTransition(Object data) {
        final TableData tableData = (TableData) data;
        tableData.getPots().clear();
        tableData.setHandId(tableData.getHandId() + 1L);
        tableData.setCards(0L);
        tableData.setDelivered(0);
        tableData.setTotalBet(tableData.getBigBlind());
        tableData.setLastRaise(tableData.getBigBlind());
        tableData.setChipsInPlay(TableUtil.countAllStacks(tableData));

        TableUtil.resetAllSeats(tableData);

        TableUtil.unseat(tableData, TableUtil.findAllBustedSeats(tableData));

        tableData.setGameStage(TableData.GameStage.HAND_BEGUN);

        logger.info("--- HAND BEGIN ---");
        tableData.getHistory().add(new HandBeginLine());

        logger.info("Poker Hand #{}: {} ({}/{}) - {}",
                tableData.getHandId(),
                tableData.getGameType(),
                tableData.getSmallBlind(),
                tableData.getBigBlind(),
                new Date());
        tableData.getHistory().add(new GameInfoLine(
                tableData.getHandId(),
                tableData.getGameType(),
                tableData.getSmallBlind(),
                tableData.getBigBlind(),
                new Date().toString()
        ));

        DeckUtil.resetAndShuffle(tableData.getDeckData());

        logger.info("Table '{}' {}-max Seat #{} is the button",
                tableData.getTableName(),
                tableData.getSeats().size(),
                tableData.getButtonAt() + 1);
        tableData.getHistory().add(new TableInfoLine(
                tableData.getTableName(),
                tableData.getSeats().size(),
                tableData.getButtonAt() + 1
        ));

        for (SeatData seat : tableData.getSeats()) {
            if (seat.getPlayer() == null) continue;

            logger.info("Seat {}: {} ({} in chips) ",
                    seat.getSeatIndex() + 1,
                    seat.getPlayer().getPlayerData().getPlayerName(),
                    seat.getStack());
            tableData.getHistory().add(new SeatInfoLine(
                    seat.getSeatIndex() + 1,
                    seat.getPlayer().getPlayerData().getPlayerName(),
                    seat.getStack()
            ));
        }

        TableUtil.forcePostBlinds(tableData, List.of(tableData.getSmallBlind(), tableData.getBigBlind()));

        final List<SeatData> seats = tableData.getSeats();
        final DeckData deckData = tableData.getDeckData();
        logger.info("*** HOLE CARDS ***");
        tableData.getHistory().add(new HolecardsHeaderLine());
        for (int i = 0; i < seats.size() * 2; i += 1) {
            final int seatIndex = (tableData.getActionAt() + i + 1) % seats.size();
            final SeatData seat = seats.get(seatIndex);
            if (seat.getPlayer() == null) continue;
            final Long card = DeckUtil.draw(deckData);
            if (card == null) throw new RuntimeException("The card drawn from the deck is null");
            seat.setCards(seat.getCards() | card);
            try {
                logger.debug("Deal card {} to {}", HandUtil.toNotation(card), seat.getPlayer());
            } catch (NotationException e) {
                throw new RuntimeException(e);
            }
        }

        for (SeatData seat : tableData.getSeats()) {
            if (seat.getPlayer() == null) continue;

            final long cards = seat.getCards();

            if (Long.bitCount(cards) != 2) throw new RuntimeException("Unexpected number of cards after deal");

            try {
                final var playerName = seat.getPlayer().getPlayerData().getPlayerName();
                final var notation = HandUtil.toNotation(seat.getCards());
                final var card1 = HandUtil.fromNotation(notation.substring(0, 2)).getBitmask();
                final var card2 = HandUtil.fromNotation(notation.substring(2, 4)).getBitmask();
                logger.info("Dealt to {} [{}{}]", playerName, HandUtil.toNotation(card1), HandUtil.toNotation(card2));
                tableData.getHistory().add(new HolecardsLine(
                        seat.getPlayer().getPlayerData().getPlayerName(),
                        card1,
                        card2
                ));
            } catch (NotationException e) {
                throw new RuntimeException(e);
            }
        }

        // final SeatData nextSeatToAct = TableUtil.findNextSeatToAct(tableData, tableData.getActionAt(), 0, true);
        // tableData.setActionAt(nextSeatToAct.getSeatIndex());

        tableData.setButtonAt(TableUtil.findNextSeatToAct(tableData, tableData.getButtonAt(), 0, false).getSeatIndex());

        tableData.setGameStage(TableData.GameStage.PREFLOP);
    }
}
