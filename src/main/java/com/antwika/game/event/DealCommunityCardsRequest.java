package com.antwika.game.event;

import com.antwika.game.data.GameData;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DealCommunityCardsRequest implements IEvent {
    private GameData gameData;
    private int count;
}
