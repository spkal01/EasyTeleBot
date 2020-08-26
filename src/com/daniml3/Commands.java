package com.daniml3;

import org.json.JSONException;

import java.io.IOException;

public final class Commands {
    public static void start() throws IOException, JSONException {
        int attemps = 3;
        Telegram.sendMessage("Enter your password:");
        while (true) {
            Telegram.getUpdates();
            if (Telegram.lastMessage.equals(Telegram.password) && Telegram.newMessage) {
                Telegram.sendMessage("Logged in successfully");
                Main.loggedIn = true;
                break;
            } else if (Telegram.newMessage) {
                attemps--;
                Telegram.sendMessage("Left attemps: "+ String.valueOf(attemps));
            }
            if (attemps == 0) {
                System.exit(1);
            }
        }
    }
}
