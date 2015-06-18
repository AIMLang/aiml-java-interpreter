package com.batiaev.aiml.core;

import java.io.File;
import java.util.HashMap;


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
}
