package com.batiaev.aiml.bot;

import com.batiaev.aiml.chat.ChatState;
import com.batiaev.aiml.core.AIMLConst;
import com.batiaev.aiml.core.GraphMaster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;


/**
 * Class representing the AIML bot
 *
 * @author anbat
 * @author Marco Piovesan
 *         Predicates are passed to brain  29/08/16
 */
public class Bot {

    private static final Logger LOG = LogManager.getLogger(Bot.class);

    private static final String PROPERTIES = "bot.properties";

    private GraphMaster brain;
    private BotInfo botInfo;
    private String rootDir;
    private String name;

    public Bot(String name, String rootDir) {
        this.name = name;
        this.rootDir = rootDir;
        botInfo = new BotInfo();
        brain = new GraphMaster(this);
    }

    private String getRootDir() {
        return rootDir;
    }

    public String getAimlFolder() {
        return getRootDir() + "aiml";
    }

    public String getSubstitutionsFolder() {
        return getRootDir() + "substitutions";
    }

    public String getSetsFolder() {
        return getRootDir() + "sets";
    }

    public String getMapsFolder() {
        return getRootDir() + "maps";
    }

    public String getSkillsFolder() {
        return getRootDir() + "skills";
    }

    public String getSystemFolder() {
        return getRootDir() + "system";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrainStats() {
        return brain.getStat();
    }

    public void reload() {
        wakeUp();
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
        if (!validate(getRootDir()) || !validate(getAimlFolder()))
            return false;
        loadSystemConfigs();
        return brain.wakeUp();
    }


    private boolean loadSystemConfigs() {
        if (!validate(getSystemFolder()))
            return false;
        File system = new File(getSystemFolder());
        File[] files = system.listFiles();
        if (files == null || files.length < 1)
            return false;
        for (File file : files) {
            LOG.debug("Load system config: " + file.getName());
            if (file.getName().equals(Bot.PROPERTIES)) {
                loadBotInfo(file.getAbsolutePath());
            }
        }
        LOG.info("Loaded " + files.length + " system config files.");
        return true;
    }

    private void loadBotInfo(String path) {
        Properties prop = new Properties();
        try (FileInputStream in = new FileInputStream(path)) {
            prop.load(in);
            botInfo.setFirstname(prop.getProperty("firstname"));
            botInfo.setLastname(prop.getProperty("lastname"));
            botInfo.setLanguage(prop.getProperty("language"));
            botInfo.setEmail(prop.getProperty("email"));
            botInfo.setGender(prop.getProperty("gender"));
            botInfo.setVersion(prop.getProperty("version"));
            botInfo.setBirthplace(prop.getProperty("birthplace"));
            botInfo.setJob(prop.getProperty("job"));
            botInfo.setSpecies(prop.getProperty("species"));
            botInfo.setBirthday(prop.getProperty("birthday"));
            botInfo.setBirthdate(prop.getProperty("birthdate"));
            botInfo.setSign(prop.getProperty("sign"));
            botInfo.setReligion(prop.getProperty("religion"));
            botInfo.setBotmaster(prop.getProperty("botmaster"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
