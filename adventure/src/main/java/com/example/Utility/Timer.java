package com.example.Utility;

public class Timer {
    private int time;

    public Timer() {
        this(0);
    }

    public Timer(int time) {
        this.time = time;
    }

    public void set(int time) {
        this.time = time;
    }

    public int get() {
        return time;
    }

    public boolean atZero() {
        return time == 0;
    }

    public void increment() {
        time++;
    }

    public void decrement() {
        if (time > 0)
            time--;
    }

    public void reset() {
        time = 0;
    }
}
