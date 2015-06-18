package com.batiaev.aiml.core;

import java.io.File;
import java.util.HashMap;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

/**
 * The AIML Pattern matching algorithm and data structure.
 * Brain of bot.
 *
 */
public class GraphMaster {

//    private static final Logger LOG = LogManager.getLogger(GraphMaster.class);
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
        int count = 0;
        for (File file : files != null ? files : new File[0]) {
            setMap.put(file.getName(), new AIMLSet(file.getAbsolutePath()));
            count += setMap.get(file.getName()).size();
        }
        if (files != null) System.out.println("Loaded " + count + " set records from " + files.length + " files.");
    }

    void loadMaps() {
        File maps = new File(bot.maps_path);
        File[] files = maps.listFiles();
        int count = 0;
        for (File file : files != null ? files : new File[0]) {
            mapMap.put(file.getName(), new AIMLMap(file.getAbsolutePath()));
            count += mapMap.get(file.getName()).size();
        }
        if (files != null) System.out.println("Loaded " + count + " map records from " + files.length + " files.");
    }

    void loadSubstitutions() {
        File substitutions = new File(bot.substitutions_path);
        File[] files = substitutions.listFiles();
        int count = 0;
        for (File file : files != null ? files : new File[0]) {
            substitutionMap.put(file.getName(), new AIMLSubstitution(file.getAbsolutePath()));
            count += substitutionMap.get(file.getName()).size();
        }
        if (files != null) System.out.println("Loaded " + count + " substitutions from " + files.length + " files.");
    }

    void loadAimlFiles() {
        AIMLProcessor processor = new AIMLProcessor();
        File aimls = new File(bot.aiml_path);
        processor.loadFiles(aimls);
    }

    void loadSystemConfigs() {
        File maps = new File(bot.system_path);
        File[] files = maps.listFiles();
        for (File file : files != null ? files : new File[0])
            System.out.println("Load system config \t" + file.getName());
        if (files != null) System.out.println("Loaded " + files.length + " system config files.");
    }
}
