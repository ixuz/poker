package com.antwika.test;

import java.util.Random;

public class RandomUtil {
    public static void step(Random random, int steps) {
        for (int i = 0; i < steps; i += 1) {
            random.nextInt();
        }
    }
}
