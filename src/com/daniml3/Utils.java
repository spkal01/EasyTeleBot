package com.daniml3;

import java.io.*;
import java.lang.reflect.Field;
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
    public static Object getValueOf(Class theClass, String value) throws NoSuchFieldException, IllegalAccessException {
        Field field = theClass.getField(value);
        String fieldType = field.getType().toString();
        if (fieldType.equals("int")) {
            return field.getInt(theClass);
        } else if (fieldType.equals("double")) {
            return field.getDouble(theClass);
        } else {
            return field.get(theClass);
        }
    }
    public static String readFile(File file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();

        while (line != null) {
            stringBuilder.append(line);
            line = bufferedReader.readLine();
        }
        return stringBuilder.toString();
    }
    public static long getSize(File file) {
        return file.length();
    }
    public static String getLineFromFile(File file, String pattern) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();

        while (line != null) {
            if (line.contains(pattern)) {
                stringBuilder.append(line);
            }
            line = bufferedReader.readLine();
        }
        return stringBuilder.toString();
    }
}
