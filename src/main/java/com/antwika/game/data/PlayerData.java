package com.antwika.game.data;

import com.antwika.game.common.Prng;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
@AllArgsConstructor
public class PlayerData {
    private static final Logger logger = LoggerFactory.getLogger(PlayerData.class);

    @ToString.Include
    @Getter
    private final String playerName;

    @Getter
    private final Prng prng;
}
