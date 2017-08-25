package com.batiaev.aiml.bot;

import com.batiaev.aiml.chat.ChatContext;
import com.batiaev.aiml.chat.ChatContextStorage;
import com.batiaev.aiml.consts.AimlConst;
import com.batiaev.aiml.core.GraphMaster;
import com.batiaev.aiml.entity.AimlCategory;
import com.batiaev.aiml.entity.AimlMap;
import com.batiaev.aiml.entity.AimlSet;
import com.batiaev.aiml.entity.AimlSubstitution;
import com.batiaev.aiml.loaders.*;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.batiaev.aiml.channels.ChannelType.CONSOLE;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Class representing the AIML bot
 *
 * @author batiaev
 */
public class BotImpl implements Bot {
    private static final Logger log = getLogger(BotImpl.class);

    private GraphMaster brain;
    private BotInfo botInfo;
    private String rootDir;
    private String name;
    private ChatContextStorage chatContextStorage;
    private ChatContext chatContext;

    public BotImpl(String name, String rootDir, ChatContextStorage chatContextStorage) {
        this.name = name;
        this.rootDir = rootDir;
        this.botInfo = new BotConfiguration(rootDir);
        this.chatContextStorage = chatContextStorage;
        this.chatContext = this.chatContextStorage.getContext(name, CONSOLE);
        Map<String, AimlSet> aimlSets = loadSets();
        Map<String, AimlMap> aimlMaps = loadMaps();
        List<AimlCategory> aimlCategories = loadAiml();
        brain = new GraphMaster(preprocess(aimlCategories, aimlSets), aimlSets, aimlMaps, loadSubstitutions(), botInfo);
    }

    private List<AimlCategory> preprocess(List<AimlCategory> categories, Map<String, AimlSet> aimlSets) {
        List<AimlCategory> processed = new ArrayList<>();
        categories.forEach(aimlCategory -> {
            String pattern = aimlCategory.getPattern();
            final Pattern regexp = Pattern.compile("<set>(.+?)</set>");
            final Matcher matcher = regexp.matcher(pattern);
            if (matcher.find()) {
                String setName = matcher.group(1);
                AimlSet setValues = aimlSets.get(setName + ".txt");
                if (setValues != null) {
                    setValues.forEach(s -> {
                        String first = matcher.replaceFirst(s);
                        AimlCategory cloned = aimlCategory.clone();
                        cloned.setPattern(first);
                        processed.add(cloned);
                    });
                }
            } else {
                processed.add(aimlCategory);
            }
        });
        return processed;
    }

    @Override
    public ChatContextStorage getChatContextStorage() {
        return chatContextStorage;
    }

    @Override
    public void setChatContext(ChatContext chatContext) {
        this.chatContext = chatContext;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean wakeUp() {
        return validate(getRootDir()) && validate(getAimlFolder());
    }

    @Override
    public String getRespond(String phrase) {
        return multisentenceRespond(phrase, chatContext);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrainStats() {
        return brain.getStat();
    }

    public String multisentenceRespond(String request, ChatContext state) {
        String[] sentences = brain.sentenceSplit(request);
        StringBuilder response = new StringBuilder();
        for (String sentence : sentences)
            response.append(" ").append(respond(sentence, state));
        return (response.length() == 0) ? AimlConst.error_bot_response : response.toString().trim();
    }

    public String respond(final String request, ChatContext state) {
        List<String> stars = new ArrayList<>();
        String pattern = brain.match(request, state.topic(), state.that(), stars);
        return brain.respond(stars, pattern, state.topic(), state.that(), state.getPredicates());
    }

    private List<AimlCategory> loadAiml() {
        AimlLoader loader = new AimlLoader();
        return loader.loadFiles(getAimlFolder());
    }

    private Map<String, AimlSet> loadSets() {

        File sets = new File(getSetsFolder());
        if (!sets.exists()) {
            log.warn("Sets not found!");
            return Collections.emptyMap();
        }
        File[] files = sets.listFiles();
        if (files == null || files.length == 0)
            return Collections.emptyMap();

        FileLoader<AimlSet> loader = new SetLoader();

        final Map<String, AimlSet> data = loader.loadAll(files);
        int count = data.keySet().stream().mapToInt(s -> data.get(s).size()).sum();

        log.info("Loaded {} set records from {} files.", count, files.length);
        return data;
    }

    private Map<String, AimlMap> loadMaps() {

        File maps = new File(getMapsFolder());
        if (!maps.exists()) {
            log.warn("Maps not found!");
            return Collections.emptyMap();
        }
        File[] files = maps.listFiles();
        if (files == null || files.length == 0) return Collections.emptyMap();


        FileLoader<AimlMap> loader = new MapLoader<>();

        final Map<String, AimlMap> data = loader.loadAll(files);
        int count = data.keySet().stream().mapToInt(s -> data.get(s).size()).sum();

        log.info("Loaded " + count + " map records from " + files.length + " files.");
        return data;
    }

    private Map<String, AimlSubstitution> loadSubstitutions() {

        File maps = new File(getSubstitutionsFolder());
        if (!maps.exists()) {
            log.warn("Maps not found!");
            return Collections.emptyMap();
        }
        File[] files = maps.listFiles();
        if (files == null || files.length == 0)
            return Collections.emptyMap();


        FileLoader<AimlSubstitution> loader = new SubstitutionLoader();

        final Map<String, AimlSubstitution> data = loader.loadAll(files);
        int count = data.keySet().stream().mapToInt(s -> data.get(s).size()).sum();

        log.info("Loaded " + count + " substitutions from " + files.length + " files.");
        return data;
    }

    private boolean validate(String folder) {
        if (folder == null || folder.isEmpty())
            return false;
        Path botsFolder = Paths.get(folder);
        if (Files.notExists(botsFolder)) {
            log.warn("BotImpl folder " + folder + " not found!");
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
