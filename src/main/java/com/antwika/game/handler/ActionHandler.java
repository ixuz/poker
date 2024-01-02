package com.antwika.game.handler;

import com.antwika.game.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ActionHandler extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(ActionHandler.class);

    private List<IActionHandler> actionHandlers = List.of(
            new FoldActionHandler(),
            new CheckActionHandler(),
            new CallActionHandler(),
            new BetActionHandler(),
            new RaiseActionHandler()
    );

    public void handleEvent(Event event) {
        if (event instanceof PlayerActionResponse action) {
            boolean handled = false;
            for (IActionHandler actionHandler : actionHandlers) {
                if (actionHandler.canHandle(action)) {
                    actionHandler.handle(action);
                    handled = true;
                }
            }

            if (!handled) {
                logger.warn("No action handler could handle the player's action response");
            }
        }
    }
}
