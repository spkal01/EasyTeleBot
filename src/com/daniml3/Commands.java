package com.daniml3;

public class Commands {
    public static void status() {
        Telegram.sendMessage("Hey! I'm alive\nI'm running at {os}\nJava Runtime version: {jre}\nUser: {user}"
                .replace("{os}",System.getProperty("os.name"))
                .replace("{jre}",System.getProperty("java.version"))
                .replace("{user}",System.getProperty("user.name")));
    }
 
    public static void build() {
        Telegram.sendMessage("You sure you want yo start a build\nUser: {user}"
        .replace("{user}",System.getProperty("user.name")))                             
    public static void yes_no() throws IOException, JSONException, InterruptedException {
    Telegram.sendMessage("Yes or no?");
    while (true) {
        // We need to run getUpdates when we want to update the last message
        Telegram.getUpdates();
        // Check if the last message is yes or no and send the opposite.
        if (Telegram.lastMessage.toLowerCase().equals("yes")) {
            Telegram.sendMessage("no");
            // Break the infinite loop as we don't want this command to be infinite.
            break;
        }
        if (Telegram.lastMessage.toLowerCase().equals("no")) {
            Telegram.sendMessage("yes");
            // Break the infinite loop as we don't want this command to be infinite.
            break;
        }
        // Wait 1 second so Telegram.lastMessage is updated correctly
        Thread.sleep(1000);            
        }
}
