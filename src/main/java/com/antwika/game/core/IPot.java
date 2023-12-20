package com.antwika.game.core;

import java.util.List;

public interface IPot {
    List<IActor> getActors();
    void setActors(List<IActor> actors);
    int getAmount();
    void setAmount(int amount);
}
