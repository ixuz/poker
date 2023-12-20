package com.antwika.eval.util;

import com.antwika.eval.core.IHandData;

public class HandDataUtil {
    public static boolean isHandType(IHandData handData, long handType) {
        return (handData.getHandType() & handType) > 0L;
    }
}
