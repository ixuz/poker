package com.antwika.eval.processor;

import com.antwika.eval.core.IEvaluation;
import com.antwika.eval.core.IHandData;
import com.antwika.eval.core.IHandProcessorData;
import com.antwika.common.exception.NotationException;
import com.antwika.common.util.BitmaskUtil;
import com.antwika.eval.util.HandDataUtil;
import com.antwika.eval.util.HandProcessorDataUtil;
import com.antwika.common.util.HandUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static com.antwika.common.util.BitmaskUtil.EIGHT_INDEX;
import static com.antwika.common.util.BitmaskUtil.FLUSH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FlushProcessorTest {
    @Test
    public void maxKickerCount() {
        final FlushProcessor flushProcessor = new FlushProcessor();
        assertEquals(1, flushProcessor.maxKickerCount());
    }

    @Test
    public void process_whenIsFlushIsAlreadyTrue_doNothing() {
        // Arrange
        try (MockedStatic<HandProcessorDataUtil> staticHandProcessorDataUtil = mockStatic(HandProcessorDataUtil.class)) {
            final FlushProcessor flushProcessor = new FlushProcessor();
            final IHandProcessorData builder = mock(IHandProcessorData.class);

            staticHandProcessorDataUtil.when(() -> HandProcessorDataUtil.isHandType(builder, BitmaskUtil.FLUSH)).thenReturn(true);

            // Act
            flushProcessor.process(builder, 0, 0, 0);

            // Assert
            verify(builder, times(0)).getHand();
            staticHandProcessorDataUtil.verify(() -> HandProcessorDataUtil.configureHandType(eq(builder), eq(BitmaskUtil.FLUSH), anyBoolean()), times(0));
            staticHandProcessorDataUtil.verify(() -> HandProcessorDataUtil.setKicker(any(), anyInt(), anyInt()), times(0));
        }
    }

    @Test
    public void process_whenIsRankIndexIsLessThanFiveIndex_doNothing() {
        // Arrange
        try (MockedStatic<HandProcessorDataUtil> staticHandProcessorDataUtil = mockStatic(HandProcessorDataUtil.class)) {
            final FlushProcessor flushProcessor = new FlushProcessor();
            final IHandProcessorData builder = mock(IHandProcessorData.class);

            staticHandProcessorDataUtil.when(() -> HandProcessorDataUtil.isHandType(builder, BitmaskUtil.FLUSH)).thenReturn(false);

            // Act
            flushProcessor.process(builder, 0, 0, 0);

            // Assert
            verify(builder, times(0)).getHand();
            staticHandProcessorDataUtil.verify(() -> HandProcessorDataUtil.configureHandType(eq(builder), eq(BitmaskUtil.FLUSH), anyBoolean()), times(0));
            staticHandProcessorDataUtil.verify(() -> HandProcessorDataUtil.setKicker(any(), anyInt(), anyInt()), times(0));
        }
    }

    @Test
    public void process_findsFlush() throws NotationException {
        // Arrange
        try (MockedStatic<HandProcessorDataUtil> staticHandProcessorDataUtil = mockStatic(HandProcessorDataUtil.class)) {
            final FlushProcessor flushProcessor = new FlushProcessor();
            final IHandProcessorData builder = mock(IHandProcessorData.class);

            staticHandProcessorDataUtil.when(() -> HandProcessorDataUtil.isHandType(builder, BitmaskUtil.FLUSH)).thenReturn(false);
            when(builder.getHand()).thenReturn(HandUtil.fromNotation("2c4c6c8cTc").getBitmask());

            // Act
            flushProcessor.process(builder, EIGHT_INDEX, 10, 999);

            // Assert
            verify(builder, times(1)).getHand();
            staticHandProcessorDataUtil.verify(() -> HandProcessorDataUtil.configureHandType(builder, BitmaskUtil.FLUSH, true), times(1));
            staticHandProcessorDataUtil.verify(() -> HandProcessorDataUtil.setKicker(builder, 999, EIGHT_INDEX), times(1));
        }
    }

    @Test
    public void process_doesNotFindFlush() throws NotationException {
        // Arrange
        try (MockedStatic<HandProcessorDataUtil> staticHandProcessorDataUtil = mockStatic(HandProcessorDataUtil.class)) {
            final FlushProcessor flushProcessor = new FlushProcessor();
            final IHandProcessorData builder = mock(IHandProcessorData.class);

            staticHandProcessorDataUtil.when(() -> HandProcessorDataUtil.isHandType(builder, BitmaskUtil.FLUSH)).thenReturn(false);
            when(builder.getHand()).thenReturn(HandUtil.fromNotation("2c4c6c8dTd").getBitmask());

            // Act
            flushProcessor.process(builder, EIGHT_INDEX, 10, 999);

            // Assert
            verify(builder, times(1)).getHand();
            staticHandProcessorDataUtil.verify(() -> HandProcessorDataUtil.configureHandType(builder, BitmaskUtil.FLUSH, true), times(0));
            staticHandProcessorDataUtil.verify(() -> HandProcessorDataUtil.setKicker(builder, 999, EIGHT_INDEX), times(0));
        }
    }

    @Test
    public void toEvaluation_whenNotFlush_returnNull() {
        // Arrange
        try (MockedStatic<HandDataUtil> staticHandDataUtil = mockStatic(HandDataUtil.class)) {
            final FlushProcessor flushProcessor = new FlushProcessor();
            final IHandData handData = mock(IHandData.class);

            staticHandDataUtil.when(() -> HandDataUtil.isHandType(handData, FLUSH)).thenReturn(false);

            // Act
            final IEvaluation evaluation = flushProcessor.toEvaluation(handData, 999);

            // Assert
            assertNull(evaluation);
        }
    }

    @Test
    public void toEvaluation() {
        // Arrange
        try (MockedStatic<HandDataUtil> staticHandDataUtil = mockStatic(HandDataUtil.class)) {
            final FlushProcessor flushProcessor = new FlushProcessor();
            final IHandData handData = mock(IHandData.class);

            staticHandDataUtil.when(() -> HandDataUtil.isHandType(handData, FLUSH)).thenReturn(true);
            when(handData.getHand()).thenReturn(888L);
            when(handData.getKickers()).thenReturn(new int[]{1, 2, 3});

            // Act
            final IEvaluation evaluation = flushProcessor.toEvaluation(handData, 1);

            // Assert
            assertNotNull(evaluation);
            assertEquals(1, evaluation.getKickers().length);
            assertEquals(BitmaskUtil.FLUSH, evaluation.getHandType());
            assertEquals(2, evaluation.getKickers()[0]);
            assertEquals(1, evaluation.getKickersCount());
        }
    }
}
