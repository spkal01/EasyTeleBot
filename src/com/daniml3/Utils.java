package com.daniml3;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Objects;

public class Utils {
    // Shortened System.out.print()
    public static void print(String args) {
        System.out.print(args);
    }

    // Clear the console content. Doesn't work on IDEs
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                Runtime.getRuntime().exec("cmd /c cls");
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException e) {
            InterfaceHandler.addWarning("Could not clear the screen correctly", 10);
            InterfaceHandler.addStackTrace(Utils.stackTraceToString(e));
        }
    }

    // Write a string to a file
    public static void writeToFile(String file, String data) {
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(new File(file));
            outputStream.write(data.getBytes(), 0, data.length());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Return the list of files in a directory. The function can follow a pattern.
    public static File[] listFiles(File directory,String startPatten ,String extension) {
        // startPattern and extension may be null if you don't want to use any patterns
        if (extension == null && startPatten == null) {
            return directory.listFiles();
        } else {
            return directory.listFiles((dir, filename) -> filename.startsWith(startPatten) && filename.endsWith(Objects.requireNonNull(extension)));
        }
    }

    // Get a specific value of an object in a specific class with its name in a string
    public static Object getValueOf(Class<?> theClass, String value) throws NoSuchFieldException, IllegalAccessException {
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

    // Read from a file and return its content
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

    // Get lines from a file matching a pattern
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

    // Shortened Thread.sleep function
    public static void sleep(int mills) {
        try {Thread.sleep(mills);} catch ( InterruptedException ignore) {}
    }

    // Return the exception output in form of a string
    public static String stackTraceToString(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
}
