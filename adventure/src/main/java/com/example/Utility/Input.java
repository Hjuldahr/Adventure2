package com.example.Utility;

import java.util.Scanner;

public class Input 
{
    private static final Scanner scanner = new Scanner(System.in);

    public static String getString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static int getInt(String prompt, int lower, int upper) {
        System.out.print(prompt);
        int n = scanner.nextInt();
        while (lower < n || n > upper) {
            System.out.printf("Input must be between %d and %d!\n%s", lower, upper, prompt);
            n = scanner.nextInt();
        }
        return n;
    }

    public static boolean getBool(String prompt, String yes, String no) {
        System.out.print(prompt);
        while (true) {
            String s = scanner.nextLine().strip().toUpperCase();
            if (s.isEmpty()) {
                return true;
            } else if (s == yes || s == no) {
                return s.equalsIgnoreCase(yes);
            }
            System.out.printf("Input must be %s or %s!\n%s", yes, no, prompt);
        }
    }

    public static String getChoice(String prompt, String[] choices) {
        System.out.print(prompt);
        while (true) {
            String s = scanner.nextLine().strip().toUpperCase();
            for (String choice : choices) {
                // partial matching
                if (choice.startsWith(s)) {
                    return choice;
                }
            }
            System.out.printf("Input must be in options!\n%s", prompt);
        }
    }

    public static void getAny(String prompt) {
        System.out.print(prompt);
        scanner.nextLine();
    }
}
