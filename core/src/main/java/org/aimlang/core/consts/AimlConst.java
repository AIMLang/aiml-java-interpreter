package org.aimlang.core.consts;

import java.io.File;
import java.nio.file.Path;

/**
 * Aiml constants
 *
 * @author batiaev
 * @since 6/17/15
 */
public class AimlConst {

    public static final String AIML_FILE_SUFFIX = ".aiml";

    private static String root_path = Path.of("./bots/src/main/resources".replace("/",File.separator))
            .toAbsolutePath().toString();
    public static final String default_bot_name = "jokebot";
    public static final String error_bot_response = "Something is wrong with my brain.";
    public static final String default_bot_response = "I have no answer for that.";
    public static final String default_topic = "unknown";
    public static final String default_that = "unknown";
    public static final String null_input = "#NORESP";
    public static boolean debug = false;

    public static String getRootPath() {
        return root_path;
    }

    public static void setRootPath(String newRootPath) {
        root_path = newRootPath;
    }
}