package com.daniml3;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Utils {
    public static void print(String args) {
        System.out.print(args);
    }
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public static void writeToFile(String file, String data) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(file));
            outputStream.write(data.getBytes(), 0, data.length());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
