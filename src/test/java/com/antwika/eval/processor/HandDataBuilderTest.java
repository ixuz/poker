package com.antwika.eval.processor;

import com.antwika.eval.core.IHandData;
import com.antwika.eval.core.IHandProcessorData;
import com.antwika.eval.data.HandData;
import com.antwika.eval.util.HandProcessorDataUtil;
import com.antwika.eval.util.HandProcessorUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HandDataBuilderTest {
    @Test
    public void setGetHand() {
        final Map<HandData, List<Object>> constructionHandDataArgs = new HashMap<>();
        try (MockedConstruction<HandData> constructionHandData = mockConstruction(
                HandData.class,
                (mock, context) -> constructionHandDataArgs.put(mock, new ArrayList<>(context.arguments()))
        )) {
            // Arrange
            final IHandProcessorData data = HandProcessorDataUtil.createHandProcessorData(0L, 0);

            // Act
            data.setHand(1L);
            final IHandData handData = HandProcessorUtil.toHandData(data);

            // Assert
            assertEquals(1L, data.getHand());
            assertEquals(1, constructionHandData.constructed().size());
            final IHandData mockHandData = constructionHandData.constructed().get(0);
            assertEquals(mockHandData, handData);
            assertEquals(4, constructionHandDataArgs.get(mockHandData).size());
            assertEquals(1L, constructionHandDataArgs.get(mockHandData).get(0));
            assertEquals(0, ((int[])constructionHandDataArgs.get(mockHandData).get(1)).length);
            assertEquals(0L, (long)constructionHandDataArgs.get(mockHandData).get(2));
            assertEquals(0, constructionHandDataArgs.get(mockHandData).get(3));
        }
    }

    @Test
    public void setGetKickers() {
        final Map<HandData, List<Object>> constructionHandDataArgs = new HashMap<>();
        try (MockedConstruction<HandData> constructionHandData = mockConstruction(
                HandData.class,
                (mock, context) -> constructionHandDataArgs.put(mock, new ArrayList<>(context.arguments()))
        )) {
            // Arrange
            final IHandProcessorData data = HandProcessorDataUtil.createHandProcessorData(0L, 3);

            // Act
            data.getKickers()[0] = 0;
            data.getKickers()[1] = 2;
            data.getKickers()[2] = 4;
            final IHandData handData = HandProcessorUtil.toHandData(data);

            // Assert
            assertEquals(0, data.getKickers()[0]);
            assertEquals(2, data.getKickers()[1]);
            assertEquals(4, data.getKickers()[2]);
            assertEquals(1, constructionHandData.constructed().size());
            final IHandData mockHandData = constructionHandData.constructed().get(0);
            assertEquals(mockHandData, handData);
            assertEquals(4, constructionHandDataArgs.get(mockHandData).size());
            assertEquals(0L, constructionHandDataArgs.get(mockHandData).get(0));
            assertEquals(3, ((int[])constructionHandDataArgs.get(mockHandData).get(1)).length);
            assertEquals(0, ((int[])constructionHandDataArgs.get(mockHandData).get(1))[0]);
            assertEquals(2, ((int[])constructionHandDataArgs.get(mockHandData).get(1))[1]);
            assertEquals(4, ((int[])constructionHandDataArgs.get(mockHandData).get(1))[2]);
            assertEquals(0L, (long)constructionHandDataArgs.get(mockHandData).get(2));
            assertEquals(0, constructionHandDataArgs.get(mockHandData).get(3));
        }
    }
}
