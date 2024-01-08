package com.antwika.game.handler;

import com.antwika.common.exception.NotationException;
import com.antwika.game.data.GameData;
import com.antwika.game.event.IEvent;
import com.antwika.game.event.ShowdownEvent;
import com.antwika.game.event.ShowdownRequest;
import com.antwika.game.util.GameDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShowdownEventHandler implements IHandler {
    private static final Logger logger = LoggerFactory.getLogger(ShowdownEventHandler.class);

    public boolean canHandle(IEvent event) {
        return event instanceof ShowdownEvent;
    }

    public List<IEvent> handle(IEvent event) {
        logger.info("--- HAND END ---");
        return null;
    }
}
