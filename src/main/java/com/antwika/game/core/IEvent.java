package com.antwika.game.core;

import com.antwika.game.World;

public interface IEvent {
    World getWorld();
    String getType();
}
