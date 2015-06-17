package com.batiaev.aiml.core;

/**
 * Created by anton on 13/06/15.
 * ---
 * Copyright © 2015. Anton Batiaev. All Rights Reserved.
 * www.batiaev.com
 */
public class AIMLTag {

    public static String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    public static String aiml = "aiml";
    public static String pattern = "pattern";
    public static String template = "template";
    public static String random = "random";
    public static String li = "li";
    public static String star = "star";
    public static String bot = "bot";
    public static String set = "set";
    public static String get = "get";
    public static String think = "think";
    public static String srai = "srai";
    public static String sraix = "sraix";
    public static String map = "map";
    public static String that = "that";
    public static String condition = "condition";
    public static String loop = "condition";
    public static String learn = "learn";
    public static String learnf = "learnf";
    public static String eval = "eval";

    public static String startTag(String name) {
        return "<" + name + ">";
    }

    public static String endTag(String name) {
        return "</" + name + ">";
    }

    public static String singleTag(String name) {
        return "<" + name + "/>";
    }

    public static String doubleTag(String name, String value) {
        return startTag(name) + value + endTag(name);
    }

}
