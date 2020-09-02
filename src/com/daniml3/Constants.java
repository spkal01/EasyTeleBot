package com.daniml3;

public class Constants {
    public static String CONFIG_FILE="config.json";
    // The base interface which objects that are in brackets will be replaced by its values in InterfaceHandler
    public static String BASE_INTERFACE="EasyTeleBot running:\n\n" +
            "/*Tasks information*/\nNumber of running tasks:{tasks_count}\nRunning tasks:{tasks}" +
            "\n\n/*Bot information*/\nWarnings: {warnings}" +
            "\n\n/*Tracebacks*/\n{tracebacks}" +
            "\n\nLast update:{time}";
}