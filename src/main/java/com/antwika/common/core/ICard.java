package com.antwika.common.core;

import java.io.Serializable;

public interface ICard extends Serializable {
    IRank getRank();
    ISuit getSuit();
}
