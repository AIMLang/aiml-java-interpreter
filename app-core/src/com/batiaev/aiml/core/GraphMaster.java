package com.batiaev.aiml.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;

/**
 * The AIML Pattern matching algorithm and data structure.
 * Brain of bot.
 *
 */
public class GraphMaster {

    private static final Logger LOG = LogManager.getLogger(GraphMaster.class);

    public Bot bot = null;
    public HashMap<String, AIMLSet> setMap = null;
    public HashMap<String, AIMLMap> mapMap = null;
    public HashMap<String, AIMLSubstitution> substitutionMap = null;
    private AIMLProcessor processor = null;

    public GraphMaster(Bot bot) {
        this.bot = bot;
    }

    public String getStat() {
        return processor.getStat();
    }

    public void wakeUp() {
        loadSets();
        loadMaps();
        loadAimlFiles();
        loadSubstitutions();
        loadSystemConfigs();
    }
    void loadSets() {
        setMap = new HashMap<String, AIMLSet>();
        File sets = new File(bot.sets_path);
        File[] files = sets.listFiles();
        if (files == null || files.length == 0) return;
        int count = 0;
        for (File file : files) {
            setMap.put(file.getName(), new AIMLSet(file.getAbsolutePath()));
            count += setMap.get(file.getName()).size();
        }
        LOG.info("Loaded " + count + " set records from " + files.length + " files.");
    }

    void loadMaps() {
        mapMap = new HashMap<String, AIMLMap>();
        File maps = new File(bot.maps_path);
        File[] files = maps.listFiles();
        if (files == null || files.length == 0) return;
        int count = 0;
        for (File file : files) {
            mapMap.put(file.getName(), new AIMLMap(file.getAbsolutePath()));
            count += mapMap.get(file.getName()).size();
        }
        LOG.info("Loaded " + count + " map records from " + files.length + " files.");
    }

    void loadSubstitutions() {
        substitutionMap = new HashMap<String, AIMLSubstitution>();
        File substitutions = new File(bot.substitutions_path);
        File[] files = substitutions.listFiles();
        if (files == null || files.length == 0) return;
        int count = 0;
        for (File file : files) {
            substitutionMap.put(file.getName(), new AIMLSubstitution(file.getAbsolutePath()));
            count += substitutionMap.get(file.getName()).size();
        }
        LOG.info("Loaded " + count + " substitutions from " + files.length + " files.");
    }

    void loadAimlFiles() {
        processor = new AIMLProcessor();
        File aimls = new File(bot.aiml_path);
        processor.loadFiles(aimls);
    }

    void loadSystemConfigs() {
        File maps = new File(bot.system_path);
        File[] files = maps.listFiles();
        for (File file : files != null ? files : new File[0])
            LOG.debug("Load system config \t" + file.getName());
        if (files != null) LOG.info("Loaded " + files.length + " system config files.");
    }

    /**
     * Split an input into an array of sentences based on sentence-splitting characters.
     *
     * @param line   input text
     * @return       array of sentences
     */
    public String[] sentenceSplit(String line) {
        line = line.replace("。",".");
        line = line.replace("？","?");
        line = line.replace("！","!");
        line = line.replaceAll("(\r\n|\n\r|\r|\n)", " ");
        String[] result = line.split("[\\.!\\?]");
        for (int i = 0; i < result.length; i++) result[i] = result[i].trim();
        return result;
    }

    public String respond(String input, String topic, String that, String respond) {
        return processor.match(input);
    }
}
