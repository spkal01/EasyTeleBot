package com.daniml3;

public class Utils {
    public static void print(String args) {
        System.out.print(args);
    }
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
