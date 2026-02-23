package com.example.Utility;

public class DisplayBar 
{
    private static final String FILLED = "#";
    private static final String EMPTY = " ";
    
    public static String generateBar(int value, int vmin, int vmax, int length) {
        // normalize value
        int n = (int) Math.floor(((float)(value - vmin) / (float)(vmax - vmin)) * length);
        return "[%s%s]".formatted(FILLED.repeat(n), EMPTY.repeat(length - n));
    }
}
