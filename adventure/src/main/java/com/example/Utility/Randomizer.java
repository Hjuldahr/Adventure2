package com.example.Utility;

import java.util.Random;

public class Randomizer {
    private static final Random random = new Random();

    public static int getInt(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    public static float getFloat() {
        return random.nextFloat();
    }

    public static float getFloat(float max) {
        return max * random.nextFloat();
    }

    public static float getFloat(float min, float max) {
        return (max - min) * random.nextFloat() + min;
    }

    public static boolean getBool() {
        return random.nextBoolean();
    }
}
