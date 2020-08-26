package com.daniml3;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Telegram {
    public static int updateId = 0;
    public static boolean newMessage;
    public static String lastMessage;
    public static String botToken;
    public static String chatId;
    public static String password;

    public static JSONObject telegram(String method, String args) throws IOException, JSONException {
        URL telegramAPIUrl = new URL("https://api.telegram.org/bot{bot_token}/{method}{args}".replace("{bot_token}",
                botToken).replace("{method}", method).replace("{args}", args));
        BufferedReader in = new BufferedReader(
                new InputStreamReader(telegramAPIUrl.openStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        in.close();

        try {
            JSONArray result;
            result = new JSONObject(response.toString()).getJSONArray("result");
            return new JSONObject(result.getJSONObject(result.length()-1).toString());
        } catch (JSONException err) {
            JSONObject result;
            result = new JSONObject(response.toString()).getJSONObject("result");
            return new JSONObject(result.toString());
        }
    }

    public static void sendMessage(String message) throws IOException, JSONException {
        telegram("sendMessage", "?chat_id=" + chatId + "&text=" + message);
    }

    public static void getUpdates() throws IOException, JSONException {
        JSONObject telegramResponse;
        int newUpdateId;

        if (updateId == 0) {
            telegramResponse = telegram("getUpdates","");
        } else {
            telegramResponse = telegram("getUpdates","?offset={update_id}".replace("{update_id}",String.valueOf(updateId)));
        }
        newUpdateId = telegramResponse.getInt("update_id");
        newMessage = updateId < newUpdateId && !newMessage;

        // Set the updateId as the last update id
        updateId = newUpdateId;

        // Save the last message in the lastMessage String. If the key text isn't in the message, this means that the user sent a non-text object, like a GIF.
        // In the case the user didn't send text, set the last message to invalid_message
        try {
            lastMessage = telegramResponse.getJSONObject("message").getString("text");
        } catch (JSONException err) {
            lastMessage="invalid_message";
        }
    }
}
