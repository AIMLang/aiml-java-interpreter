package com.batiaev.aiml.consts;

/**
 * Aiml tags
 *
 * @author batiaev
 * @since 13/06/15
 */
public class AimlTag {

    public static final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    public static final String aiml = "aiml";
    public static final String category = "category";
    public static final String topic = "topic";
    public static final String pattern = "pattern";
    public static final String template = "template";
    public static final String random = "random";
    public static final String li = "li";
    public static final String star = "star";
    public static final String index = "index";
    public static final String bot = "bot";
    public static final String set = "set";
    public static final String get = "get";
    public static final String think = "think";
    public static final String srai = "srai";
    public static final String sraix = "sraix";
    public static final String map = "map";
    public static final String that = "that";
    public static final String condition = "condition";
    public static final String loop = "loop";
    public static final String learn = "learn";
    public static final String learnf = "learnf";
    public static final String eval = "eval";
    public static final String text = "#text";
    public static final String comment = "#comment";
    //Attributes
    public static final String name = "name";

    public static String getCloseTag(String name) {
        return "</" + name + ">";
    }

    public static String getOpenTag(String name) {
        return "<" + name + ">";
    }
}
