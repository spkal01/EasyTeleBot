package com.daniml3;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

import static com.daniml3.Utils.print;

// We want an infinite loop so this warning is useless
@SuppressWarnings("InfiniteLoopStatement")

public class Main {
    public static boolean loggedIn = false;

    public static void main(String[] args) throws IOException, JSONException {
        // Check if the config file exists. The default path is the user's home directory + config.json
        File configFile = new File(System.getenv("HOME") + "/" + Constants.CONFIG_FILE);

        if (!configFile.exists()) {
            // If the config file doesn't exist, print a template
            print("ERROR: the file config.json doesn't exist.\nCreate it with the following template and try again.");
            print("\n{\n  \"bot_token\": \"your_bot_token\",\n  \"chat_id\" : \"your_chat_id\",\n  \"password\" : \"bot_password\"\n}\n");
            System.exit(1);
        } else {
            // Load the config file to a bufferReader and then parse the JSON. If it isn't valid, throw an error
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line = bufferedReader.readLine();

                while (line != null) {
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }
                try {
                    // Set the bot token and chat id according to the config in config.json
                    Telegram.botToken = new JSONObject(stringBuilder.toString()).getString("bot_token");
                    Telegram.chatId = new JSONObject(stringBuilder.toString()).getString("chat_id");
                    Telegram.password = new JSONObject(stringBuilder.toString()).getString("password");
                } catch (JSONException err) {
                    print("Error while parsing the config file");
                    System.exit(1);
                }
            }
        }

        InterfaceHandler.startInterface();
        boolean firstExecution = true;
        while (true) {
            Telegram.getUpdates();
            if (! firstExecution) {
                // If there is a new message, check if the message is valid and if it is a command
                if (Telegram.newMessage) {
                    if (Telegram.lastMessage.contains("/")) {
                            new Thread(() -> {
                                String command = Telegram.lastMessage.replace("/", "");
                                try {
                                    // Execute the command if it is the /start command (for logging in) or if the user is already logged
                                    if (command.equals("start") || loggedIn) {
                                        /*
                                        * Check if the CustomCommands class exists. If so, check if the given method (command) exists.
                                        * If it exists, use the CustomCommand class for running the command. Else, use the Commands class.
                                        */
                                        Class<?> commandClass;
                                        try {
                                            Class.forName("com.daniml3.CustomCommands").getMethod(command);
                                            commandClass = Class.forName("com.daniml3.CustomCommands");
                                            /*
                                            * ClassNotFoundException means that the CustomCommands class doesn't exist
                                            * NoSuchMethodException means that the CustomCommands class exists, but doesn't contain the command.
                                            * So, if any of this exceptions are thrown, use the Commands class.
                                            */
                                        } catch (ClassNotFoundException | NoSuchMethodException e) {
                                            commandClass = Class.forName("com.daniml3.Commands");
                                        }
                                        InterfaceHandler.runningTasks.add(command);
                                        commandClass.getMethod(command).invoke(command);
                                        InterfaceHandler.runningTasks.remove(command);
                                    } else {
                                        // If the user sent a command that isn't /start and it isn't logged in, send a warning
                                        Telegram.sendMessage("You must login in first with /start");
                                    }
                                } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
                                    // Send a message to the user for possible exceptions
                                    Telegram.sendMessage("Bot exception. Does your command exist? Check the bot log for details. Hint: "
                                            + e.getClass().getSimpleName());
                                    /*
                                    * Remove the command from the running tasks, add the warning with the exception name for 10 seconds
                                    * and show the stacktrace
                                    */
                                    InterfaceHandler.runningTasks.remove(command);
                                    InterfaceHandler.addWarning(e.getClass().getSimpleName(), 10);
                                    InterfaceHandler.addStackTrace(Utils.stackTraceToString(e));
                                } catch (NoSuchMethodException e) {
                                    // NoSuchMethodException means that the command doesn't exist
                                    Telegram.sendMessage("Command not found");
                                    InterfaceHandler.runningTasks.remove(command);
                                }
                            }).start();
                    }
                }
            }
            Utils.sleep(1000);
            firstExecution = false;
        }
    }
}
