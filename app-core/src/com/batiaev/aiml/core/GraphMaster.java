package com.batiaev.aiml.core;

import java.io.File;
import java.util.HashMap;

/**
 * The AIML Pattern matching algorithm and data structure.
 * Brain of bot.
 *
 */
public class GraphMaster {

    public Bot bot;

    public HashMap<String, AIMLSet> setMap = new HashMap<String, AIMLSet>();
    public HashMap<String, AIMLMap> mapMap = new HashMap<String, AIMLMap>();
    public HashMap<String, AIMLSubstitution> substitutionMap = new HashMap<String, AIMLSubstitution>();

    public GraphMaster() {}

    public GraphMaster(Bot bot) {
        this.bot = bot;
    }

    public void wakeUp() {
        loadSets();
        loadMaps();
        loadAimlFiles();
        loadSubstitutions();
        loadSystemConfigs();
    }
    void loadSets() {
        File sets = new File(bot.sets_path);
        File[] files = sets.listFiles();
        for (File file : files != null ? files : new File[0])
            setMap.put(file.getName(), new AIMLSet(file.getAbsolutePath()));
    }

    void loadMaps() {
        File maps = new File(bot.maps_path);
        File[] files = maps.listFiles();
        for (File file : files != null ? files : new File[0])
            mapMap.put(file.getName(), new AIMLMap(file.getAbsolutePath()));
    }

    void loadSubstitutions() {
        File substitutions = new File(bot.substitutions_path);
        File[] files = substitutions.listFiles();
        for (File file : files != null ? files : new File[0])
            substitutionMap.put(file.getName(), new AIMLSubstitution(file.getAbsolutePath()));
    }

    void loadAimlFiles() {
        AIMLProcessor processor = new AIMLProcessor();
        File aimls = new File(bot.aiml_path);
        File[] files = aimls.listFiles();
        for (File file : files != null ? files : new File[0]) processor.loadFile(file.getAbsolutePath());
    }

    void loadSystemConfigs() {
        File maps = new File(bot.system_path);
        File[] files = maps.listFiles();
        for (File file : files != null ? files : new File[0])
            System.out.println("Load system config \t" + file.getName());
    }
}
