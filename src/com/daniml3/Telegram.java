package com.daniml3;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Telegram {
    public static int updateId = 0;
    public static boolean newMessage;
    public static String lastMessage;
    public static String botToken;
    public static String chatId;
    public static String password;

    public static JSONObject telegram(String method, String args) throws IOException, JSONException {
        URL telegramAPIUrl = new URL("https://api.telegram.org/bot{bot_token}/{method}{args}".replace("{bot_token}",
                botToken).replace("{method}", method).replace("{args}", args).replace(" ", "%20"));
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(telegramAPIUrl.openStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;
        // Get the API response and save it in a StringBuilder
        while ((inputLine = bufferedReader.readLine()) != null)
            response.append(inputLine);
        bufferedReader.close();

        // Return the API response
        return new JSONObject(response.toString());
    }

    public static void sendMessage(String message) {
        try {
            telegram("sendMessage", "?chat_id=" + chatId + "&text=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getUpdates() throws IOException, JSONException {
        JSONObject telegramResponse;
        int newUpdateId;

        /*
        If the updateId is 0 (the default value), get the last telegram updates without any offset
        If it isn't 0, this means an update was already executed, so set the offset to the latest update id
        */
        if (updateId == 0) {
            telegramResponse = telegram("getUpdates","");
        } else {
            telegramResponse = telegram("getUpdates","?offset={update_id}".replace("{update_id}",String.valueOf(updateId)));
        }
        // Get the last JSON object from the result array
        telegramResponse = telegramResponse.getJSONArray("result").getJSONObject(telegramResponse.getJSONArray("result").length()-1);

        // Get the update id of the last message
        newUpdateId = telegramResponse.getInt("update_id");

        //If the latest update id is higher than the previous update id, a new message was sent, so set the newMessage boolean to true
        newMessage = updateId < newUpdateId;

        // Set the updateId as the last update id
        updateId = newUpdateId;

        /*
         Save the last message in the lastMessage String. If the key text isn't in the message, this means that the user sent a non-text object, like a GIF.
         In the case the user didn't send text, set the last message to invalid_message
        */
        try {
            lastMessage = telegramResponse.getJSONObject("message").getString("text");
        } catch (JSONException err) {
            lastMessage="invalid_message";
        }
    }
}
