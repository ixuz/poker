package com.antwika.test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockIterator {
    public static <T> void mockIterable(Iterable<T> iterable, List<T> values) {
        @SuppressWarnings("unchecked") final Iterator<T> mockIterator = (Iterator<T>) mock(Iterator.class);
        when(iterable.iterator()).thenReturn(mockIterator);

        if (values.isEmpty()) {
            when(mockIterator.hasNext()).thenReturn(false);
        } else if (values.size() == 1) {
            when(mockIterator.hasNext()).thenReturn(true, false);
            when(mockIterator.next()).thenReturn(values.get(0));
        } else {
            // build boolean array for hasNext()
            final Boolean[] hasNextResponses = new Boolean[values.size()];
            for (int i = 0; i < hasNextResponses.length -1 ; i++) {
                hasNextResponses[i] = true;
            }
            hasNextResponses[hasNextResponses.length - 1] = false;
            when(mockIterator.hasNext()).thenReturn(true, hasNextResponses);
            @SuppressWarnings("unchecked") final var arr = (T[]) values.toArray();
            final T[] valuesMinusTheFirst = Arrays.copyOfRange(arr, 1, arr.length);
            when(mockIterator.next()).thenReturn(values.get(0), valuesMinusTheFirst);
        }
    }
}
