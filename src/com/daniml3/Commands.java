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
    }
}
