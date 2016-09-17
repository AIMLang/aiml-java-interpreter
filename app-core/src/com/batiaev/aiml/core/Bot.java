package com.batiaev.aiml.core;

import com.batiaev.aiml.chat.ChatState;

import java.io.File;


/**
 * @author batiaev
 * Class representing the AIML bot
 * ---
 * @author Marco Piovesan
 * Predicates are passed to brain  29/08/16
 * ---
 * Copyright © 2015. Anton Batiaev. All Rights Reserved.
 * www.batiaev.com
 */
public class Bot {
    private GraphMaster brain = null;

    private BotInfo botInfo;

    public static final String PROPERTIES = "bot.properties";

    private String name = AIMLConst.default_bot_name;
    public String root_path = AIMLConst.root_path;
    public String bot_path = root_path + File.separator + "bots";
    public String bot_name_path = bot_path + File.separator  + name;
    public String substitutions_path = bot_name_path + File.separator + "substitutions";
    public String aiml_path = bot_name_path + File.separator + "aiml";
    public String system_path = bot_name_path + File.separator + "system";
    public String skills_path = bot_name_path + File.separator + "skills";
    public String sets_path = bot_name_path + File.separator + "sets";
    public String maps_path = bot_name_path + File.separator + "maps";

    public Bot() {
        botInfo = new BotInfo();
        brain = new GraphMaster(this);
        brain.wakeUp();
    }

    public BotInfo botInfo() {
        return botInfo;
    }

    public void setBotInfo(BotInfo botInfo) {
        this.botInfo = botInfo;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        reloadPaths();
    }

    public String getBrainStats() {
        return brain.getStat();
    }

    public void reload() {
        brain.wakeUp();
    }

    private void reloadPaths() {
        bot_name_path = bot_path + File.separator  + name;
        String prefix = bot_name_path + File.separator;
        substitutions_path = prefix + "substitutions";
        aiml_path = prefix + "aiml";
        system_path = prefix + "system";
        skills_path = prefix + "skills";
        sets_path = prefix + "sets";
        maps_path = prefix + "maps";
    }

    public String multisentenceRespond(String request, ChatState state) {
        String[] sentences = brain.sentenceSplit(request);
        String response = "";
        for (String sentence : sentences) response += " " + respond(sentence, state);
        return response.isEmpty() ? AIMLConst.error_bot_response : response;
    }

    public String respond(String request, ChatState state) {
        String pattern = brain.match(request, state.topic(), state.that());
        return brain.respond(pattern, state.topic(), state.that(), state.getPredicates());
    }
}
