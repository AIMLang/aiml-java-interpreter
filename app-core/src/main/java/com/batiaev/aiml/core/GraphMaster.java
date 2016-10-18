package com.batiaev.aiml.core;

import com.batiaev.aiml.bot.Bot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;

/**
 * The AIML Pattern matching algorithm and data structure.
 * Brain of bot.
 *
 * @author anbat
 * @author Marco
 *         Predicates are passed to AIMLProcessor
 */
public class GraphMaster {

    private static final Logger LOG = LogManager.getLogger(GraphMaster.class);

    private Bot bot;
    private HashMap<String, AIMLSet> setMap;
    private HashMap<String, AIMLMap> mapMap;
    private HashMap<String, AIMLSubstitution> substitutionMap;
    private AIMLProcessor processor;

    public GraphMaster(Bot bot) {
        this.setMap = new HashMap<>();
        this.mapMap = new HashMap<>();
        this.substitutionMap = new HashMap<>();
        AIMLLoader loader = new AIMLLoader();
        this.processor = new AIMLProcessor(loader);
        this.bot = bot;
    }

    public String getStat() {
        return processor.getStat();
    }

    public boolean wakeUp() {
        loadSets();
        loadMaps();
        loadAimlFiles();
        loadSubstitutions();
        return true;
    }

    private void loadSets() {
        setMap.clear();
        File sets = new File(bot.getSetsFolder());
        if (!sets.exists()) {
            LOG.warn("Sets not found!");
            return;
        }
        File[] files = sets.listFiles();
        if (files == null || files.length == 0) return;
        int count = 0;
        for (File file : files) {
            final AIMLSet set = new AIMLSet(file.getAbsolutePath());
            setMap.put(file.getName(), set);
            count += set.size();
        }
        LOG.info("Loaded " + count + " set records from " + files.length + " files.");
    }

    private void loadMaps() {
        mapMap.clear();
        File maps = new File(bot.getMapsFolder());
        if (!maps.exists()) {
            LOG.warn("Maps not found!");
            return;
        }
        File[] files = maps.listFiles();
        if (files == null || files.length == 0) return;
        int count = 0;
        for (File file : files) {
            final AIMLMap map = new AIMLMap(file.getAbsolutePath());
            mapMap.put(file.getName(), map);
            count += map.size();
        }
        LOG.info("Loaded " + count + " map records from " + files.length + " files.");
    }

    private void loadSubstitutions() {
        substitutionMap.clear();
        File substitutions = new File(bot.getSubstitutionsFolder());
        File[] files = substitutions.listFiles();
        if (files == null || files.length == 0) return;
        int count = 0;
        for (File file : files) {
            substitutionMap.put(file.getName(), new AIMLSubstitution(file.getAbsolutePath()));
            count += substitutionMap.get(file.getName()).size();
        }
        LOG.info("Loaded " + count + " substitutions from " + files.length + " files.");
    }

    private void loadAimlFiles() {
        processor.loadCategories(bot.getAimlFolder());
        LOG.info(getStat());
    }

    /**
     * Split an input into an array of sentences based on sentence-splitting characters.
     *
     * @param line input text
     * @return array of sentences
     */
    public String[] sentenceSplit(String line) {
        line = line.replace("。", ".");
        line = line.replace("？", "?");
        line = line.replace("！", "!");
        line = line.replaceAll("(\r\n|\n\r|\r|\n)", " ");
        String[] result = line.split("[\\.!\\?]");
        for (int i = 0; i < result.length; i++) result[i] = result[i].trim();
        return result;
    }

    public String respond(String pattern, String topic, String that, HashMap<String, String> predicates) {
        return processor.template(pattern, topic, that, predicates);
    }

    public String match(String request, String topic, String that) {
        return processor.match(request, topic, that);
    }
}
