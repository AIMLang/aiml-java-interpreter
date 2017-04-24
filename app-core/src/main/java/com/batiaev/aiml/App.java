package com.batiaev.aiml;

import com.batiaev.aiml.bot.BotImpl;
import com.batiaev.aiml.bot.BotRepository;
import com.batiaev.aiml.channels.Channel;
import com.batiaev.aiml.channels.ConsoleChannel;
import com.batiaev.aiml.channels.Provider;
import com.batiaev.aiml.chat.Chat;
import com.batiaev.aiml.exception.BotNotInitializedException;
import com.batiaev.aiml.exception.ChatNotStartedException;

/**
 * @author batiaev
 * @since 30/06/15
 */
public class App {

    public static void main(String[] args) {
        BotImpl bot = (BotImpl) BotRepository.get();
        Provider provider = new ConsoleChannel(bot);

        Channel consoleChannel = new ConsoleChannel(bot);
        consoleChannel.startChat("Tony");
        try {
            consoleChannel.send("Привет!");
        } catch (ChatNotStartedException | BotNotInitializedException e) {
            e.printStackTrace();
        }

        if (!bot.wakeUp())
            return;
        Chat chat = new Chat(bot, provider);
        chat.start();
    }
}