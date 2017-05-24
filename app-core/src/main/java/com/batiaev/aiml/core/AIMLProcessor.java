package com.batiaev.aiml.core;

import com.batiaev.aiml.consts.AimlConst;
import com.batiaev.aiml.consts.AimlTag;
import com.batiaev.aiml.consts.WildCard;
import com.batiaev.aiml.entity.AimlCategory;
import com.batiaev.aiml.utils.AppUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.regex.Pattern;

import static com.batiaev.aiml.consts.AimlTag.*;

/**
 * The core AIML parser and interpreter.
 * Implements the AIML 2.0 specification as described in
 * AIML 2.0 Working Draft document
 * https://docs.google.com/document/d/1wNT25hJRyupcG51aO89UcQEiG-HkXRXusukADpFnDs4/pub
 * https://playground.pandorabots.com/en/tutorial/
 * http://blog.pandorabots.com/aiaas-aiml-2-0-support/
 * http://www.alicebot.org/documentation/aiml101.html
 * http://itc.ua/articles/kvest_tyuringa_7667/
 * http://www.eblong.com/zarf/markov/chan.c
 *
 * @author anton
 * @author Marco
 *         Implementation SET tag processing on 19/08/2016
 *         Topic managment improvement on 20/08/2016
 *         Parsing THINK tag
 *         Reimplemented isMatching method
 * @since 19/10/16
 */
public class AIMLProcessor {
    private final List<AimlCategory> categories;
    private final Map<String, Map<String, AimlCategory>> topics;
    private Map<String, String> predicates;

    public AIMLProcessor(List<AimlCategory> categories) {
        this.predicates = new HashMap<>();
        this.topics = convert(categories);
        this.categories = categories;
    }

    private Map<String, Map<String, AimlCategory>> convert(List<AimlCategory> categories) {
        Map<String, Map<String, AimlCategory>> topics = new HashMap<>();
        categories.forEach(category -> {
            Map<String, AimlCategory> topicCategories;
            if (topics.containsKey(category.getTopic())) {
                topicCategories = topics.get(category.getTopic());
            } else {
                topicCategories = new HashMap<>();
                topics.put(category.getTopic(), topicCategories);
            }
            topicCategories.put(category.getPattern(), category);
        });
        return topics;
    }

    public String match(final String input, String topic, String that) {
        String request = input.toUpperCase();
        Set<String> patterns = patterns(topic);
        for (String pattern : patterns) {
            if (isMatching(request, pattern))
                return pattern;
        }
        patterns = patterns(AimlConst.default_topic);
        for (String pattern : patterns) {
            if (isMatching(request, pattern))
                return pattern;
        }
        return WildCard.OneMore.get();
    }

    public String template(String pattern, String topic, String that, Map<String, String> predicates) {
        this.predicates = predicates;
        AimlCategory category = category(topic, pattern);
        if (category == null)
            category = category(AimlConst.default_topic, WildCard.OneMore.get());
        return category == null ? AimlConst.default_bot_response : getTemplateValue(category.getTemplate());
    }

    public int getTopicCount() {
        return topics.size();
    }

    public int getCategoriesCount() {
        return categories.size();
    }

    private boolean isMatching(String input, String pattern) {
        input = input.trim();
        String regex_pattern = pattern.trim();
        regex_pattern = regex_pattern.replace(WildCard.OneMorePriority.get(), ".+");
        regex_pattern = regex_pattern.replace(WildCard.OneMore.get(), ".+");
        regex_pattern = regex_pattern.replace(WildCard.ZeroMorePriority.get(), ".*");
        regex_pattern = regex_pattern.replace(WildCard.ZeroMore.get(), ".*");
        regex_pattern = regex_pattern.replace(" ", "\\s*");
        return Pattern.matches(regex_pattern, input);
    }

    private String getTemplateValue(Node node) {
        String result = "";
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i)
            result += recurseParse(childNodes.item(i));
        return result.isEmpty() ? AimlConst.default_bot_response : result;
    }

    private String recurseParse(Node node) {
        String nodeName = node.getNodeName();
        switch (nodeName) {
            case text:
                return textParse(node);
            case template:
                return getTemplateValue(node);
            case random:
                return randomParse(node);
            case srai:
                return sraiParse(node);
            case set:
                setParse(node);
                return "";
            case think:
                getTemplateValue(node);
                return "";
        }
        return "";
    }

    private String textParse(Node node) {
        return node.getNodeValue().replaceAll("(\r\n|\n\r|\r|\n)", "").replaceAll("  ", " ");
    }

    private void setParse(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        if (attributes.getLength() > 0) {
            Node node1 = attributes.getNamedItem("getName");
            if (node1 == null) return;
            String key = node1.getNodeValue();
            String value = getTemplateValue(node);
            predicates.put(key, value);
        }
    }

    private String sraiParse(Node node) {
        AimlCategory category = category(AimlConst.default_topic, AppUtils.node2String(node));
        return category != null ? getTemplateValue(category.getTemplate()) : AimlConst.error_bot_response;
    }

    private String randomParse(Node node) {
        List<String> values = new ArrayList<>();
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            if (childNodes.item(i).getNodeName().equals(AimlTag.li))
                values.add(AppUtils.node2String(childNodes.item(i)));
        }

        return AppUtils.getRandom(values);
    }

    private Set<String> patterns(String topic) {
        if (topics.containsKey(topic)) {
            return topics.get(topic).keySet();
        } else {
            topics.put(topic, new HashMap<>());
            return Collections.emptySet();
        }
    }

    private AimlCategory category(String topic, String pattern) {
        return topics.get(topic).get(pattern);
    }
}