package com.batiaev.aiml.bot;

import com.batiaev.aiml.chat.ChatState;
import com.batiaev.aiml.consts.AIMLConst;
import com.batiaev.aiml.core.GraphMaster;
import com.batiaev.aiml.core.Named;
import com.batiaev.aiml.entity.AIMLMap;
import com.batiaev.aiml.entity.AIMLSet;
import com.batiaev.aiml.entity.AIMLSubstitution;
import com.batiaev.aiml.entity.CategoryList;
import com.batiaev.aiml.loaders.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

/**
 * Class representing the AIML bot
 *
 * @author anbat
 */
public class Bot implements Named {

    private static final Logger LOG = LoggerFactory.getLogger(Bot.class);

    private GraphMaster brain;
    private BotInfo botInfo;
    private String rootDir;
    private String name;

    public Bot(String name, String rootDir) {
        this.name = name;
        this.rootDir = rootDir;
        this.botInfo = new BotConfiguration(rootDir);
        brain = new GraphMaster(loadAiml(), loadSets(), loadMaps(), loadSubstitutions());
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrainStats() {
        return brain.getStat();
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

    public boolean wakeUp() {
        return validate(getRootDir()) && validate(getAimlFolder());
    }

    private CategoryList loadAiml() {
        AIMLLoader loader = new AIMLLoader();
        return loader.loadFiles(getAimlFolder());
    }

    private Map<String, AIMLSet> loadSets() {

        File sets = new File(getSetsFolder());
        if (!sets.exists()) {
            LOG.warn("Sets not found!");
            return Collections.emptyMap();
        }
        File[] files = sets.listFiles();
        if (files == null || files.length == 0)
            return Collections.emptyMap();

        Loader<AIMLSet> loader = new SetLoader();

        final Map<String, AIMLSet> data = loader.loadAll(files);
        int count = data.keySet().stream().mapToInt(s -> data.get(s).size()).sum();

        LOG.info("Loaded {} set records from {} files.", count, files.length);
        return data;
    }

    private Map<String, AIMLMap> loadMaps() {

        File maps = new File(getMapsFolder());
        if (!maps.exists()) {
            LOG.warn("Maps not found!");
            return Collections.emptyMap();
        }
        File[] files = maps.listFiles();
        if (files == null || files.length == 0) return Collections.emptyMap();


        Loader<AIMLMap> loader = new MapLoader<>();

        final Map<String, AIMLMap> data = loader.loadAll(files);
        int count = data.keySet().stream().mapToInt(s -> data.get(s).size()).sum();

        LOG.info("Loaded " + count + " map records from " + files.length + " files.");
        return data;
    }

    private Map<String, AIMLSubstitution> loadSubstitutions() {

        File maps = new File(getSubstitutionsFolder());
        if (!maps.exists()) {
            LOG.warn("Maps not found!");
            return Collections.emptyMap();
        }
        File[] files = maps.listFiles();
        if (files == null || files.length == 0)
            return Collections.emptyMap();


        Loader<AIMLSubstitution> loader = new SubstitutionLoader();

        final Map<String, AIMLSubstitution> data = loader.loadAll(files);
        int count = data.keySet().stream().mapToInt(s -> data.get(s).size()).sum();

        LOG.info("Loaded " + count + " substitutions from " + files.length + " files.");
        return data;
    }

    private boolean validate(String folder) {
        if (folder == null || folder.isEmpty())
            return false;
        Path botsFolder = Paths.get(folder);
        if (Files.notExists(botsFolder)) {
            LOG.warn("Bot folder " + folder + " not found!");
            return false;
        }
        return true;
    }

    private String getRootDir() {
        return rootDir;
    }

    private String getAimlFolder() {
        return getRootDir() + "aiml";
    }

    private String getSubstitutionsFolder() {
        return getRootDir() + "substitutions";
    }

    private String getSetsFolder() {
        return getRootDir() + "sets";
    }

    private String getMapsFolder() {
        return getRootDir() + "maps";
    }

    private String getSkillsFolder() {
        return getRootDir() + "skills";
    }
}
