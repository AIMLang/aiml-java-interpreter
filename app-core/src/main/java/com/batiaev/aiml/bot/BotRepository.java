package com.batiaev.aiml.bot;

import com.batiaev.aiml.core.AIMLConst;

import java.io.File;

/**
 * Created by anbat on 19/10/16.
 *
 * @author anbat
 */
public class BotRepository {
    private static String rootDir = AIMLConst.getRootPath();

    public static void setRootPath(String path) {
        rootDir = path;
    }

    public static Bot get() {
        return get(AIMLConst.default_bot_name);
    }

    public static Bot get(String name) {
        return new Bot(name, getBotPath(name));
    }

    private static String getBotPath(String name) {
        return rootDir + File.separator + name + File.separator;
    }
}
