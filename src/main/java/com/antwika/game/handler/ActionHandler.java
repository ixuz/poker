package com.antwika.game.handler;

import com.antwika.game.event.IEvent;
import com.antwika.game.event.EventHandler;
import com.antwika.game.event.PlayerActionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ActionHandler extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(ActionHandler.class);

    private static final List<IActionHandler> actionHandlers = List.of(
            new FoldActionHandler(),
            new CheckActionHandler(),
            new CallActionHandler(),
            new BetActionHandler(),
            new RaiseActionHandler(),
            new ShowdownHandler()
    );

    public static void handleEvent(IEvent event) {
        //if (event instanceof PlayerActionResponse action) {
            boolean handled = false;
            for (IActionHandler actionHandler : actionHandlers) {
                if (actionHandler.canHandle(event)) {
                    actionHandler.handle(event);
                    handled = true;
                }
            }

            if (!handled) {
                logger.warn("No action handler could handle the player's action response");
            }
        //}
    }
}
