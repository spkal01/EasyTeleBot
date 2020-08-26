package com.daniml3;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        print("\rExecuting " + Telegram.lastMessage);
                                        print("\n");
                                        if (Telegram.lastMessage.equals("/start") || loggedIn){
                                            Method runCommandMethod = Commands.class.getMethod(Telegram.lastMessage.replace("/", ""));
                                            runCommandMethod.invoke(Telegram.lastMessage.replace("/", ""));
                                        }
                                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException err) {
                                        try {
                                            Telegram.sendMessage("Command not found");
                                        } catch (IOException | JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                            }
                        } ).start();
                    } else if (Telegram.lastMessage.equals("invalid_message")) {

                    }
                }
                print("\rLast message update: " + new Date());
            }
            Thread.sleep(1000);
            firstExecution = false;
        }
    }
}
