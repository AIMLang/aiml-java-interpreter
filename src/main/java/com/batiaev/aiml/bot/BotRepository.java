package com.batiaev.aiml.bot;

import com.batiaev.aiml.chat.ChatContextStorage;
import com.batiaev.aiml.consts.AimlConst;

import java.io.File;

/**
 * Bot repository
 *
 * @author batiaev
 * @since 19/10/16
 */
public class BotRepository {
    private String rootDir = AimlConst.getRootPath();

    private ChatContextStorage chatContextStorage;

    public BotRepository(ChatContextStorage chatContextStorage) {
        this.chatContextStorage = chatContextStorage;
    }

    public void setRootPath(String path) {
        rootDir = path;
    }

    public Bot get() {
        return get(AimlConst.default_bot_name);
    }

    public Bot get(String name) {
        return new BotImpl(name, getBotPath(name), chatContextStorage);
    }

    private String getBotPath(String name) {
        return rootDir + File.separator + name + File.separator;
    }
}
