package com.batiaev.aiml;

import com.batiaev.aiml.chat.Chat;
import com.batiaev.aiml.core.AIMLConst;
import com.batiaev.aiml.core.Bot;

/**
 * @author batiaev
 */
public class App {

    public static void main(String[] args) {
        AIMLConst.setRootPath("./app-core");
        mainFunction(args);
    }

    private static void mainFunction(String[] args) {
        Bot bot = new Bot();
        Chat chat = new Chat(bot);
        chat.start();
    }
}