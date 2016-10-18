package com.batiaev.aiml;

import com.batiaev.aiml.chat.Chat;
import com.batiaev.aiml.core.Bot;
import com.batiaev.aiml.providers.ConsoleProvider;
import com.batiaev.aiml.providers.Provider;

/**
 * @author anbat
 */
public class App {

    public static void main(String[] args) {
        Provider provider = new ConsoleProvider();
        Bot bot = new Bot();
        if (!bot.wakeUp())
            return;
        Chat chat = new Chat(bot, provider);
        chat.start();
    }
}