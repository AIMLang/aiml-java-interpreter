package com.batiaev.aiml;

import com.batiaev.aiml.bot.BotImpl;
import com.batiaev.aiml.bot.BotRepository;
import com.batiaev.aiml.channels.ConsoleChannel;
import com.batiaev.aiml.chat.Chat;
import com.batiaev.aiml.chat.InMemoryChatContextStorage;
import com.batiaev.aiml.consts.AimlConst;

/**
 * @author batiaev
 * @since 30/06/15
 */
public class App {
    private final BotRepository botRepository = new BotRepository(new InMemoryChatContextStorage());

    public static void main(String[] args) {
        String botName = args.length > 0 ? args[0] : AimlConst.default_bot_name;
        new App().init(botName);
    }

    public void init(String botName) {
        BotImpl bot = (BotImpl) botRepository.get(botName);

        ConsoleChannel consoleChannel = new ConsoleChannel(bot);
        consoleChannel.startChat("Tony");

        if (!bot.wakeUp())
            return;
        Chat chat = new Chat(bot, consoleChannel);
        chat.start();
    }
}