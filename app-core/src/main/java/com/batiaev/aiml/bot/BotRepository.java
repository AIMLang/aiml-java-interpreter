package com.batiaev.aiml.bot;

import com.batiaev.aiml.consts.AimlConst;

import java.io.File;

/**
 * Created by anbat on 19/10/16.
 *
 * @author anbat
 */
public class BotRepository {
    private static String rootDir = AimlConst.getRootPath();

    public static void setRootPath(String path) {
        rootDir = path;
    }

    public static Bot get() {
        return get(AimlConst.default_bot_name);
    }

    public static Bot get(String name) {
        return new Bot(name, getBotPath(name));
    }

    private static String getBotPath(String name) {
        return rootDir + File.separator + name + File.separator;
    }
}
