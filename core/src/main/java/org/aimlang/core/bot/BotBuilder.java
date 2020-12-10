package org.aimlang.core.bot;

import org.aimlang.core.chat.ChatContextStorage;
import org.aimlang.core.consts.AimlConst;

import java.io.File;

import static org.aimlang.core.chat.ChatContextStorageFactory.inMemory;

/**
 * Bot builder
 *
 * @author batiaev
 * @since 19/10/16
 */
public class BotBuilder {
    private ChatContextStorage context = inMemory();
    private String rootPath = AimlConst.getRootPath();
    private String botName;

    private BotBuilder(String botName) {
        this.botName = botName;
    }

    public static BotBuilder bot() {
        return new BotBuilder(AimlConst.default_bot_name);
    }

    public static BotBuilder bot(String botName) {
        return new BotBuilder(botName);
    }

    public BotBuilder withContext(ChatContextStorage context) {
        this.context = context;
        return this;
    }

    public BotBuilder fromSource(String rootPath) {
        this.rootPath = rootPath;
        return this;
    }

    public BotImpl wakeUp() {
        var botPath = getBotPath(botName);
        var bot = new BotImpl(botName, botPath, context);
        if (!bot.wakeUp()) {
            throw new IllegalStateException(
                    "Bot couldn't wake up, please check that '" + botPath + "' contains all required aiml files");
        }
        return bot;
    }

    private String getBotPath(String name) {
        return rootPath + File.separator + name + File.separator;
    }
}
