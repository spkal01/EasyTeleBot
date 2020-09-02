package com.daniml3;

import java.util.ArrayList;
import java.util.Date;

public class InterfaceHandler {
    public static Thread interfaceThread;
    public static ArrayList<String> runningTasks = new ArrayList<>();
    public static ArrayList<String> warnings = new ArrayList<>();
    public static ArrayList<String> stackTraces = new ArrayList<>();

    public static void startInterface() {
        interfaceThread = new Thread(() -> {
            while (true) {
                /*
                * Clear the screen and print the interface replacing the objects in brackets with its values
                * Also, wait 0.5 seconds so we don't have a print overflow
                */
                Utils.clearScreen();
                Utils.print(Constants.BASE_INTERFACE.replace("{tasks}",runningTasks.toString())
                        .replace("{tasks_count}",String.valueOf(runningTasks.size()))
                        .replace("{warnings}",warnings.toString())
                        .replace("{tracebacks}",stackTraces.toString())
                        .replace("{time}",new Date().toString()));
                Utils.sleep(500);
            }
        });
        interfaceThread.start();
    }

    /*
    * Add a warning to the warnings ArrayList that will be shown in the interface
    * Also, remove the warning after a defined time
    * If the seconds int is 0, leave the warning forever
    */
    public static void addWarning(String warning, int seconds) {
        warnings.add(warning);
        if (seconds != 0) {
            new Thread(() -> {
                Utils.sleep(seconds * 1000);
                warnings.remove(warning);
            }).start();
        }
    }

    /*
    * Add a string to the stackTraces ArrayList that will be shown on the interface
    * Remove them after 30 seconds
    */
    public static void addStackTrace(String stacktrace) {
        stackTraces.add(stacktrace);
        new Thread(() -> {
            Utils.sleep(30000);
            stackTraces.remove(stacktrace);
        }).start();
    }
}
