package com.batiaev.aiml.core;

import com.batiaev.aiml.entity.AimlMap;
import com.batiaev.aiml.entity.AimlSet;
import com.batiaev.aiml.entity.AimlSubstitution;
import com.batiaev.aiml.entity.Category;

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
    private Map<String, AimlSet> sets;
    private Map<String, AimlMap> maps;
    private Map<String, AimlSubstitution> substitutions;
    private AIMLProcessor processor;

    public GraphMaster(List<Category> categories, Map<String, AimlSet> sets, Map<String, AimlMap> maps,
                       Map<String, AimlSubstitution> substitutions) {
        this.sets = sets;
        this.maps = maps;
        this.substitutions = substitutions;
        this.processor = new AIMLProcessor(categories);
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
        String[] result = line.split("[.!?]");
        for (int i = 0; i < result.length; i++)
            result[i] = result[i].trim();
        return result;
    }

    public String respond(String pattern, String topic, String that, Map<String, String> predicates) {
        return processor.template(pattern, topic, that, predicates);
    }

    public String match(String request, String topic, String that) {
        return processor.match(request, topic, that);
    }
}
