package com.daniml3;

public class Commands {
    public static void status() {
        Telegram.sendMessage("Hey! I'm alive\nI'm running at {os}\nJava Runtime version: {jre}\nUser: {user}"
                .replace("{os}",System.getProperty("os.name"))
                .replace("{jre}",System.getProperty("java.version"))
                .replace("{user}",System.getProperty("user.name")));
    }
    
public static void build() {
    code_to_execute;
    }
}    
