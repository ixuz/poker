package com.antwika.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Random;

public class Prng {
    @Getter
    private long seed;

    @Getter
    private long steps = 0L;

    private Random randomInstance;

    @AllArgsConstructor
    public static class ProxyRandom extends Random {
        private final Prng prng;

        @Override
        public int nextInt() {
            return prng.nextInt();
        }

        @Override
        public int nextInt(int bound) {
            return prng.nextInt(bound);
        }
    }

    public Prng(long seed) {
        set(seed, 0L);
    }

    public synchronized int nextInt() {
        steps += 1L;
        return randomInstance.nextInt();
    }

    public synchronized int nextInt(int bound) {
        steps += 1L;
        return randomInstance.nextInt(bound);
    }

    public synchronized void set(long seed, long steps) {
        this.seed = seed;
        this.randomInstance = new Random(seed);
        this.steps = steps;
        for (long i = 0L; i < steps; i += 1L) {
            this.randomInstance.nextInt();
        }
    }

    public synchronized Random getRandomInstance() {
        return new ProxyRandom(this);
    }
}
