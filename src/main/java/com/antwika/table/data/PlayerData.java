package com.antwika.table.data;

import com.antwika.table.common.Prng;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@Data
@AllArgsConstructor
public class PlayerData {
    @ToString.Include
    private final String playerName;
    private final Prng prng;
}
