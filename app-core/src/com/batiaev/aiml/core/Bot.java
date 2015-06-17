package com.batiaev.aiml.core;

import java.io.File;
import java.util.HashMap;


/**
 * Class representing the AIML bot
 */
public class Bot {
    public final GraphMaster brain = null;

    public String name = AIMLConst.default_bot_name;
    public String voice = "";

    public HashMap<String, AIMLSet> setMap = new HashMap<String, AIMLSet>();
    public HashMap<String, AIMLMap> mapMap = new HashMap<String, AIMLMap>();
    public HashMap<String, AIMLSubstitution> substitutionMap = new HashMap<String, AIMLSubstitution>();
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
        loader();
    }

    void loader() {
        loadSets();
        loadMaps();
        loadAimlFiles();
        loadSubstitutions();
        loadSystemConfigs();
    }

    void loadSets() {
        File sets = new File(sets_path);
        File[] files = sets.listFiles();
        for (File file : files != null ? files : new File[0]) {
            AIMLSet set = new AIMLSet();
            set.loadFile(file.getAbsolutePath());
            setMap.put(file.getName(), set);
        }
    }

    void loadMaps() {
        File maps = new File(maps_path);
        File[] files = maps.listFiles();
        for (File file : files != null ? files : new File[0]) {
            AIMLMap map = new AIMLMap();
            map.loadFile(file.getAbsolutePath());
            mapMap.put(file.getName(), map);
        }
    }

    void loadSubstitutions() {
        File substitutions = new File(substitutions_path);
        File[] files = substitutions.listFiles();
        for (File file : files != null ? files : new File[0]) {
            AIMLSubstitution substitution = new AIMLSubstitution();
            substitution.loadFile(file.getAbsolutePath());
            substitutionMap.put(file.getName(), substitution);
        }
    }

    void loadAimlFiles() {
        File aimls = new File(aiml_path);
        File[] files = aimls.listFiles();
        for (File file : files != null ? files : new File[0]) {
            System.out.println("Load aiml \t\t" + file.getName());
        }
    }

    void loadSystemConfigs() {
        File maps = new File(system_path);
        File[] files = maps.listFiles();
        for (File file : files != null ? files : new File[0]) {
            System.out.println("Load system config \t" + file.getName());
        }
    }
}
