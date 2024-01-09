package com.antwika.table.handler;

import com.antwika.common.exception.NotationException;
import com.antwika.common.util.HandUtil;
import com.antwika.table.data.DeckData;
import com.antwika.table.data.TableData;
import com.antwika.table.data.SeatData;
import com.antwika.table.event.*;
import com.antwika.table.util.DeckUtil;
import com.antwika.table.util.TableDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HandBeginRequestHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(HandBeginRequestHandler.class);

    public boolean canHandle(IEvent event) {
        if (!(event instanceof HandBeginRequest handBeginRequest)) return false;

        final TableData.GameStage gameStage = handBeginRequest.getTableData().getGameStage();

        return gameStage.equals(TableData.GameStage.NONE);
    }

    public List<IEvent> handle(IEvent event) {
        try {
            final List<IEvent> additionalEvents = new ArrayList<>();

            final HandBeginRequest handBeginRequest = (HandBeginRequest) event;
            final TableData tableData = handBeginRequest.getTableData();

            TableDataUtil.prepareHand(tableData);

            additionalEvents.addAll(TableDataUtil.unseat(tableData, TableDataUtil.findAllBustedSeats(tableData)));

            TableDataUtil.resetAllSeats(tableData);

            tableData.setGameStage(TableData.GameStage.HAND_BEGUN);

            logger.info("--- HAND BEGIN ---");

            logger.info("Poker Hand #{}: {} ({}/{}) - {}",
                    tableData.getHandId(),
                    tableData.getGameType(),
                    tableData.getSmallBlind(),
                    tableData.getBigBlind(),
                    new Date());

            DeckUtil.resetAndShuffle(tableData.getDeckData());

            logger.info("Table '{}' {}-max Seat #{} is the button",
                    tableData.getTableName(),
                    tableData.getSeats().size(),
                    tableData.getButtonAt() + 1);

            for (SeatData seat : tableData.getSeats()) {
                if (seat.getPlayer() == null) continue;

                logger.info("Seat {}: {} ({} in chips) ",
                        seat.getSeatIndex() + 1,
                        seat.getPlayer().getPlayerData().getPlayerName(),
                        seat.getStack());
            }

            TableDataUtil.forcePostBlinds(tableData, List.of(tableData.getSmallBlind(), tableData.getBigBlind()));

            final List<SeatData> seats = tableData.getSeats();
            final DeckData deckData = tableData.getDeckData();
            logger.info("*** HOLE CARDS ***");
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

                logger.info("Dealt to {} [{}]", seat.getPlayer().getPlayerData().getPlayerName(), TableDataUtil.toNotation(seat.getCards()));
            }

            tableData.setGameStage(TableData.GameStage.PREFLOP);

            additionalEvents.add(new OrbitBeginRequest(tableData, 0));

            return additionalEvents;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
