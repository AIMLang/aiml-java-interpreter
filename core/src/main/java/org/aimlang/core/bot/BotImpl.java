package org.aimlang.core.bot;

import lombok.extern.slf4j.Slf4j;
import org.aimlang.core.channels.ChannelType;
import org.aimlang.core.channels.Channel;
import org.aimlang.core.chat.Chat;
import org.aimlang.core.chat.ChatContext;
import org.aimlang.core.chat.ChatContextStorage;
import org.aimlang.core.consts.AimlConst;
import org.aimlang.core.core.GraphMaster;
import org.aimlang.core.entity.AimlCategory;
import org.aimlang.core.entity.AimlMap;
import org.aimlang.core.entity.AimlSet;
import org.aimlang.core.entity.AimlSubstitution;
import org.aimlang.core.loaders.AimlLoader;
import org.aimlang.core.loaders.MapLoader;
import org.aimlang.core.loaders.SetLoader;
import org.aimlang.core.loaders.SubstitutionLoader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Class representing the AIML bot
 *
 * @author batiaev
 */
@Slf4j
public class BotImpl implements Bot {

    private final GraphMaster brain;
    private final String rootDir;
    private final ChatContextStorage chatContextStorage;
    private ChatContext chatContext;
    private String name;

    public BotImpl(String name, String rootDir, ChatContextStorage chatContextStorage) {
        this.name = name;
        this.rootDir = rootDir;
        this.chatContextStorage = chatContextStorage;
        this.chatContext = this.chatContextStorage.getContext(name, ChannelType.CONSOLE);
        var aimlSets = loadSets();
        var aimlMaps = loadMaps();
        var aimlCategories = loadAiml();
        brain = new GraphMaster(preprocess(aimlCategories, aimlSets), aimlSets, aimlMaps, loadSubstitutions(),
                new BotConfiguration(rootDir));
    }

    private List<AimlCategory> preprocess(List<AimlCategory> categories, Map<String, AimlSet> aimlSets) {
        var processed = new ArrayList<AimlCategory>();
        for (AimlCategory aimlCategory : categories) {
            var pattern = aimlCategory.getPattern();
            var regexp = Pattern.compile("<set>(.+?)</set>");
            var matcher = regexp.matcher(pattern);
            if (matcher.find()) {
                var setName = matcher.group(1);
                var setValues = aimlSets.get(setName + ".txt");
                if (setValues != null) {
                    for (String s : setValues) {
                        var first = matcher.replaceFirst(s);
                        var cloned = aimlCategory.clone();
                        cloned.setPattern(first);
                        processed.add(cloned);
                    }
                }
            } else {
                processed.add(aimlCategory);
            }
        }
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

    @Override
    public Chat buildChat(Channel channel) {
        setChatContext(getChatContextStorage().getContext(null, channel.getType()));
        return new Chat(this, channel);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrainStats() {
        return brain.getStat();
    }

    public String multisentenceRespond(String request, ChatContext state) {
        var sentences = brain.sentenceSplit(request);
        var response = new StringBuilder();
        for (String sentence : sentences)
            response.append(" ").append(respond(sentence, state));
        return (response.length() == 0)
                ? AimlConst.error_bot_response
                : response.toString().trim();
    }

    public String respond(final String request, ChatContext state) {
        var stars = new ArrayList<String>();
        var pattern = brain.match(request, state.topic(), state.that(), stars);
        return brain.respond(stars, pattern, state.topic(), state.that(), state.getPredicates());
    }

    private List<AimlCategory> loadAiml() {
        var loader = new AimlLoader();
        return loader.loadFiles(getAimlFolder());
    }

    private Map<String, AimlSet> loadSets() {
        var sets = new File(getSetsFolder());
        if (!sets.exists()) {
            log.warn("Sets not found!");
            return Collections.emptyMap();
        }
        var files = sets.listFiles();
        if (files == null || files.length == 0)
            return Collections.emptyMap();

        var loader = new SetLoader();

        var data = loader.loadAll(files);
        int count = data.keySet().stream().mapToInt(s -> data.get(s).size()).sum();

        log.info("Loaded {} set records from {} files.", count, files.length);
        return data;
    }

    private Map<String, AimlMap> loadMaps() {
        var maps = new File(getMapsFolder());
        if (!maps.exists()) {
            log.warn("Maps not found!");
            return Collections.emptyMap();
        }
        var files = maps.listFiles();
        if (files == null || files.length == 0) return Collections.emptyMap();

        var loader = new MapLoader<>();

        var data = loader.loadAll(files);
        int count = data.keySet()
                .stream()
                .mapToInt(s -> data.get(s).size())
                .sum();

        log.info("Loaded " + count + " map records from " + files.length + " files.");
        return data;
    }

    private Map<String, AimlSubstitution> loadSubstitutions() {
        var maps = new File(getSubstitutionsFolder());
        if (!maps.exists()) {
            log.warn("Maps not found!");
            return Collections.emptyMap();
        }
        var files = maps.listFiles();
        if (files == null || files.length == 0)
            return Collections.emptyMap();

        var loader = new SubstitutionLoader();

        var data = loader.loadAll(files);
        int count = data.keySet().stream().mapToInt(s -> data.get(s).size()).sum();

        log.info("Loaded " + count + " substitutions from " + files.length + " files.");
        return data;
    }

    private boolean validate(String folder) {
        if (folder == null || folder.isEmpty())
            return false;
        var botsFolder = Paths.get(folder);
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
