package org.aimlang.app;

import org.aimlang.core.channels.ConsoleChannel;
import org.aimlang.core.channels.TelegramChannel;
import org.aimlang.core.consts.AimlConst;

import static org.aimlang.core.bot.BotBuilder.bot;
import static org.aimlang.core.chat.ChatContextStorageFactory.inMemory;

/**
 * @author batiaev
 * @since 30/06/15
 */
public class TelegramBot {

    public static void main(String[] args) {
        var botName = AimlConst.default_bot_name;
//        var provider = new TelegramChannel(args[0], args[1]);
        var provider = new ConsoleChannel(botName);
        bot(botName)
                .withContext(inMemory())
                .fromSource(AimlConst.getRootPath())
                .wakeUp()
                .buildChat(provider)
                .start();
    }
}
