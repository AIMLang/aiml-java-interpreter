package org.aimlang.app;

import org.aimlang.core.channels.ChannelType;
import org.aimlang.core.consts.AimlConst;

import static org.aimlang.core.bot.BotBuilder.bot;
import static org.aimlang.core.chat.ChatContextStorageFactory.inMemory;

/**
 * @author batiaev
 * @since 30/06/15
 */
public class App {
    public static void main(String[] args) {
        if (args.length < 1)
            throw new IllegalStateException("user name is required!");
        var userName = args[0];

        var bot = bot(AimlConst.default_bot_name)
                .withContext(inMemory())
                .fromSource(AimlConst.getRootPath())
                .wakeUp();

        bot.startChat(userName, ChannelType.CONSOLE);
    }
}