package com.daniml3;

public class Constants {
    public static final String CONFIG_FILE="config.json";
    public static final String USAGE_MESSAGE="Usage: java -jar \"path_to_jar\" --config \"path_to_config_file\"\n";
    // The base interface which objects that are in brackets will be replaced by its values in InterfaceHandler
    public static final String BASE_INTERFACE="EasyTeleBot running:\n\n" +
            "/*Tasks information*/\nNumber of running tasks:{tasks_count}\nRunning tasks:{tasks}" +
            "\n\n/*Bot information*/\nWarnings: {warnings}" +
            "\n\n/*Tracebacks*/\n{tracebacks}" +
            "\n\nLast update:{time}";
}