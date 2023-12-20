package com.antwika.eval.processor;

import com.antwika.eval.core.IHandProcessorData;
import com.antwika.common.util.BitmaskUtil;
import com.antwika.eval.util.HandProcessorDataUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FullHouseProcessorTest {
    @Test
    public void maxKickerCount() {
        final FullHouseProcessor fullHouseProcessor = new FullHouseProcessor();
        assertEquals(2, fullHouseProcessor.maxKickerCount());
    }

    @Test
    public void process_whenIsFullHouseIsAlreadyTrue_doNothing() {
        // Arrange
        try (MockedStatic<HandProcessorDataUtil> staticHandProcessorDataUtil = mockStatic(HandProcessorDataUtil.class)) {
            final FullHouseProcessor fullHouseProcessor = new FullHouseProcessor();
            final IHandProcessorData builder = mock(IHandProcessorData.class);

            staticHandProcessorDataUtil.when(() -> HandProcessorDataUtil.isHandType(builder, BitmaskUtil.FULL_HOUSE)).thenReturn(true);

            // Act
            fullHouseProcessor.process(builder, 0, 0, 0);

            // Assert
            verify(builder, times(0)).getHand();
            staticHandProcessorDataUtil.verify(() -> HandProcessorDataUtil.configureHandType(eq(builder), eq(BitmaskUtil.FULL_HOUSE), anyBoolean()), times(0));
            staticHandProcessorDataUtil.verify(() -> HandProcessorDataUtil.getKicker(any(), anyInt()), times(0));
            staticHandProcessorDataUtil.verify(() -> HandProcessorDataUtil.hasKicker(any(), anyInt()), times(0));
            staticHandProcessorDataUtil.verify(() -> HandProcessorDataUtil.setKicker(any(), anyInt(), anyInt()), times(0));
        }
    }
}
