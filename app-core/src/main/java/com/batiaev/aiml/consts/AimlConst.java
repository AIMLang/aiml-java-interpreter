package com.batiaev.aiml.consts;

import java.io.File;

/**
 * Aiml constants
 *
 * @author anbat
 * @since 6/17/15
 */
public class AimlConst {

    private static String root_path = System.getProperty("user.dir") + File.separator + "bots/";
    public static final String default_bot_name = "russian";
    public static final String aiml_file_suffix = ".aiml";
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