package com.daniml3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CustomCommands {
    public static void build() throws IOException {
        // Arrays for the available build arguments and the selected build arguments
        ArrayList<String> buildArgsArrayList = new ArrayList<>(Arrays.asList("sync", "clean", "ccache", "gapps", "no-repopick", "no-checkout"));
        ArrayList<String> selectedArgs = new ArrayList<>();

        // Some booleans to control argument parsing
        boolean argumentsUpdated = false;
        boolean finishedParsingArgs = false;

        // Messages variables
        String lastMessage;
        boolean newMessage;

        StringBuilder buildCommand = new StringBuilder().append("bash /root/scheduler ");

        Telegram.sendMessage("Send the build options\nAvailable arguments: {args}\nSend the message 'Done' when you are ready"
                .replace("{args}", buildArgsArrayList.toString()));

        while (!finishedParsingArgs) {
            lastMessage = Telegram.lastMessage.toLowerCase();
            newMessage = Telegram.newMessage;

            for (String word : lastMessage.split(" ")) {
                if (!finishedParsingArgs) {
                    if (buildArgsArrayList.contains(word) && newMessage && !selectedArgs.contains(word)) {
                        selectedArgs.add(word);
                        argumentsUpdated = true;
                    } else if (word.equals("done") && newMessage) {
                        Telegram.sendMessage("Build will be executed with the following arguments: " + selectedArgs);
                        argumentsUpdated = true;
                        finishedParsingArgs = true;
                    } else if (selectedArgs.contains(word) && newMessage) {
                        Telegram.sendMessage("Argument {arg} already added".replace("{arg}",word));
                    } else if (newMessage) {
                        Telegram.sendMessage("Argument {arg} invalid".replace("{arg}",word));
                    }
                }
            }

            if (argumentsUpdated && !finishedParsingArgs) {
                Telegram.sendMessage("Current argument list: " + selectedArgs);
                argumentsUpdated = false;
            }
        }

        for (String arg : selectedArgs) {
            buildCommand.append("--").append(arg).append(" ");
        }

        Runtime.getRuntime().exec(buildCommand.toString());
    }
}
