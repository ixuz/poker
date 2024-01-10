package com.antwika.eval.util;

import com.antwika.eval.core.IHandProcessorData;
import com.antwika.eval.data.HandProcessorData;
import com.antwika.common.util.BitmaskUtil;

import java.util.Arrays;

public class HandProcessorDataUtil {
    public static IHandProcessorData createHandProcessorData(long hand, int kickersSize) {
        int[] kickers = new int[kickersSize];
        Arrays.fill(kickers, -1);
        return HandProcessorData.builder()
                .hand(hand)
                .handType(BitmaskUtil.NONE)
                .kickers(kickers)
                .highCardKickerCount(0)
                .build();
    }

    public static int getKicker(IHandProcessorData data, int offset) {
        return data.getKickers()[offset];
    }

    public static boolean hasKicker(IHandProcessorData data, int offset) {
        return data.getKickers()[offset] != -1;
    }

    public static void setKicker(IHandProcessorData data, int offset, int value) {
        data.getKickers()[offset] = value;
    }

    public static boolean isKicker(IHandProcessorData data, int offset, int value) {
        return data.getKickers()[offset] == value;
    }

    public static boolean isNotKicker(IHandProcessorData data, int offset, int value) {
        return !isKicker(data, offset, value);
    }

    public static void configureHandType(IHandProcessorData data, long handType, boolean enable) {
        if (enable) {
            data.setHandType(data.getHandType() | handType);
        } else {
            data.setHandType(data.getHandType() & ~handType);
        }
    }

    public static boolean isHandType(IHandProcessorData data, long handType) {
        return (data.getHandType() & handType) > 0L;
    }

    public static void setHighCardKickerCount(IHandProcessorData data, int highCardKickerCount) {
        configureHandType(data, BitmaskUtil.HIGH_CARD, highCardKickerCount > 0);
        data.setHighCardKickerCount(highCardKickerCount);
    }
}
