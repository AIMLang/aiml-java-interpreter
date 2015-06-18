package com.batiaev.aiml.core;

public class AIMLConst {

    public static String program_name_version = "Program Jarvis 0.0.1";
    public static String root_path = "./";
    public static String default_bot_name = "alice2";
    public static String aiml_file_suffix = ".aiml";
    public static String default_language = "en_US";
    public static String error_bot_response = "Something is wrong with my brain.";
    public static String default_bot_response = "I have no answer for that.";
    public static String null_input = "#NORESP";

    public static void setRootPath(String newRootPath) {
        root_path = newRootPath;
    }
    public static void setRootPath() {
        setRootPath(System.getProperty("user.dir"));
    }
}