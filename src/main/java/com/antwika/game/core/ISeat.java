package com.antwika.game.core;

public interface ISeat {
    IActor getActor();
    void setActor(IActor actor);
    int getStack();
    void setStack(int stack);
    int getCommitted();
    void setCommitted(int committed);
}
