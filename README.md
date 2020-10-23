# EasyTeleBot
```
EasyTeleBot. An utility for making telegram bots the easy way.
Copyright (C) 2020 daniml3 All Rights Reserved.
This program is licensed under the GNU GPLv3 license.
```

## Table of contents 
1. [ Introduction ](#1-introduction)
2. [ Functions documentation ](#2-functions-documentation)
3. [ Usage ](#3-usage)
4. [ Creating commands ](#4-creating-commands)

## 1. Introduction
This tool allows you to easily create a Telegram bot (obviously, if you know Java).
You can create a command by simply writing its code. You don't need to modify any
command list.

## 2 Functions documentation
```
src-|
    |-com.daniml3-|
                  |-Commands.java-  This contains the default bot commands. Shouldn't be 
                  |                 edited.
                  |
                  |-Constants.java- This contains the values that are always the same. 
                  |                 Can be edited.
                  |
                  |-Main.java-      As its name says, it's the main class which will be
                  |                 running an infinite loop for listening to the
                  |                 latest messages and for running the commands.
                  |
                  |-Telegram-       This class contains a function for calling the
                  |                 Telegram API. It also contains some general methods,
                  |                 like sendMessage (for sending a message), getUpdates,
                  |                 that gets latest updates from the API, like the
                  |                 latest message.
                  |
                  |-Utils-          Some tools like a simplification of
                                    System.out.print();
```

* More documentation inside of each class.

## 3. Usage
* First, you need to create a file in your home directory (variable $HOME on Shell), called config.json.
The config file name can be customized from the Constants.java.
The file format is:
```
    {
      "bot_token": "your_bot_token",
      "chat_id" : "your_chat_id",
      "allowed_user" : "user_id"
    }
```
* The bot_token field is the bot key that you can get from BotFather when you have created a new bot
* The chat_id field is the chat id where the messages will be sent
* The allowed_user is the user id whose commands will be accepted. If other users try to run commands, they will not be executed. In a near update this will be changed into a list so more than one user can execute the commands.

* After creating the configuration file with your data, you need to create the commands.
For creating your custom commands, you shouldn't edit the Commands.java as there are all
the general bot commands, not the custom commands.

## 4. Creating commands
* For your custom commands, you should create a class called CustomCommands (CustomCommands.java),
 and you must create functions to run in that class.
* For naming your commands, you should follow this example:
    * If you want a command called `build`, you should write in the `CustomCommands`
    class a function with the command name. In this case, the function would be:
    ```
    public static void build() {
        code_to_execute;
    }
    ```
* If you want your command to have user interaction, like asking for a thing, you can use
the Telegram.getUpdates() function. There is an example:
    ```
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
    ```
 ## 5. Building the bot
 * For building the bot, you can use IntelliJ IDEA and open the project for building, or
 you can build with `ant`.
 * For building with `ant`:
     ```
    $ ant
    ```
   * You will get the output jar at out/artifacts/EasyTeleBot_jar/EasyTeleBot.jar
   * For running it just do: `java -jar path_to_jar` (`java -jar out/artifacts/EasyTeleBot_jar/EasyTeleBot.jar`)
