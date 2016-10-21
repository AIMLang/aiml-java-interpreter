package com.batiaev.aiml.core;

import com.batiaev.aiml.entity.AIMLMap;
import com.batiaev.aiml.entity.AIMLSet;
import com.batiaev.aiml.entity.AIMLSubstitution;
import com.batiaev.aiml.entity.CategoryList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * The AIML Pattern matching algorithm and data structure.
 * Brain of aimlFolder.
 *
 * @author anton
 * @author Marco
 *         Predicates are passed to AIMLProcessor
 */
public class GraphMaster {

    private static final Logger LOG = LoggerFactory.getLogger(GraphMaster.class);

    private Map<String, AIMLSet> sets;
    private Map<String, AIMLMap> maps;
    private Map<String, AIMLSubstitution> substitutions;
    private AIMLProcessor processor;

    public GraphMaster(CategoryList categories, Map<String, AIMLSet> sets, Map<String, AIMLMap> maps,
                       Map<String, AIMLSubstitution> substitutions) {
        this.sets = sets;
        this.maps = maps;
        this.substitutions = substitutions;
        this.processor = new AIMLProcessor(categories);
    }

    public String getStat() {
        int topicCount = processor.getTopicCount();
        int categoriesCount = processor.getCategoriesCount();
        return "Brain contain "
                + topicCount + " topics, "
                + categoriesCount + " categories, "
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
        line = line.replace("。", ".");
        line = line.replace("？", "?");
        line = line.replace("！", "!");
        line = line.replaceAll("(\r\n|\n\r|\r|\n)", " ");
        String[] result = line.split("[\\.!\\?]");
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
