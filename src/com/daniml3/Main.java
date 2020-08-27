package com.daniml3;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import static com.daniml3.Utils.print;

// We want an infinite loop so this warning is useless
@SuppressWarnings("InfiniteLoopStatement")

public class Main {
    public static boolean loggedIn = false;

    public static void main(String[] args) throws IOException, JSONException, InterruptedException {
        // Check if the config file exists. The default path is the user's home directory + config.json
        File configFile = new File(System.getenv("HOME") + "/" + Constants.CONFIG_FILE);

        if (!configFile.exists()) {
            print("ERROR: the file config.json doesn't exist.\nCreate it with the following template and try again.");
            print("{\n  \"bot_token\": \"your_bot_token\",\n  \"chat_id\" : \"your_chat_id\",\n  \"password\" : \"bot_password\"\n}");
            System.exit(1);
        } else {
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

        Utils.clearScreen();
        print("################################################################################\n");
        print("------------------------------TELEGRAM BOT RUNNING------------------------------\n");
        print("Last message update: " + new Date());

        boolean firstExecution = true;
        while (true) {
            Telegram.getUpdates();
            if (! firstExecution) {
                // If there is a new message, check if the message is valid and if it is a command
                if (Telegram.newMessage) {
                    if (Telegram.lastMessage.contains("/")) {
                            new Thread(() -> {
                                try {
                                    String command = Telegram.lastMessage.replace("/", "");
                                    // Execute the command if it is the /start command (for logging in) or if the user is already logged
                                    if (command.equals("start") || loggedIn) {
                                        print("\nExecuting " + Telegram.lastMessage + "\n");
                                        /*
                                        Check if the CustomCommands class exists. If so, check if the given method (command) exists.
                                        If it exists, use the CustomCommand class for running the command. Else, use the Commands class.
                                        */
                                        Class<?> commandClass;
                                        try {
                                            Class.forName("com.daniml3.CustomCommands").getMethod(command);
                                            commandClass = Class.forName("com.daniml3.CustomCommands");
                                            /*
                                            ClassNotFoundException means that the CustomCommands class doesn't exist
                                            NoSuchMethodException means that the CustomCommands class exists, but doesn't contain the command.
                                            So, if any of this exceptions are thrown, use the Commands class.
                                            */
                                        } catch (ClassNotFoundException | NoSuchMethodException e) {
                                            commandClass = Class.forName("com.daniml3.Commands");
                                        }
                                        commandClass.getMethod(command).invoke(command);
                                    } else {
                                        // If the user sent a command that isn't /start and it isn't logged in, send a warning
                                        Telegram.sendMessage("You must login in first with /start");
                                    }
                                } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
                                    // Send a message to the user for possible exceptions
                                    e.printStackTrace();
                                    Telegram.sendMessage("Bot exception. Does your command exist? Check the bot log for details. Hint: " + e.getClass().getSimpleName());
                                } catch (NoSuchMethodException e) {
                                    // NoSuchMethodException means that the command doesn't exist
                                    Telegram.sendMessage("Command not found");
                                }
                            }).start();
                    } else if (Telegram.lastMessage.equals("invalid_message")) {
                        // Warn the user about an invalid message (like a GIF)
                        Telegram.sendMessage("Invalid message");
                    }
                }
                print("\rLast message update: " + new Date());
            }
            //noinspection BusyWait
            Thread.sleep(1000);
            firstExecution = false;
        }
    }
}