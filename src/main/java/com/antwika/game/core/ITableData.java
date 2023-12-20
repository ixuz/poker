package com.antwika.game.core;

import com.antwika.game.actor.Dealer;

import java.util.List;

public interface ITableData {
    Dealer getDealer();
    void setDealer(Dealer dealer);
    List<ISeat> getSeats();
    void setSeats(List<ISeat> seats);
    int getSmallBlind();
    void setSmallBlind(int smallBlind);
    int getBigBlind();
    void setBigBlind(int bigBlind);
    List<IPot> getPots();
    void setPots(List<IPot> pots);
    int getButtonAt();
    void setButtonAt(int buttonAt);
    int getActionAt();
    void setActionAt(int actionAt);
}
