package org.aimlang.core.core;

import org.aimlang.core.bot.BotInfo;
import org.aimlang.core.entity.AimlCategory;
import org.aimlang.core.entity.AimlMap;
import org.aimlang.core.entity.AimlSet;
import org.aimlang.core.entity.AimlSubstitution;

import java.util.List;
import java.util.Map;

/**
 * The AIML Pattern matching algorithm and data structure.
 * Brain of bot.
 *
 * @author anton
 * @author Marco
 *         Predicates are passed to AIMLProcessor
 */
public class GraphMaster {
    private final Map<String, AimlSet> sets;
    private final Map<String, AimlMap> maps;
    private final Map<String, AimlSubstitution> substitutions;
    private final AIMLProcessor processor;

    public GraphMaster(List<AimlCategory> categories, Map<String, AimlSet> sets, Map<String, AimlMap> maps,
                       Map<String, AimlSubstitution> substitutions, BotInfo botInfo) {
        this.sets = sets;
        this.maps = maps;
        this.substitutions = substitutions;
        this.processor = new AIMLProcessor(categories, botInfo);
    }

    public String getStat() {
        return "Brain contain "
                + processor.getTopicCount() + " topics, "
                + processor.getCategoriesCount() + " categories, "
                + sets.size() + " sets, "
                + maps.size() + " maps, "
                + substitutions.size() + " substitutions.";
    }

    /**
     * Split an input into an array of sentences based on sentence-splitting characters.
     *
     * @param line input text
     * @return array of sentences
     */
    public String[] sentenceSplit(String line) {
        line = line.replace("。", ".")
                .replace("？", "?")
                .replace("！", "!")
                .replaceAll("(\r\n|\n\r|\r|\n)", " ");
        var result = line.split("[.!?]");
        for (int i = 0; i < result.length; i++)
            result[i] = result[i].trim();
        return result;
    }

    public String respond(List<String> stars, String pattern, String topic, String that, Map<String, String> predicates) {
        return processor.template(stars, pattern, topic, that, predicates);
    }

    public String match(String request, String topic, String that, List<String> stars) {
        return processor.match(request, topic, that, stars);
    }
}
