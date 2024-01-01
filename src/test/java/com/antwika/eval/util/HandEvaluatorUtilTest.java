package com.antwika.eval.util;

import com.antwika.eval.data.Evaluation;
import com.antwika.eval.core.IEvaluation;
import com.antwika.eval.core.IHandData;
import com.antwika.eval.core.IHandProcessor;
import com.antwika.eval.core.IHandProcessorData;
import com.antwika.eval.exception.HandEvaluatorException;
import com.antwika.common.exception.NotationException;
import com.antwika.eval.data.HandProcessorData;
import com.antwika.eval.processor.TexasHoldemProcessor;
import com.antwika.common.util.BitmaskUtil;
import com.antwika.common.util.HandUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.util.*;
import java.util.stream.Collectors;

import static com.antwika.common.util.BitmaskUtil.*;
import static com.antwika.common.util.BitmaskUtil.TWO_INDEX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class HandEvaluatorUtilTest {
    @Test
    @Tag("IntegrationTest")
    public void findStraightFlush() throws NotationException, HandEvaluatorException {
        final IHandProcessor handProcessor = new TexasHoldemProcessor();
        Assertions.assertEquals(BitmaskUtil.STRAIGHT_FLUSH, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("Ac2c3c4c5c7c2d")).getHandType());
        assertEquals(BitmaskUtil.STRAIGHT_FLUSH, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcKcQcJcTc7c2d")).getHandType());
    }

    @Test
    @Tag("IntegrationTest")
    public void findQuads() throws NotationException, HandEvaluatorException {
        final IHandProcessor handProcessor = new TexasHoldemProcessor();
        assertEquals(BitmaskUtil.QUADS, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcAdAhAsKd7c2d")).getHandType());
        assertEquals(BitmaskUtil.QUADS, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcAd5hKcKdKhKs")).getHandType());
        assertEquals(BitmaskUtil.QUADS, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("2c2d2h2sKd7c2d")).getHandType());
        assertEquals(BitmaskUtil.QUADS, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("2c2d5h3c3d3h3s")).getHandType());
    }

    @Test
    @Tag("IntegrationTest")
    public void findFullHouse() throws NotationException, HandEvaluatorException {
        final IHandProcessor handProcessor = new TexasHoldemProcessor();
        assertEquals(BitmaskUtil.FULL_HOUSE, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcAdAhKsKd7c2d")).getHandType());
        assertEquals(BitmaskUtil.FULL_HOUSE, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcAdKhKsKd7c2d")).getHandType());
        assertEquals(BitmaskUtil.FULL_HOUSE, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("2c2d2h3s3d7c2d")).getHandType());
        assertEquals(BitmaskUtil.FULL_HOUSE, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("2c2d3h3s3d7cAd")).getHandType());
    }

    @Test
    @Tag("IntegrationTest")
    public void findFlush() throws NotationException, HandEvaluatorException {
        final IHandProcessor handProcessor = new TexasHoldemProcessor();
        assertEquals(BitmaskUtil.FLUSH, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcQcTc8c4c6s2s")).getHandType());
        assertEquals(BitmaskUtil.FLUSH, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AdQdTd8d4d6s2s")).getHandType());
        assertEquals(BitmaskUtil.FLUSH, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AhQhTh8h4h6s2s")).getHandType());
        assertEquals(BitmaskUtil.FLUSH, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AsQsTs8s4s6c2c")).getHandType());
    }

    @Test
    @Tag("IntegrationTest")
    public void findStraight() throws NotationException, HandEvaluatorException {
        final IHandProcessor handProcessor = new TexasHoldemProcessor();
        assertEquals(BitmaskUtil.STRAIGHT, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcKdQhJsTc7d2h")).getHandType());
        assertEquals(BitmaskUtil.STRAIGHT, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcTd9h8s7c6d2h")).getHandType());
        assertEquals(BitmaskUtil.STRAIGHT, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("Ac2d3h4s5cQdTh")).getHandType());
    }

    @Test
    @Tag("IntegrationTest")
    public void findTrips() throws NotationException, HandEvaluatorException {
        final IHandProcessor handProcessor = new TexasHoldemProcessor();
        assertEquals(BitmaskUtil.TRIPS, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcAdAhQsTs7s2s")).getHandType());
        assertEquals(BitmaskUtil.TRIPS, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AsQcQdQhTs7s2s")).getHandType());
        assertEquals(BitmaskUtil.TRIPS, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AsQsTcTdTh7s2s")).getHandType());
        assertEquals(BitmaskUtil.TRIPS, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AsQsTs7c7d7h2s")).getHandType());
        assertEquals(BitmaskUtil.TRIPS, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AsQsTs7s2c2d2h")).getHandType());
    }

    @Test
    @Tag("IntegrationTest")
    public void findTwoPair() throws NotationException, HandEvaluatorException {
        final IHandProcessor handProcessor = new TexasHoldemProcessor();
        HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcAdKcKdQcQdJc"));
        assertEquals(BitmaskUtil.TWO_PAIR, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcAdKhKs7c5d2h")).getHandType());
        assertEquals(BitmaskUtil.TWO_PAIR, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcKcKd7h7s5d2h")).getHandType());
        assertEquals(BitmaskUtil.TWO_PAIR, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcKcKd7d5h5s2h")).getHandType());
        assertEquals(BitmaskUtil.TWO_PAIR, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcKcKd7d5h2h2s")).getHandType());
        assertEquals(BitmaskUtil.TWO_PAIR, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcAdKc7d5h2h2s")).getHandType());
        assertEquals(BitmaskUtil.TWO_PAIR, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcAdKcKdQcQdJc")).getHandType());
        assertEquals(BitmaskUtil.TWO_PAIR, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcKcKdQcQdJc9d")).getHandType());
    }

    @Test
    @Tag("IntegrationTest")
    @Tag("RegressionTest")
    public void findTwoPairKickers() throws NotationException, HandEvaluatorException {
        final IHandProcessor handProcessor = new TexasHoldemProcessor();
        assertEquals(3, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcAdKcKdQcQdJc")).getKickersCount());
        assertEquals(ACE_INDEX, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcAdKcKdQcQdJc")).getKickers()[0]);
        assertEquals(KING_INDEX, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcAdKcKdQcQdJc")).getKickers()[1]);
        assertEquals(QUEEN_INDEX, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcAdKcKdQcQdJc")).getKickers()[2]);
    }

    @Test
    @Tag("IntegrationTest")
    public void findPair() throws NotationException, HandEvaluatorException {
        final IHandProcessor handProcessor = new TexasHoldemProcessor();
        assertEquals(BitmaskUtil.PAIR, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcAdTc8d6h4s2c")).getHandType());
        assertEquals(BitmaskUtil.PAIR, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcTcTd8d6h4s2c")).getHandType());
        assertEquals(BitmaskUtil.PAIR, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcTd8c8d6h4s2c")).getHandType());
        assertEquals(BitmaskUtil.PAIR, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcTd8h6c6d4s2c")).getHandType());
        assertEquals(BitmaskUtil.PAIR, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcTd8h6s4c4d2c")).getHandType());
        assertEquals(BitmaskUtil.PAIR, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcTd8h6s4c2c2d")).getHandType());
    }

    @Test
    @Tag("IntegrationTest")
    @Tag("RegressionTest")
    public void findPairKickers() throws NotationException, HandEvaluatorException {
        final IHandProcessor handProcessor = new TexasHoldemProcessor();
        assertEquals(4, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcQdQh8s6c")).getKickersCount());
        assertEquals(QUEEN_INDEX, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcQdQh8s6c")).getKickers()[0]);
        assertEquals(ACE_INDEX, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcQdQh8s6c")).getKickers()[1]);
        assertEquals(EIGHT_INDEX, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcQdQh8s6c")).getKickers()[2]);
        assertEquals(SIX_INDEX, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcQdQh8s6c")).getKickers()[3]);
    }

    @Test
    @Tag("IntegrationTest")
    public void findHighCard() throws NotationException, HandEvaluatorException {
        final IHandProcessor handProcessor = new TexasHoldemProcessor();
        HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("Ac8d7h6s4c3d2h"));
        assertEquals(BitmaskUtil.HIGH_CARD, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("AcQdTh8s4c6d2h")).getHandType());
        assertEquals(BitmaskUtil.HIGH_CARD, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("KcQdTh8s4c6d2h")).getHandType());
        assertEquals(BitmaskUtil.HIGH_CARD, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("Tc9d8h5s4c3d2h")).getHandType());
        assertEquals(BitmaskUtil.HIGH_CARD, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("Ac8d7h6s4c3d2h")).getHandType());
    }

    @Test
    @Tag("IntegrationTest")
    public void findNone() throws NotationException, HandEvaluatorException {
        final IHandProcessor handProcessor = new TexasHoldemProcessor();
        assertEquals(BitmaskUtil.NONE, HandEvaluatorUtil.evaluate(handProcessor, HandUtil.fromNotation("")).getHandType());
    }

    @Test
    @Tag("UnitTest")
    public void evaluate_processorReturnsNullEvaluation_returnToEmptyEvaluation() throws NotationException, HandEvaluatorException {
        final IHandData mockHandData = mock(IHandData.class);
        final List<IEvaluation> constructionEvaluationInstances = new ArrayList<>();
        final Map<IEvaluation, List<?>> constructionEvaluationArgs = new HashMap<>();
        try (MockedConstruction<HandProcessorData> constructionHandDataBuilder = mockConstruction(HandProcessorData.class);
             MockedConstruction<Evaluation> constructionEvaluation = mockConstruction(Evaluation.class, (mock, context) -> {
                 constructionEvaluationInstances.add(mock);
                 constructionEvaluationArgs.put(mock, context.arguments());
             });
             MockedStatic<HandProcessorUtil> staticHandProcessorUtil = mockStatic(HandProcessorUtil.class)
        ) {
            // Arrange
            final IHandProcessor mockHandProcessor = mock(IHandProcessor.class);
            when(mockHandProcessor.maxKickerCount()).thenReturn(0);
            staticHandProcessorUtil.when(() -> HandProcessorUtil.toHandData(any(IHandProcessorData.class))).thenReturn(mockHandData);

            // Act
            final IEvaluation evaluation = HandEvaluatorUtil.evaluate(mockHandProcessor, HandUtil.fromNotation(""));

            // Assert
            assertEquals(1, constructionHandDataBuilder.constructed().size());
            final HandProcessorData mockData = constructionHandDataBuilder.constructed().get(0);
            verify(mockHandProcessor, times(13)).process(any(IHandProcessorData.class), anyInt(), anyLong(), anyInt());
            verify(mockHandProcessor, times(1)).process(mockData, ACE_INDEX, 0, 0);
            verify(mockHandProcessor, times(1)).process(mockData, KING_INDEX, 0, 0);
            verify(mockHandProcessor, times(1)).process(mockData, QUEEN_INDEX, 0, 0);
            verify(mockHandProcessor, times(1)).process(mockData, JACK_INDEX, 0, 0);
            verify(mockHandProcessor, times(1)).process(mockData, TEN_INDEX, 0, 0);
            verify(mockHandProcessor, times(1)).process(mockData, NINE_INDEX, 0, 0);
            verify(mockHandProcessor, times(1)).process(mockData, EIGHT_INDEX, 0, 0);
            verify(mockHandProcessor, times(1)).process(mockData, SEVEN_INDEX, 0, 0);
            verify(mockHandProcessor, times(1)).process(mockData, SIX_INDEX, 0, 0);
            verify(mockHandProcessor, times(1)).process(mockData, FIVE_INDEX, 0, 0);
            verify(mockHandProcessor, times(1)).process(mockData, FIVE_INDEX, 0, 0);
            verify(mockHandProcessor, times(1)).process(mockData, THREE_INDEX, 0, 0);
            verify(mockHandProcessor, times(1)).process(mockData, TWO_INDEX, 0, 0);
            staticHandProcessorUtil.verify(() -> HandProcessorUtil.toHandData(mockData), times(1));
            verify(mockHandProcessor, times(1)).toEvaluation(mockHandData, 0);
            assertEquals(1, constructionEvaluationInstances.size());
            final IEvaluation mockEvaluation = constructionEvaluationInstances.get(0);
            assertEquals(mockEvaluation, evaluation);
            assertEquals(0L, constructionEvaluationArgs.get(mockEvaluation).get(0));
            assertEquals(BitmaskUtil.NONE, constructionEvaluationArgs.get(mockEvaluation).get(1));
            assertEquals(0, ((int[])constructionEvaluationArgs.get(mockEvaluation).get(2)).length);
            assertEquals(0, constructionEvaluationArgs.get(mockEvaluation).get(3));
        }
    }

    @Test
    @Tag("IntegrationTest")
    public void compare() throws HandEvaluatorException {
        final TexasHoldemProcessor handProcessor = new TexasHoldemProcessor();

        // Nothing
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "", ""));

        // High card
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "Ac", "Ac"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AcTd", "AcTd"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AcTd5h", "AcTd5h"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "Ac", "5c"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "5c", "Ac"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "AcTd", "5c3d"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "5c3d", "AcTd"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "AcTd5h", "Ac9d5h"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "Ac9d5h", "AcTd5h"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "AcTd5h", "AcTd4h"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "AcTd4h", "AcTd5h"));

        // Pair
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "JcJd", "JcJd"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AcJcJd", "AcJcJd"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "JcJd8h", "JcJd8h"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "AcQdTh8s6c", "AcQdTh8s4c"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "AcQdTh8s4c", "AcQdTh8s6c"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "AcQdQh8s6c", "AcQdThTs6c"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "AcQdThTs6c", "AcQdQh8s6c"));

        // Two pair
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "JcJd8h8s", "JcJd8h8s"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AcJcJd8h8s", "AcJcJd8h8s"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "JcJd8h8s4c", "JcJd8h8s4c"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "JcJdTs8h8s", "JcJdTs8h8s"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "JcJd8h8s", "TcTd8h8s"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "TcTd8h8s", "JcJd8h8s"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "JcJd8h8s", "JcJd7h7s"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "JcJd7h7s", "JcJd8h8s"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "AcJcJd8h8s", "5cJcJd8h8s"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "5cJcJd8h8s", "AcJcJd8h8s"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "JcJd8h8s4c", "JcJd8h8s3c"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "JcJd8h8s3c", "JcJd8h8s4c"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "JcJdTs8h8s", "JcJd9s8h8s"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "JcJd9s8h8s", "JcJdTs8h8s"));

        // Trips
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "JcJdJh", "JcJdJh"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AcJcJdJh", "AcJcJdJh"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AcKsJcJdJh", "AcKsJcJdJh"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AcJcJdJh8c", "AcJcJdJh8c"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "JcJdJh9c8c", "JcJdJh9c8c"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "JcJdJh", "TcTdTh"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "TcTdTh", "JcJdJh"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "AcJcJdJh", "AcTcTdTh"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "AcTcTdTh", "AcJcJdJh"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "JcJdJh8c", "TcTdTh8c"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "TcTdTh8c", "JcJdJh8c"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "AcJcJdJh8c", "AcTcTdTh8c"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "AcTcTdTh8c", "AcJcJdJh8c"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "AcKsJcJdJh", "AcKsTcTdTh"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "AcKsTcTdTh", "AcKsJcJdJh"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "AcJcJdJh", "KcJcJdJh"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "KcJcJdJh", "AcJcJdJh"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "JcJdJh8c", "JcJdJh7c"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "JcJdJh7c", "JcJdJh8c"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "AcKcJcJdJh", "AcQcJcJdJh"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "AcQcJcJdJh", "AcKcJcJdJh"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "AcJcJdJhTc", "AcJcJdJh9c"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "AcJcJdJh9c", "AcJcJdJhTc"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "JcJdJhTc9c", "JcJdJhTc8c"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "JcJdJhTc8c", "JcJdJhTc9c"));

        // Straight
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AcKdQsJhTc", "AcKdQsJhTc"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "8c7d6s5h4c", "8c7d6s5h4c"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "5c4d3s2hAc", "5c4d3s2hAc"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "AcKdQsJhTc", "8c7d6s5h4c"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "8c7d6s5h4c", "AcKdQsJhTc"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "AcKdQsJhTc", "5c4d3s2hAc"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "5c4d3s2hAc", "AcKdQsJhTc"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "8c7d6s5h4c", "5c4d3s2hAc"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "5c4d3s2hAc", "8c7d6s5h4c"));

        // Flush
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AcQcTc8c6c", "AcQcTc8c6c"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AdQdTd8d6d", "AdQdTd8d6d"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AhQhTh8h6h", "AhQhTh8h6h"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AsQsTs8s6s", "AsQsTs8s6s"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AcQcTc8c6c", "AdQdTd8d6d"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AcQcTc8c6c", "AhQhTh8h6h"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AcQcTc8c6c", "AsQsTs8s6s"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AdQdTd8d6d", "AcQcTc8c6c"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AdQdTd8d6d", "AhQhTh8h6h"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AdQdTd8d6d", "AsQsTs8s6s"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AhQhTh8h6h", "AcQcTc8c6c"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AhQhTh8h6h", "AdQdTd8d6d"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AhQhTh8h6h", "AsQsTs8s6s"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AsQsTs8s6s", "AcQcTc8c6c"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AsQsTs8s6s", "AdQdTd8d6d"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AsQsTs8s6s", "AhQhTh8h6h"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AcQcTc8c6c", "AcQcTc8c5c"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AcQcTc8c5c", "AcQcTc8c6c"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "AcQcTc8c6c", "KcQcTc8c6c"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "KcQcTc8c6c", "AcQcTc8c6c"));

        // Full house
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "TcTcTc8c8c", "TcTcTc8c8c"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "JcJcJc8c8c", "TcTcTc8c8c"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "TcTcTc8c8c", "JcJcJc8c8c"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "TcTcTc8c8c", "TcTcTc7c7c"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "TcTcTc7c7c", "TcTcTc8c8c"));

        // Quads
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "TcTcTcTc", "TcTcTcTc"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AcTcTcTcTc", "AcTcTcTcTc"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "TcTcTcTc8c", "TcTcTcTc8c"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "TcTcTcTc", "8c8c8c8c"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "8c8c8c8c", "TcTcTcTc"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "AcTcTcTcTc", "KcTcTcTcTc"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "KcTcTcTcTc", "AcTcTcTcTc"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "TcTcTcTc8c", "TcTcTcTc7c"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "TcTcTcTc7c", "TcTcTcTc8c"));

        // Straight flush
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "AcKcQcJcTc", "AcKcQcJcTc"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "8c7c6c5c4c", "8c7c6c5c4c"));
        assertEquals(0, HandEvaluatorUtil.compare(handProcessor, "5c4c3c2cAc", "5c4c3c2cAc"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "AcKcQcJcTc", "8c7c6c5c4c"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "8c7c6c5c4c", "AcKcQcJcTc"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "AcKcQcJcTc", "5c4c3c2cAc"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "5c4c3c2cAc", "AcKcQcJcTc"));
        assertEquals(1, HandEvaluatorUtil.compare(handProcessor, "8c7c6c5c4c", "5c4c3c2cAc"));
        assertEquals(-1, HandEvaluatorUtil.compare(handProcessor, "5c4c3c2cAc", "8c7c6c5c4c"));
    }

    @Test
    @Tag("IntegrationTest")
    public void compare_whenIncompatibleHandsAreCompared_throwHandEvaluatorException() {
        final TexasHoldemProcessor handProcessor = new TexasHoldemProcessor();

        assertThrows(HandEvaluatorException.class, () -> HandEvaluatorUtil.compare(handProcessor, "Ac", ""));
        assertThrows(HandEvaluatorException.class, () -> HandEvaluatorUtil.compare(handProcessor, "AcKd", "Ac"));
        assertThrows(HandEvaluatorException.class, () -> HandEvaluatorUtil.compare(handProcessor, "AcAd", ""));
        assertThrows(HandEvaluatorException.class, () -> HandEvaluatorUtil.compare(handProcessor, "AcAd", "AcAdKh"));
        assertThrows(HandEvaluatorException.class, () -> HandEvaluatorUtil.compare(handProcessor, "AcAdAh", ""));
        assertThrows(HandEvaluatorException.class, () -> HandEvaluatorUtil.compare(handProcessor, "AcKdQhJsTc", ""));
        assertThrows(HandEvaluatorException.class, () -> HandEvaluatorUtil.compare(handProcessor, "AcKdQhJsTc", "JcJdJh"));
        assertThrows(HandEvaluatorException.class, () -> HandEvaluatorUtil.compare(handProcessor, "AcQcTc8c6c", ""));
        assertThrows(HandEvaluatorException.class, () -> HandEvaluatorUtil.compare(handProcessor, "AcQcTc8c6c", "AcAdKhKs"));
        assertThrows(HandEvaluatorException.class, () -> HandEvaluatorUtil.compare(handProcessor, "AcAdAhTsTc", ""));
        assertThrows(HandEvaluatorException.class, () -> HandEvaluatorUtil.compare(handProcessor, "AcAdAhTsTc", "Tc9d"));
        assertThrows(HandEvaluatorException.class, () -> HandEvaluatorUtil.compare(handProcessor, "AcAdAhAs", ""));
        assertThrows(HandEvaluatorException.class, () -> HandEvaluatorUtil.compare(handProcessor, "AcAdAhAs", "AcAdAhAsKc"));
        assertThrows(HandEvaluatorException.class, () -> HandEvaluatorUtil.compare(handProcessor, "AcKcQcJcTc", ""));
    }

    @Test
    @Tag("UnitTest")
    public void compare_whenHand1ThrowsNotationException_rethrowHandEvaluatorException() {
        try (MockedStatic<HandUtil> staticHandUtil = mockStatic(HandUtil.class)) {
            staticHandUtil.when(() -> HandUtil.fromNotation("hand1")).thenThrow(new NotationException());

            // Arrange
            final IHandProcessor handProcessor = mock(IHandProcessor.class);

            // Act
            assertThrows(HandEvaluatorException.class, () -> HandEvaluatorUtil.compare(handProcessor, "hand1", "hand2"));
        }
    }

    @Test
    @Tag("UnitTest")
    public void compare_whenHand2ThrowsNotationException_rethrowHandEvaluatorException() {
        try (MockedStatic<HandUtil> staticHandUtil = mockStatic(HandUtil.class)) {
            staticHandUtil.when(() -> HandUtil.fromNotation("hand2")).thenThrow(new NotationException());

            // Arrange
            final IHandProcessor handProcessor = mock(IHandProcessor.class);

            // Act
            assertThrows(HandEvaluatorException.class, () -> HandEvaluatorUtil.compare(handProcessor, "hand1", "hand2"));
        }
    }

    @Disabled
    @Test
    public void test_evaluateAllHands() {
        long start = 0L;
        long max = 10000000000L;
        long iterations = 0L;
        for (long hand = start; hand < 0b1111111111111111111111111111111111111111111111111111L; hand++, iterations++) {
            if (hand >= max) break;
        }
    }

    @Disabled
    @Test
    public void test_evaluateHands() {
        final TexasHoldemProcessor handProcessor = new TexasHoldemProcessor();

        // Warm up
        System.out.println("Warming up...");
        for (long hand = 0; hand <= 10000000000L; hand++) {
            if (Long.bitCount(hand) > 7) continue;
            HandEvaluatorUtil.evaluate(handProcessor, hand);
        }

        /**
         Hands evaluated: 1m (11.6m hands/sec), delta time: 86ms
         Hands evaluated: 10m (33.6m hands/sec), delta time: 298ms
         Hands evaluated: 100m (128.2m hands/sec), delta time: 780ms
         Hands evaluated: 1b (505.6m hands/sec), delta time: 1978ms
         Hands evaluated: 10b (1556.9m hands/sec), delta time: 6423ms
         Hands evaluated: 100b (3134.9m hands/sec), delta time: 31899ms
         Hands evaluated: 1t (3836.4m hands/sec), delta time: 260660ms
         */

        for (long i = 0; i <= 9; i++) {
            {
                long iterations = 0;
                long maxIterations = (long) (1000L * Math.pow(10, i));
                long startTime = System.nanoTime();
                for (long hand = 0L; iterations < maxIterations; hand++, iterations++) {
                    if (Long.bitCount(hand) > 7) continue;
                    HandEvaluatorUtil.evaluate(handProcessor, hand);
                }
                long endTime = System.nanoTime();
                String iterationString = getString(iterations);
                long deltaTimeMs = (endTime - startTime) / 1000L / 1000L;

                float handsPerSec = ((float)iterations / deltaTimeMs) / 1000L;
                System.out.printf("Hands evaluated: %s (%.1fm hands/sec), delta time: %sms%n", iterationString, handsPerSec, deltaTimeMs);
            }
        }
    }

    private static String getString(long iterations) {
        String iterationString = "";
        if (iterations >= 1000000000000L) {
            iterationString += (iterations / 1000000000000L) + "t";
        } else if (iterations >= 1000000000L) {
            iterationString += (iterations / 1000000000L) + "b";
        } else if (iterations >= 1000000L) {
            iterationString += (iterations / 1000000L) + "m";
        } else if (iterations >= 1000L) {
            iterationString += (iterations / 1000L) + "k";
        } else {
            iterationString += iterations;
        }
        return iterationString;
    }

    @Disabled
    @Test
    public void test_specificHand() throws Exception {
        final TexasHoldemProcessor handProcessor = new TexasHoldemProcessor();

        long hand = 0b0000000000000000000000000000000000000010000000000011L;
        IEvaluation evaluation = HandEvaluatorUtil.evaluate(handProcessor, hand);
        String kickers = Arrays.stream(evaluation.getKickers()).boxed().filter(kicker -> kicker != -1).map(kicker -> "" + kicker).collect(Collectors.joining(","));
        // String kickers = evaluation.kickers() .boxed().map(kicker -> "" + kicker).collect(Collectors.joining(","));
        System.out.println("Hand: " + HandUtil.toText(hand) + ", type: " + evaluation.getHandType() +  ", kickers: " + kickers);
    }

    @Disabled
    @Test
    public void testCombos() {
        final TexasHoldemProcessor handProcessor = new TexasHoldemProcessor();

        int count = 52;
        int iteration = 0;
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                for (int k = j + 1; k < count; k++) {
                    for (int l = k + 1; l < count; l++) {
                        for (int m = l + 1; m < count; m++) {
                            for (int n = m + 1; n < count; n++) {
                                for (int o = n + 1; o < count; o++) {
                                    iteration++;
                                    long hand = 0L;
                                    hand |= (RANKS[i % 13] << (FAMILY_SIZE * (i / 4)));
                                    hand |= (RANKS[j % 13] << (FAMILY_SIZE * (j / 4)));
                                    hand |= (RANKS[k % 13] << (FAMILY_SIZE * (k / 4)));
                                    hand |= (RANKS[l % 13] << (FAMILY_SIZE * (l / 4)));
                                    hand |= (RANKS[m % 13] << (FAMILY_SIZE * (m / 4)));
                                    hand |= (RANKS[n % 13] << (FAMILY_SIZE * (n / 4)));
                                    hand |= (RANKS[o % 13] << (FAMILY_SIZE * (o / 4)));

                                    IEvaluation evaluation = HandEvaluatorUtil.evaluate(handProcessor, hand);
                                    // System.out.println(hand + " is " + evaluation);
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Combinations: " + iteration);
    }
}
