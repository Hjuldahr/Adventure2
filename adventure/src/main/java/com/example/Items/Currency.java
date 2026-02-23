package com.example.Items;

public class Currency {
    public enum CoinType { COPPER, SILVER, GOLD }

    private static final int COPPER_PER_SILVER = 10;
    private static final int COPPER_PER_GOLD = 100;

    public static int convert(CoinType from, CoinType to, int value) {
        if (from == to) return value;
        int copperValue = switch (from) {
            case COPPER -> value;
            case SILVER -> value * COPPER_PER_SILVER;
            case GOLD -> value * COPPER_PER_GOLD;
        };
        return switch (to) {
            case COPPER -> copperValue;
            case SILVER -> copperValue / COPPER_PER_SILVER;
            case GOLD -> copperValue / COPPER_PER_GOLD;
        };
    }
}
