package com.antwika.eval.util;

import com.antwika.eval.core.IHandProcessorData;
import com.antwika.common.util.BitmaskUtil;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HandProcessorDataUtilTest {
    @Test
    @Tag("UnitTest")
    public void getKicker() {
        IHandProcessorData data = mock(IHandProcessorData.class);
        int[] kickers = new int[]{ 1 };
        when(data.getKickers()).thenReturn(kickers);
        assertEquals(1, HandProcessorDataUtil.getKicker(data, 0));
    }

    @Test
    public void configureHandType() {
        final IHandProcessorData data = HandProcessorDataUtil.createHandProcessorData(0L, 0);

        HandProcessorDataUtil.configureHandType(data, BitmaskUtil.HIGH_CARD, true);
        assertEquals(0b000000001L, data.getHandType());

        HandProcessorDataUtil.configureHandType(data, BitmaskUtil.PAIR, true);
        assertEquals(0b000000011L, data.getHandType());

        HandProcessorDataUtil.configureHandType(data, BitmaskUtil.TWO_PAIR, true);
        assertEquals(0b000000111L, data.getHandType());

        HandProcessorDataUtil.configureHandType(data, BitmaskUtil.TRIPS, true);
        assertEquals(0b000001111L, data.getHandType());

        HandProcessorDataUtil.configureHandType(data, BitmaskUtil.STRAIGHT, true);
        assertEquals(0b000011111L, data.getHandType());

        HandProcessorDataUtil.configureHandType(data, BitmaskUtil.FLUSH, true);
        assertEquals(0b000111111L, data.getHandType());

        HandProcessorDataUtil.configureHandType(data, BitmaskUtil.FULL_HOUSE, true);
        assertEquals(0b001111111L, data.getHandType());

        HandProcessorDataUtil.configureHandType(data, BitmaskUtil.QUADS, true);
        assertEquals(0b011111111L, data.getHandType());

        HandProcessorDataUtil.configureHandType(data, BitmaskUtil.STRAIGHT_FLUSH, true);
        assertEquals(0b111111111L, data.getHandType());

        HandProcessorDataUtil.configureHandType(data, BitmaskUtil.HIGH_CARD, false);
        assertEquals(0b111111110L, data.getHandType());

        HandProcessorDataUtil.configureHandType(data, BitmaskUtil.PAIR, false);
        assertEquals(0b111111100L, data.getHandType());

        HandProcessorDataUtil.configureHandType(data, BitmaskUtil.TWO_PAIR, false);
        assertEquals(0b111111000L, data.getHandType());

        HandProcessorDataUtil.configureHandType(data, BitmaskUtil.TRIPS, false);
        assertEquals(0b111110000L, data.getHandType());

        HandProcessorDataUtil.configureHandType(data, BitmaskUtil.STRAIGHT, false);
        assertEquals(0b111100000L, data.getHandType());

        HandProcessorDataUtil.configureHandType(data, BitmaskUtil.FLUSH, false);
        assertEquals(0b111000000L, data.getHandType());

        HandProcessorDataUtil.configureHandType(data, BitmaskUtil.FULL_HOUSE, false);
        assertEquals(0b110000000L, data.getHandType());

        HandProcessorDataUtil.configureHandType(data, BitmaskUtil.QUADS, false);
        assertEquals(0b100000000L, data.getHandType());

        HandProcessorDataUtil.configureHandType(data, BitmaskUtil.STRAIGHT_FLUSH, false);
        assertEquals(0b000000000L, data.getHandType());
    }
}
