package com.example.Utility;

public class Clamped {
    private int value;
    private int lower;
    private int upper;

    public static int Clamp(int lower, int value, int upper)
    {
        return Math.min(Math.max(lower, value), upper);
    }

    public Clamped(int value, int upper) {
        this(0, value, upper);
    }

    public Clamped(int lower, int value, int upper) {
        this.value = value;
        this.lower = lower;
        this.upper = upper;
    }

    public Clamped(Clamped other) {
        this(other.lower, other.value, other.upper);
    }

    public int getMinValue() {
        return lower;
    }

    private void applySwap() {
        int temp = lower;
        lower = upper;
        upper = temp;
    }

    public void setLower(int lower) {
        this.lower = lower;

        if (lower > upper) {
            applySwap();
        }
    }

    public int getMaxValue() {
        return upper;
    }

    public void setUpper(int upper) {
        this.upper = upper;

        if (upper < lower) {
            applySwap();
        }
    }

    public int reverse() {
        return upper - (value - lower);
    }

    public int getValue() {
        return value;
    }

    public boolean setSafeValue(int newValue) {
        if (inBounds(newValue)) {
            value = newValue;
            return true;
        }
        return false;
    }

    public int setValue(int newValue) {
        value = Math.min(Math.max(lower, value), upper);
        return value;
    }

    public void minimize() {
        value = lower;
    }

    public void maximize() {
        value = upper;
    }

    public boolean atMinimum() {
        return value == lower;
    }

    public boolean atMaximum() {
        return value == upper;
    }

    public float getRatio() {
        return (float) value / (float) upper;
    }

    public float getReverseRatio() {
    return 1 - getRatio();
}

    public boolean decrement() {
        if (value > lower) {
            value--;
            return true;
        }
        return false;
    }

    public boolean decrement(int change) {
        if (value - change >= lower) {
            value -= change;
            return true;
        }
        return false;
    }

    public boolean increment() {
        if (value < upper) {
            value++;
            return true;
        }
        return false;
    }

    public boolean increment(int change) {
        if (value + change <= upper) {
            value += change;
            return true;
        }
        return false;
    }

    // current value test
    public boolean inBounds() {
        return value >= lower && value <= upper;
    }

    // manual value test
    public boolean inBounds(int testedValue) {
        return testedValue >= lower && testedValue <= upper;
    }

    public boolean change(int change) {
        if (inBounds(value + change)) {
            value += change;
            return true;
        }
        return false;
    }

    public void shift(int change) {
        lower += change;
        value += change;
        upper += change;
    }

    @Override
    public String toString() {
        return "Clamped[value=%d, lower=%d, upper=%d]".formatted(value, lower, upper);
    }
}
