package com.batiaev.aiml.core;

/**
 * @author batiaev
 * Created by batyaev on 6/17/15.
 * ---
 * Copyright © 2015. Anton Batiaev. All Rights Reserved.
 * www.batiaev.com
 */
public class AIMLConst {

    public static String root_path = ".";
    public static String default_bot_name = "russian";
    public static String aiml_file_suffix = ".aiml";
    public static String default_language = "en_US";
    public static String error_bot_response = "Something is wrong with my brain.";
    public static String default_bot_response = "I have no answer for that.";
    public static String default_topic = "unknown";
    public static String default_that = "unknown";
    public static String null_input = "#NORESP";
    public static boolean debug = false;
    public static int loopLimit = 20;

    public static void setRootPath(String newRootPath) {
        root_path = newRootPath;
    }
    public static void setRootPath() {
        setRootPath(System.getProperty("user.dir"));
    }
}