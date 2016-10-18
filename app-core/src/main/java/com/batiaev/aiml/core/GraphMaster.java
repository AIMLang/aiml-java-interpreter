package com.batiaev.aiml.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

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

    private boolean validate() {
        File sets = new File(bot.bot_path);
        if (!sets.exists()) {
            LOG.warn("Bot data not found in " + bot.bot_path + " folder!");
            return false;
        }
        return true;
    }

    public boolean wakeUp() {
        if (!validate())
            return false;
        loadSets();
        loadMaps();
        loadAimlFiles();
        loadSubstitutions();
        loadSystemConfigs();
        return true;
    }

    private void loadSets() {
        setMap = new HashMap<>();
        File sets = new File(bot.sets_path);
        if (!sets.exists()) {
            LOG.warn("Sets not found!");
            return;
        }
        File[] files = sets.listFiles();
        if (files == null || files.length == 0) return;
        int count = 0;
        for (File file : files) {
            setMap.put(file.getName(), new AIMLSet(file.getAbsolutePath()));
            count += setMap.get(file.getName()).size();
        }
        LOG.info("Loaded " + count + " set records from " + files.length + " files.");
    }

    private void loadMaps() {
        mapMap = new HashMap<>();
        File maps = new File(bot.maps_path);
        if (!maps.exists()) {
            LOG.warn("Maps not found!");
            return;
        }
        File[] files = maps.listFiles();
        if (files == null || files.length == 0) return;
        int count = 0;
        for (File file : files) {
            mapMap.put(file.getName(), new AIMLMap(file.getAbsolutePath()));
            count += mapMap.get(file.getName()).size();
        }
        LOG.info("Loaded " + count + " map records from " + files.length + " files.");
    }

    private void loadSubstitutions() {
        substitutionMap = new HashMap<>();
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

    private void loadAimlFiles() {
        processor = new AIMLProcessor();
        AIMLLoader loader = new AIMLLoader();
        CategoryList categories = loader.loadFiles(bot.aiml_path);
        processor.setCategoryList(categories);
        LOG.info(getStat());
    }

    private void loadSystemConfigs() {
        File maps = new File(bot.system_path);
        File[] files = maps.listFiles();
        for (File file : files != null ? files : new File[0]) {
            LOG.debug("Load system config: " + file.getName());
            if (file.getName().equals(Bot.PROPERTIES)) {
                loadBotInfo(file.getAbsolutePath());
            }
        }
        if (files != null) LOG.info("Loaded " + files.length + " system config files.");
    }

    private void loadBotInfo(String path) {
        Properties prop = new Properties();
        try {
            FileInputStream in = new FileInputStream(path);
            prop.load(in);
            BotInfo info = new BotInfo();
            info.setFirstname(prop.getProperty("firstname"));
            info.setLastname(prop.getProperty("lastname"));
            info.setLanguage(prop.getProperty("language"));
            info.setEmail(prop.getProperty("email"));
            info.setGender(prop.getProperty("gender"));
            info.setVersion(prop.getProperty("version"));
            info.setBirthplace(prop.getProperty("birthplace"));
            info.setJob(prop.getProperty("job"));
            info.setSpecies(prop.getProperty("species"));
            info.setBirthday(prop.getProperty("birthday"));
            info.setBirthdate(prop.getProperty("birthdate"));
            info.setSign(prop.getProperty("sign"));
            info.setReligion(prop.getProperty("religion"));
            info.setBotmaster(prop.getProperty("botmaster"));
            bot.setBotInfo(info);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
