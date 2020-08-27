package com.daniml3;

import org.json.JSONException;

import java.io.IOException;

public class Commands {
    public static void start() throws IOException, JSONException {
        int attempts = 3;
        Telegram.sendMessage("Enter your password:");
        while (true) {
            Telegram.getUpdates();
            if (Telegram.lastMessage.equals(Telegram.password) && Telegram.newMessage) {
                Telegram.sendMessage("Logged in successfully");
                Main.loggedIn = true;
                break;
            } else if (Telegram.newMessage) {
                attempts--;
                Telegram.sendMessage("Left attemps: "+ attempts);
            }
            if (attempts == 0) {
                Telegram.sendMessage("Stopping the bot due to security reasons");
                System.exit(1);
            }
        }
    }
}
