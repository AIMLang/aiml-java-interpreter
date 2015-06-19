package com.batiaev.aiml.core;

import com.batiaev.aiml.chat.ChatState;

import java.io.File;


/**
 * Class representing the AIML bot
 */
public class Bot {
    private GraphMaster brain = null;

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
        brain = new GraphMaster(this);
        brain.wakeUp();
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
        substitutions_path = bot_name_path + File.separator + "substitutions";
        aiml_path = bot_name_path + File.separator + "aiml";
        system_path = bot_name_path + File.separator + "system";
        skills_path = bot_name_path + File.separator + "skills";
        sets_path = bot_name_path + File.separator + "sets";
        maps_path = bot_name_path + File.separator + "maps";
    }

    public String multisentenceRespond(String request, ChatState state) {
        String[] sentences = brain.sentenceSplit(request);
        String response = "";
        for (String sentence : sentences) response += " " + respond(sentence, state);
        return response.isEmpty() ? AIMLConst.error_bot_response : response;
    }

    public String respond(String request, ChatState state) {
        return brain.respond(request, state.topic(), state.that(), state.request());
    }
}
