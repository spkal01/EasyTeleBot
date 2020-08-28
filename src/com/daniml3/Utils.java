package com.daniml3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

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
    public static File[] listFiles(File directory,String startPatten ,String extension) {
        // startPattern and extension may be null if you don't want to use any patterns
        if (extension == null && startPatten == null) {
            return directory.listFiles();
        } else {
            return directory.listFiles((dir, filename) -> filename.startsWith(startPatten) && filename.endsWith(Objects.requireNonNull(extension)));
        }
    }
}
