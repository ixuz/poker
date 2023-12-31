package com.antwika.common.util;

import java.util.HashMap;
import java.util.Map;

public class BitmaskUtil {
    public static final long NONE = 0b0L;
    public static final long HIGH_CARD = 0b1L;
    public static final long PAIR = 0b1L << 1;
    public static final long TWO_PAIR = 0b1L << 2;
    public static final long TRIPS = 0b1L << 3;
    public static final long STRAIGHT = 0b1L << 4;
    public static final long FLUSH = 0b1L << 5;
    public static final long FULL_HOUSE = 0b1L << 6;
    public static final long QUADS = 0b1L << 7;
    public static final long STRAIGHT_FLUSH = 0b1L << 8;
    public static final long TWO = 0b0000000000001L;
    public static final long THREE = TWO << 1L;
    public static final long FOUR = THREE << 1L;
    public static final long FIVE = FOUR << 1L;
    public static final long SIX = FIVE << 1L;
    public static final long SEVEN = SIX << 1L;
    public static final long EIGHT = SEVEN << 1L;
    public static final long NINE = EIGHT << 1L;
    public static final long TEN = NINE << 1L;
    public static final long JACK = TEN << 1L;
    public static final long QUEEN = JACK << 1L;
    public static final long KING = QUEEN << 1L;
    public static final long ACE = KING << 1L;
    public static final int TWO_INDEX = 0;
    public static final int THREE_INDEX = 1;
    public static final int FOUR_INDEX = 2;
    public static final int FIVE_INDEX = 3;
    public static final int SIX_INDEX = 4;
    public static final int SEVEN_INDEX = 5;
    public static final int EIGHT_INDEX = 6;
    public static final int NINE_INDEX = 7;
    public static final int TEN_INDEX = 8;
    public static final int JACK_INDEX = 9;
    public static final int QUEEN_INDEX = 10;
    public static final int KING_INDEX = 11;
    public static final int ACE_INDEX = 12;
    public static final long[] RANKS = new long[]{ TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE };
    public static final int[] RANK_TO_INDEX = new int[(int)ACE+1];
    static {
        RANK_TO_INDEX[(int)TWO] = 0;
        RANK_TO_INDEX[(int)THREE] = 1;
        RANK_TO_INDEX[(int)FOUR] = 2;
        RANK_TO_INDEX[(int)FIVE] = 3;
        RANK_TO_INDEX[(int)SIX] = 4;
        RANK_TO_INDEX[(int)SEVEN] = 5;
        RANK_TO_INDEX[(int)EIGHT] = 6;
        RANK_TO_INDEX[(int)NINE] = 7;
        RANK_TO_INDEX[(int)TEN] = 8;
        RANK_TO_INDEX[(int)JACK] = 9;
        RANK_TO_INDEX[(int)QUEEN] = 10;
        RANK_TO_INDEX[(int)KING] = 11;
        RANK_TO_INDEX[(int)ACE] = 12;
    }

    public static final long FAMILY_SIZE = 13L;
    public static final int CLUBS_INDEX = 0;
    public static final int DIAMONDS_INDEX = 1;
    public static final int HEARTS_INDEX = 2;
    public static final int SPADES_INDEX = 3;
    public static final long CLUBS_OFFSET = 0L;
    public static final long DIAMONDS_OFFSET = FAMILY_SIZE;
    public static final long HEARTS_OFFSET = FAMILY_SIZE * 2L;
    public static final long SPADES_OFFSET = FAMILY_SIZE * 3L;
    public static final long[] FAMILY_OFFSETS = new long[]{ CLUBS_OFFSET, DIAMONDS_OFFSET, HEARTS_OFFSET, SPADES_OFFSET };
    public static final long FAMILY = TWO | THREE | FOUR | FIVE | SIX | SEVEN | EIGHT | NINE | TEN | JACK | QUEEN | KING | ACE;
    public static final long CLUBS = FAMILY;
    public static final long DIAMONDS = CLUBS << FAMILY_SIZE;
    public static final long HEARTS = DIAMONDS << FAMILY_SIZE;
    public static final long SPADES = HEARTS << FAMILY_SIZE;
    public static final long[] FAMILIES = new long[]{ CLUBS, DIAMONDS, HEARTS, SPADES };
    public static final long DECK = CLUBS | DIAMONDS | HEARTS | SPADES;
    public static final long TWOS = (TWO << CLUBS_OFFSET) | (TWO << DIAMONDS_OFFSET) | (TWO << HEARTS_OFFSET) | (TWO << SPADES_OFFSET);
    public static final long THREES = TWOS << 1L;
    public static final long FOURS = THREES << 1L;
    public static final long FIVES = FOURS << 1L;
    public static final long SIXES = FIVES << 1L;
    public static final long SEVENS = SIXES << 1L;
    public static final long EIGHTS = SEVENS << 1L;
    public static final long NINES = EIGHTS << 1L;
    public static final long TENS = NINES << 1L;
    public static final long JACKS = TENS << 1L;
    public static final long QUEENS = JACKS << 1L;
    public static final long KINGS = QUEENS << 1L;
    public static final long ACES = KINGS << 1L;
    public static final long[] RANK_TO_ANY_OF_RANK = new long[13];
    static {
        RANK_TO_ANY_OF_RANK[TWO_INDEX] = TWOS;
        RANK_TO_ANY_OF_RANK[THREE_INDEX] = THREES;
        RANK_TO_ANY_OF_RANK[FOUR_INDEX] = FOURS;
        RANK_TO_ANY_OF_RANK[FIVE_INDEX] = FIVES;
        RANK_TO_ANY_OF_RANK[SIX_INDEX] = SIXES;
        RANK_TO_ANY_OF_RANK[SEVEN_INDEX] = SEVENS;
        RANK_TO_ANY_OF_RANK[EIGHT_INDEX] = EIGHTS;
        RANK_TO_ANY_OF_RANK[NINE_INDEX] = NINES;
        RANK_TO_ANY_OF_RANK[TEN_INDEX] = TENS;
        RANK_TO_ANY_OF_RANK[JACK_INDEX] = JACKS;
        RANK_TO_ANY_OF_RANK[QUEEN_INDEX] = QUEENS;
        RANK_TO_ANY_OF_RANK[KING_INDEX] = KINGS;
        RANK_TO_ANY_OF_RANK[ACE_INDEX] = ACES;
    }
    public static final long ACE_CLUBS = (ACE << CLUBS_OFFSET);
    public static final long KING_CLUBS = (KING << CLUBS_OFFSET);
    public static final long QUEEN_CLUBS = (QUEEN << CLUBS_OFFSET);
    public static final long JACK_CLUBS = (JACK << CLUBS_OFFSET);
    public static final long TEN_CLUBS = (TEN << CLUBS_OFFSET);
    public static final long NINE_CLUBS = (NINE << CLUBS_OFFSET);
    public static final long EIGHT_CLUBS = (EIGHT << CLUBS_OFFSET);
    public static final long SEVEN_CLUBS = (SEVEN << CLUBS_OFFSET);
    public static final long SIX_CLUBS = (SIX << CLUBS_OFFSET);
    public static final long FIVE_CLUBS = (FIVE << CLUBS_OFFSET);
    public static final long FOUR_CLUBS = (FOUR << CLUBS_OFFSET);
    public static final long THREE_CLUBS = (THREE << CLUBS_OFFSET);
    public static final long TWO_CLUBS = (TWO << CLUBS_OFFSET);
    public static final long ACE_DIAMONDS = (ACE << DIAMONDS_OFFSET);
    public static final long KING_DIAMONDS = (KING << DIAMONDS_OFFSET);
    public static final long QUEEN_DIAMONDS = (QUEEN << DIAMONDS_OFFSET);
    public static final long JACK_DIAMONDS = (JACK << DIAMONDS_OFFSET);
    public static final long TEN_DIAMONDS = (TEN << DIAMONDS_OFFSET);
    public static final long NINE_DIAMONDS = (NINE << DIAMONDS_OFFSET);
    public static final long EIGHT_DIAMONDS = (EIGHT << DIAMONDS_OFFSET);
    public static final long SEVEN_DIAMONDS = (SEVEN << DIAMONDS_OFFSET);
    public static final long SIX_DIAMONDS = (SIX << DIAMONDS_OFFSET);
    public static final long FIVE_DIAMONDS = (FIVE << DIAMONDS_OFFSET);
    public static final long FOUR_DIAMONDS = (FOUR << DIAMONDS_OFFSET);
    public static final long THREE_DIAMONDS = (THREE << DIAMONDS_OFFSET);
    public static final long TWO_DIAMONDS = (TWO << DIAMONDS_OFFSET);
    public static final long ACE_HEARTS = (ACE << HEARTS_OFFSET);
    public static final long KING_HEARTS = (KING << HEARTS_OFFSET);
    public static final long QUEEN_HEARTS = (QUEEN << HEARTS_OFFSET);
    public static final long JACK_HEARTS = (JACK << HEARTS_OFFSET);
    public static final long TEN_HEARTS = (TEN << HEARTS_OFFSET);
    public static final long NINE_HEARTS = (NINE << HEARTS_OFFSET);
    public static final long EIGHT_HEARTS = (EIGHT << HEARTS_OFFSET);
    public static final long SEVEN_HEARTS = (SEVEN << HEARTS_OFFSET);
    public static final long SIX_HEARTS = (SIX << HEARTS_OFFSET);
    public static final long FIVE_HEARTS = (FIVE << HEARTS_OFFSET);
    public static final long FOUR_HEARTS = (FOUR << HEARTS_OFFSET);
    public static final long THREE_HEARTS = (THREE << HEARTS_OFFSET);
    public static final long TWO_HEARTS = (TWO << HEARTS_OFFSET);
    public static final long ACE_SPADES = (ACE << SPADES_OFFSET);
    public static final long KING_SPADES = (KING << SPADES_OFFSET);
    public static final long QUEEN_SPADES = (QUEEN << SPADES_OFFSET);
    public static final long JACK_SPADES = (JACK << SPADES_OFFSET);
    public static final long TEN_SPADES = (TEN << SPADES_OFFSET);
    public static final long NINE_SPADES = (NINE << SPADES_OFFSET);
    public static final long EIGHT_SPADES = (EIGHT << SPADES_OFFSET);
    public static final long SEVEN_SPADES = (SEVEN << SPADES_OFFSET);
    public static final long SIX_SPADES = (SIX << SPADES_OFFSET);
    public static final long FIVE_SPADES = (FIVE << SPADES_OFFSET);
    public static final long FOUR_SPADES = (FOUR << SPADES_OFFSET);
    public static final long THREE_SPADES = (THREE << SPADES_OFFSET);
    public static final long TWO_SPADES = (TWO << SPADES_OFFSET);
    public static final long ACE_HIGH_STRAIGHT_FLUSH_CLUBS = ACE_CLUBS | KING_CLUBS | QUEEN_CLUBS | JACK_CLUBS | TEN_CLUBS;
    public static final long KING_HIGH_STRAIGHT_FLUSH_CLUBS = KING_CLUBS | QUEEN_CLUBS | JACK_CLUBS | TEN_CLUBS | NINE_CLUBS;
    public static final long QUEEN_HIGH_STRAIGHT_FLUSH_CLUBS = QUEEN_CLUBS | JACK_CLUBS | TEN_CLUBS | NINE_CLUBS | EIGHT_CLUBS;
    public static final long JACK_HIGH_STRAIGHT_FLUSH_CLUBS = JACK_CLUBS | TEN_CLUBS | NINE_CLUBS | EIGHT_CLUBS | SEVEN_CLUBS;
    public static final long TEN_HIGH_STRAIGHT_FLUSH_CLUBS = TEN_CLUBS | NINE_CLUBS | EIGHT_CLUBS | SEVEN_CLUBS | SIX_CLUBS;
    public static final long NINE_HIGH_STRAIGHT_FLUSH_CLUBS = NINE_CLUBS | EIGHT_CLUBS | SEVEN_CLUBS | SIX_CLUBS | FIVE_CLUBS;
    public static final long EIGHT_HIGH_STRAIGHT_FLUSH_CLUBS = EIGHT_CLUBS | SEVEN_CLUBS | SIX_CLUBS | FIVE_CLUBS | FOUR_CLUBS;
    public static final long SEVEN_HIGH_STRAIGHT_FLUSH_CLUBS = SEVEN_CLUBS | SIX_CLUBS | FIVE_CLUBS | FOUR_CLUBS | THREE_CLUBS;
    public static final long SIX_HIGH_STRAIGHT_FLUSH_CLUBS = SIX_CLUBS | FIVE_CLUBS | FOUR_CLUBS | THREE_CLUBS | TWO_CLUBS;
    public static final long FIVE_HIGH_STRAIGHT_FLUSH_CLUBS = FIVE_CLUBS | FOUR_CLUBS | THREE_CLUBS | TWO_CLUBS | ACE_CLUBS;
    public static final long ACE_HIGH_STRAIGHT_FLUSH_DIAMONDS = ACE_DIAMONDS | KING_DIAMONDS | QUEEN_DIAMONDS | JACK_DIAMONDS | TEN_DIAMONDS;
    public static final long KING_HIGH_STRAIGHT_FLUSH_DIAMONDS = KING_DIAMONDS | QUEEN_DIAMONDS | JACK_DIAMONDS | TEN_DIAMONDS | NINE_DIAMONDS;
    public static final long QUEEN_HIGH_STRAIGHT_FLUSH_DIAMONDS = QUEEN_DIAMONDS | JACK_DIAMONDS | TEN_DIAMONDS | NINE_DIAMONDS | EIGHT_DIAMONDS;
    public static final long JACK_HIGH_STRAIGHT_FLUSH_DIAMONDS = JACK_DIAMONDS | TEN_DIAMONDS | NINE_DIAMONDS | EIGHT_DIAMONDS | SEVEN_DIAMONDS;
    public static final long TEN_HIGH_STRAIGHT_FLUSH_DIAMONDS = TEN_DIAMONDS | NINE_DIAMONDS | EIGHT_DIAMONDS | SEVEN_DIAMONDS | SIX_DIAMONDS;
    public static final long NINE_HIGH_STRAIGHT_FLUSH_DIAMONDS = NINE_DIAMONDS | EIGHT_DIAMONDS | SEVEN_DIAMONDS | SIX_DIAMONDS | FIVE_DIAMONDS;
    public static final long EIGHT_HIGH_STRAIGHT_FLUSH_DIAMONDS = EIGHT_DIAMONDS | SEVEN_DIAMONDS | SIX_DIAMONDS | FIVE_DIAMONDS | FOUR_DIAMONDS;
    public static final long SEVEN_HIGH_STRAIGHT_FLUSH_DIAMONDS = SEVEN_DIAMONDS | SIX_DIAMONDS | FIVE_DIAMONDS | FOUR_DIAMONDS | THREE_DIAMONDS;
    public static final long SIX_HIGH_STRAIGHT_FLUSH_DIAMONDS = SIX_DIAMONDS | FIVE_DIAMONDS | FOUR_DIAMONDS | THREE_DIAMONDS | TWO_DIAMONDS;
    public static final long FIVE_HIGH_STRAIGHT_FLUSH_DIAMONDS = FIVE_DIAMONDS | FOUR_DIAMONDS | THREE_DIAMONDS | TWO_DIAMONDS | ACE_DIAMONDS;
    public static final long ACE_HIGH_STRAIGHT_FLUSH_HEARTS = ACE_HEARTS | KING_HEARTS | QUEEN_HEARTS | JACK_HEARTS | TEN_HEARTS;
    public static final long KING_HIGH_STRAIGHT_FLUSH_HEARTS = KING_HEARTS | QUEEN_HEARTS | JACK_HEARTS | TEN_HEARTS | NINE_HEARTS;
    public static final long QUEEN_HIGH_STRAIGHT_FLUSH_HEARTS = QUEEN_HEARTS | JACK_HEARTS | TEN_HEARTS | NINE_HEARTS | EIGHT_HEARTS;
    public static final long JACK_HIGH_STRAIGHT_FLUSH_HEARTS = JACK_HEARTS | TEN_HEARTS | NINE_HEARTS | EIGHT_HEARTS | SEVEN_HEARTS;
    public static final long TEN_HIGH_STRAIGHT_FLUSH_HEARTS = TEN_HEARTS | NINE_HEARTS | EIGHT_HEARTS | SEVEN_HEARTS | SIX_HEARTS;
    public static final long NINE_HIGH_STRAIGHT_FLUSH_HEARTS = NINE_HEARTS | EIGHT_HEARTS | SEVEN_HEARTS | SIX_HEARTS | FIVE_HEARTS;
    public static final long EIGHT_HIGH_STRAIGHT_FLUSH_HEARTS = EIGHT_HEARTS | SEVEN_HEARTS | SIX_HEARTS | FIVE_HEARTS | FOUR_HEARTS;
    public static final long SEVEN_HIGH_STRAIGHT_FLUSH_HEARTS = SEVEN_HEARTS | SIX_HEARTS | FIVE_HEARTS | FOUR_HEARTS | THREE_HEARTS;
    public static final long SIX_HIGH_STRAIGHT_FLUSH_HEARTS = SIX_HEARTS | FIVE_HEARTS | FOUR_HEARTS | THREE_HEARTS | TWO_HEARTS;
    public static final long FIVE_HIGH_STRAIGHT_FLUSH_HEARTS = FIVE_HEARTS | FOUR_HEARTS | THREE_HEARTS | TWO_HEARTS | ACE_HEARTS;
    public static final long ACE_HIGH_STRAIGHT_FLUSH_SPADES = ACE_SPADES | KING_SPADES | QUEEN_SPADES | JACK_SPADES | TEN_SPADES;
    public static final long KING_HIGH_STRAIGHT_FLUSH_SPADES = KING_SPADES | QUEEN_SPADES | JACK_SPADES | TEN_SPADES | NINE_SPADES;
    public static final long QUEEN_HIGH_STRAIGHT_FLUSH_SPADES = QUEEN_SPADES | JACK_SPADES | TEN_SPADES | NINE_SPADES | EIGHT_SPADES;
    public static final long JACK_HIGH_STRAIGHT_FLUSH_SPADES = JACK_SPADES | TEN_SPADES | NINE_SPADES | EIGHT_SPADES | SEVEN_SPADES;
    public static final long TEN_HIGH_STRAIGHT_FLUSH_SPADES = TEN_SPADES | NINE_SPADES | EIGHT_SPADES | SEVEN_SPADES | SIX_SPADES;
    public static final long NINE_HIGH_STRAIGHT_FLUSH_SPADES = NINE_SPADES | EIGHT_SPADES | SEVEN_SPADES | SIX_SPADES | FIVE_SPADES;
    public static final long EIGHT_HIGH_STRAIGHT_FLUSH_SPADES = EIGHT_SPADES | SEVEN_SPADES | SIX_SPADES | FIVE_SPADES | FOUR_SPADES;
    public static final long SEVEN_HIGH_STRAIGHT_FLUSH_SPADES = SEVEN_SPADES | SIX_SPADES | FIVE_SPADES | FOUR_SPADES | THREE_SPADES;
    public static final long SIX_HIGH_STRAIGHT_FLUSH_SPADES = SIX_SPADES | FIVE_SPADES | FOUR_SPADES | THREE_SPADES | TWO_SPADES;
    public static final long FIVE_HIGH_STRAIGHT_FLUSH_SPADES = FIVE_SPADES | FOUR_SPADES | THREE_SPADES | TWO_SPADES | ACE_SPADES;

    public static final long[] straightFlushBitmaskByRankAndFamily = new long[52];
    static {
        straightFlushBitmaskByRankAndFamily[FIVE_INDEX] = FIVE_HIGH_STRAIGHT_FLUSH_CLUBS;
        straightFlushBitmaskByRankAndFamily[SIX_INDEX] = SIX_HIGH_STRAIGHT_FLUSH_CLUBS;
        straightFlushBitmaskByRankAndFamily[SEVEN_INDEX] = SEVEN_HIGH_STRAIGHT_FLUSH_CLUBS;
        straightFlushBitmaskByRankAndFamily[EIGHT_INDEX] = EIGHT_HIGH_STRAIGHT_FLUSH_CLUBS;
        straightFlushBitmaskByRankAndFamily[NINE_INDEX] = NINE_HIGH_STRAIGHT_FLUSH_CLUBS;
        straightFlushBitmaskByRankAndFamily[TEN_INDEX] = TEN_HIGH_STRAIGHT_FLUSH_CLUBS;
        straightFlushBitmaskByRankAndFamily[JACK_INDEX] = JACK_HIGH_STRAIGHT_FLUSH_CLUBS;
        straightFlushBitmaskByRankAndFamily[QUEEN_INDEX] = QUEEN_HIGH_STRAIGHT_FLUSH_CLUBS;
        straightFlushBitmaskByRankAndFamily[KING_INDEX] = KING_HIGH_STRAIGHT_FLUSH_CLUBS;
        straightFlushBitmaskByRankAndFamily[ACE_INDEX] = ACE_HIGH_STRAIGHT_FLUSH_CLUBS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * DIAMONDS_INDEX + FIVE_INDEX] = FIVE_HIGH_STRAIGHT_FLUSH_DIAMONDS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * DIAMONDS_INDEX + SIX_INDEX] = SIX_HIGH_STRAIGHT_FLUSH_DIAMONDS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * DIAMONDS_INDEX + SEVEN_INDEX] = SEVEN_HIGH_STRAIGHT_FLUSH_DIAMONDS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * DIAMONDS_INDEX + EIGHT_INDEX] = EIGHT_HIGH_STRAIGHT_FLUSH_DIAMONDS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * DIAMONDS_INDEX + NINE_INDEX] = NINE_HIGH_STRAIGHT_FLUSH_DIAMONDS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * DIAMONDS_INDEX + TEN_INDEX] = TEN_HIGH_STRAIGHT_FLUSH_DIAMONDS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * DIAMONDS_INDEX + JACK_INDEX] = JACK_HIGH_STRAIGHT_FLUSH_DIAMONDS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * DIAMONDS_INDEX + QUEEN_INDEX] = QUEEN_HIGH_STRAIGHT_FLUSH_DIAMONDS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * DIAMONDS_INDEX + KING_INDEX] = KING_HIGH_STRAIGHT_FLUSH_DIAMONDS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * DIAMONDS_INDEX + ACE_INDEX] = ACE_HIGH_STRAIGHT_FLUSH_DIAMONDS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * HEARTS_INDEX + FIVE_INDEX] = FIVE_HIGH_STRAIGHT_FLUSH_HEARTS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * HEARTS_INDEX + SIX_INDEX] = SIX_HIGH_STRAIGHT_FLUSH_HEARTS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * HEARTS_INDEX + SEVEN_INDEX] = SEVEN_HIGH_STRAIGHT_FLUSH_HEARTS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * HEARTS_INDEX + EIGHT_INDEX] = EIGHT_HIGH_STRAIGHT_FLUSH_HEARTS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * HEARTS_INDEX + NINE_INDEX] = NINE_HIGH_STRAIGHT_FLUSH_HEARTS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * HEARTS_INDEX + TEN_INDEX] = TEN_HIGH_STRAIGHT_FLUSH_HEARTS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * HEARTS_INDEX + JACK_INDEX] = JACK_HIGH_STRAIGHT_FLUSH_HEARTS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * HEARTS_INDEX + QUEEN_INDEX] = QUEEN_HIGH_STRAIGHT_FLUSH_HEARTS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * HEARTS_INDEX + KING_INDEX] = KING_HIGH_STRAIGHT_FLUSH_HEARTS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * HEARTS_INDEX + ACE_INDEX] = ACE_HIGH_STRAIGHT_FLUSH_HEARTS;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * SPADES_INDEX + FIVE_INDEX] = FIVE_HIGH_STRAIGHT_FLUSH_SPADES;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * SPADES_INDEX + SIX_INDEX] = SIX_HIGH_STRAIGHT_FLUSH_SPADES;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * SPADES_INDEX + SEVEN_INDEX] = SEVEN_HIGH_STRAIGHT_FLUSH_SPADES;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * SPADES_INDEX + EIGHT_INDEX] = EIGHT_HIGH_STRAIGHT_FLUSH_SPADES;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * SPADES_INDEX + NINE_INDEX] = NINE_HIGH_STRAIGHT_FLUSH_SPADES;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * SPADES_INDEX + TEN_INDEX] = TEN_HIGH_STRAIGHT_FLUSH_SPADES;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * SPADES_INDEX + JACK_INDEX] = JACK_HIGH_STRAIGHT_FLUSH_SPADES;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * SPADES_INDEX + QUEEN_INDEX] = QUEEN_HIGH_STRAIGHT_FLUSH_SPADES;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * SPADES_INDEX + KING_INDEX] = KING_HIGH_STRAIGHT_FLUSH_SPADES;
        straightFlushBitmaskByRankAndFamily[(int)FAMILY_SIZE * SPADES_INDEX + ACE_INDEX] = ACE_HIGH_STRAIGHT_FLUSH_SPADES;
    }

    public static final Map<Long, Integer> CARD_TO_RANK_INDEX = new HashMap<>();
    static {
        CARD_TO_RANK_INDEX.put(TWO_CLUBS, TWO_INDEX);
        CARD_TO_RANK_INDEX.put(TWO_DIAMONDS, TWO_INDEX);
        CARD_TO_RANK_INDEX.put(TWO_HEARTS, TWO_INDEX);
        CARD_TO_RANK_INDEX.put(TWO_SPADES, TWO_INDEX);
        CARD_TO_RANK_INDEX.put(THREE_CLUBS, THREE_INDEX);
        CARD_TO_RANK_INDEX.put(THREE_DIAMONDS, THREE_INDEX);
        CARD_TO_RANK_INDEX.put(THREE_HEARTS, THREE_INDEX);
        CARD_TO_RANK_INDEX.put(THREE_SPADES, THREE_INDEX);
        CARD_TO_RANK_INDEX.put(FOUR_CLUBS, FOUR_INDEX);
        CARD_TO_RANK_INDEX.put(FOUR_DIAMONDS, FOUR_INDEX);
        CARD_TO_RANK_INDEX.put(FOUR_HEARTS, FOUR_INDEX);
        CARD_TO_RANK_INDEX.put(FOUR_SPADES, FOUR_INDEX);
        CARD_TO_RANK_INDEX.put(FIVE_CLUBS, FIVE_INDEX);
        CARD_TO_RANK_INDEX.put(FIVE_DIAMONDS, FIVE_INDEX);
        CARD_TO_RANK_INDEX.put(FIVE_HEARTS, FIVE_INDEX);
        CARD_TO_RANK_INDEX.put(FIVE_SPADES, FIVE_INDEX);
        CARD_TO_RANK_INDEX.put(SIX_CLUBS, SIX_INDEX);
        CARD_TO_RANK_INDEX.put(SIX_DIAMONDS, SIX_INDEX);
        CARD_TO_RANK_INDEX.put(SIX_HEARTS, SIX_INDEX);
        CARD_TO_RANK_INDEX.put(SIX_SPADES, SIX_INDEX);
        CARD_TO_RANK_INDEX.put(SEVEN_CLUBS, SEVEN_INDEX);
        CARD_TO_RANK_INDEX.put(SEVEN_DIAMONDS, SEVEN_INDEX);
        CARD_TO_RANK_INDEX.put(SEVEN_HEARTS, SEVEN_INDEX);
        CARD_TO_RANK_INDEX.put(SEVEN_SPADES, SEVEN_INDEX);
        CARD_TO_RANK_INDEX.put(EIGHT_CLUBS, EIGHT_INDEX);
        CARD_TO_RANK_INDEX.put(EIGHT_DIAMONDS, EIGHT_INDEX);
        CARD_TO_RANK_INDEX.put(EIGHT_HEARTS, EIGHT_INDEX);
        CARD_TO_RANK_INDEX.put(EIGHT_SPADES, EIGHT_INDEX);
        CARD_TO_RANK_INDEX.put(NINE_CLUBS, NINE_INDEX);
        CARD_TO_RANK_INDEX.put(NINE_DIAMONDS, NINE_INDEX);
        CARD_TO_RANK_INDEX.put(NINE_HEARTS, NINE_INDEX);
        CARD_TO_RANK_INDEX.put(NINE_SPADES, NINE_INDEX);
        CARD_TO_RANK_INDEX.put(TEN_CLUBS, TEN_INDEX);
        CARD_TO_RANK_INDEX.put(TEN_DIAMONDS, TEN_INDEX);
        CARD_TO_RANK_INDEX.put(TEN_HEARTS, TEN_INDEX);
        CARD_TO_RANK_INDEX.put(TEN_SPADES, TEN_INDEX);
        CARD_TO_RANK_INDEX.put(JACK_CLUBS, JACK_INDEX);
        CARD_TO_RANK_INDEX.put(JACK_DIAMONDS, JACK_INDEX);
        CARD_TO_RANK_INDEX.put(JACK_HEARTS, JACK_INDEX);
        CARD_TO_RANK_INDEX.put(JACK_SPADES, JACK_INDEX);
        CARD_TO_RANK_INDEX.put(QUEEN_CLUBS, QUEEN_INDEX);
        CARD_TO_RANK_INDEX.put(QUEEN_DIAMONDS, QUEEN_INDEX);
        CARD_TO_RANK_INDEX.put(QUEEN_HEARTS, QUEEN_INDEX);
        CARD_TO_RANK_INDEX.put(QUEEN_SPADES, QUEEN_INDEX);
        CARD_TO_RANK_INDEX.put(KING_CLUBS, KING_INDEX);
        CARD_TO_RANK_INDEX.put(KING_DIAMONDS, KING_INDEX);
        CARD_TO_RANK_INDEX.put(KING_HEARTS, KING_INDEX);
        CARD_TO_RANK_INDEX.put(KING_SPADES, KING_INDEX);
        CARD_TO_RANK_INDEX.put(ACE_CLUBS, ACE_INDEX);
        CARD_TO_RANK_INDEX.put(ACE_DIAMONDS, ACE_INDEX);
        CARD_TO_RANK_INDEX.put(ACE_HEARTS, ACE_INDEX);
        CARD_TO_RANK_INDEX.put(ACE_SPADES, ACE_INDEX);
    }

    public static final Map<Long, Integer> CARD_TO_SUIT_INDEX = new HashMap<>();
    static {
        CARD_TO_SUIT_INDEX.put(TWO_CLUBS, CLUBS_INDEX);
        CARD_TO_SUIT_INDEX.put(TWO_DIAMONDS, DIAMONDS_INDEX);
        CARD_TO_SUIT_INDEX.put(TWO_HEARTS, HEARTS_INDEX);
        CARD_TO_SUIT_INDEX.put(TWO_SPADES, SPADES_INDEX);
        CARD_TO_SUIT_INDEX.put(THREE_CLUBS, CLUBS_INDEX);
        CARD_TO_SUIT_INDEX.put(THREE_DIAMONDS, DIAMONDS_INDEX);
        CARD_TO_SUIT_INDEX.put(THREE_HEARTS, HEARTS_INDEX);
        CARD_TO_SUIT_INDEX.put(THREE_SPADES, SPADES_INDEX);
        CARD_TO_SUIT_INDEX.put(FOUR_CLUBS, CLUBS_INDEX);
        CARD_TO_SUIT_INDEX.put(FOUR_DIAMONDS, DIAMONDS_INDEX);
        CARD_TO_SUIT_INDEX.put(FOUR_HEARTS, HEARTS_INDEX);
        CARD_TO_SUIT_INDEX.put(FOUR_SPADES, SPADES_INDEX);
        CARD_TO_SUIT_INDEX.put(FIVE_CLUBS, CLUBS_INDEX);
        CARD_TO_SUIT_INDEX.put(FIVE_DIAMONDS, DIAMONDS_INDEX);
        CARD_TO_SUIT_INDEX.put(FIVE_HEARTS, HEARTS_INDEX);
        CARD_TO_SUIT_INDEX.put(FIVE_SPADES, SPADES_INDEX);
        CARD_TO_SUIT_INDEX.put(SIX_CLUBS, CLUBS_INDEX);
        CARD_TO_SUIT_INDEX.put(SIX_DIAMONDS, DIAMONDS_INDEX);
        CARD_TO_SUIT_INDEX.put(SIX_HEARTS, HEARTS_INDEX);
        CARD_TO_SUIT_INDEX.put(SIX_SPADES, SPADES_INDEX);
        CARD_TO_SUIT_INDEX.put(SEVEN_CLUBS, CLUBS_INDEX);
        CARD_TO_SUIT_INDEX.put(SEVEN_DIAMONDS, DIAMONDS_INDEX);
        CARD_TO_SUIT_INDEX.put(SEVEN_HEARTS, HEARTS_INDEX);
        CARD_TO_SUIT_INDEX.put(SEVEN_SPADES, SPADES_INDEX);
        CARD_TO_SUIT_INDEX.put(EIGHT_CLUBS, CLUBS_INDEX);
        CARD_TO_SUIT_INDEX.put(EIGHT_DIAMONDS, DIAMONDS_INDEX);
        CARD_TO_SUIT_INDEX.put(EIGHT_HEARTS, HEARTS_INDEX);
        CARD_TO_SUIT_INDEX.put(EIGHT_SPADES, SPADES_INDEX);
        CARD_TO_SUIT_INDEX.put(NINE_CLUBS, CLUBS_INDEX);
        CARD_TO_SUIT_INDEX.put(NINE_DIAMONDS, DIAMONDS_INDEX);
        CARD_TO_SUIT_INDEX.put(NINE_HEARTS, HEARTS_INDEX);
        CARD_TO_SUIT_INDEX.put(NINE_SPADES, SPADES_INDEX);
        CARD_TO_SUIT_INDEX.put(TEN_CLUBS, CLUBS_INDEX);
        CARD_TO_SUIT_INDEX.put(TEN_DIAMONDS, DIAMONDS_INDEX);
        CARD_TO_SUIT_INDEX.put(TEN_HEARTS, HEARTS_INDEX);
        CARD_TO_SUIT_INDEX.put(TEN_SPADES, SPADES_INDEX);
        CARD_TO_SUIT_INDEX.put(JACK_CLUBS, CLUBS_INDEX);
        CARD_TO_SUIT_INDEX.put(JACK_DIAMONDS, DIAMONDS_INDEX);
        CARD_TO_SUIT_INDEX.put(JACK_HEARTS, HEARTS_INDEX);
        CARD_TO_SUIT_INDEX.put(JACK_SPADES, SPADES_INDEX);
        CARD_TO_SUIT_INDEX.put(QUEEN_CLUBS, CLUBS_INDEX);
        CARD_TO_SUIT_INDEX.put(QUEEN_DIAMONDS, DIAMONDS_INDEX);
        CARD_TO_SUIT_INDEX.put(QUEEN_HEARTS, HEARTS_INDEX);
        CARD_TO_SUIT_INDEX.put(QUEEN_SPADES, SPADES_INDEX);
        CARD_TO_SUIT_INDEX.put(KING_CLUBS, CLUBS_INDEX);
        CARD_TO_SUIT_INDEX.put(KING_DIAMONDS, DIAMONDS_INDEX);
        CARD_TO_SUIT_INDEX.put(KING_HEARTS, HEARTS_INDEX);
        CARD_TO_SUIT_INDEX.put(KING_SPADES, SPADES_INDEX);
        CARD_TO_SUIT_INDEX.put(ACE_CLUBS, CLUBS_INDEX);
        CARD_TO_SUIT_INDEX.put(ACE_DIAMONDS, DIAMONDS_INDEX);
        CARD_TO_SUIT_INDEX.put(ACE_HEARTS, HEARTS_INDEX);
        CARD_TO_SUIT_INDEX.put(ACE_SPADES, SPADES_INDEX);
    }
}
