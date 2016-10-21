package com.batiaev.aiml.core;

import com.batiaev.aiml.consts.AIMLConst;
import com.batiaev.aiml.consts.AIMLTag;
import com.batiaev.aiml.consts.WildCard;
import com.batiaev.aiml.entity.Category;
import com.batiaev.aiml.utils.AppUtils;
import com.batiaev.aiml.utils.XmlHelper;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.regex.Pattern;

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
 */
public class AIMLProcessor {
    private final List<Category> categories;
    private Map<String, Map<String, Category>> topics;
    private Map<String, String> predicates;

    public AIMLProcessor(List<Category> categories) {
        this.predicates = new HashMap<>();
        this.topics = convert(categories);
        this.categories = categories;
    }

    private Map<String, Map<String, Category>> convert(List<Category> categories) {
        Map<String, Map<String, Category>> topics = new HashMap<>();
        categories.forEach(category -> {
            Map<String, Category> topicCategories;
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
        patterns = patterns(AIMLConst.default_topic);
        for (String pattern : patterns) {
            if (isMatching(request, pattern))
                return pattern;
        }
        return WildCard.OneMore.get();
    }

    public String template(String pattern, String topic, String that, Map<String, String> predicates) {
        this.predicates = predicates;
        Category category = category(topic, pattern);
        if (category == null)
            category = category(AIMLConst.default_topic, WildCard.OneMore.get());
        return category == null ? AIMLConst.default_bot_response : getTemplateValue(category.getTemplate());
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
        regex_pattern = regex_pattern.replace(WildCard.OneMore.get(), ".+");
        regex_pattern = regex_pattern.replace(WildCard.OneMorePriority.get(), ".+");
        regex_pattern = regex_pattern.replace(WildCard.ZeroMore.get(), ".*");
        regex_pattern = regex_pattern.replace(WildCard.ZeroMorePriority.get(), ".*");
        regex_pattern = regex_pattern.replace(" ", "\\s*");
        return Pattern.matches(regex_pattern, input);
    }

    private String getTemplateValue(Node node) {
        String result = "";
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i)
            result += recurseParse(childNodes.item(i));
        return result.isEmpty() ? AIMLConst.default_bot_response : result;
    }

    private String recurseParse(Node node) {
        String nodeName = node.getNodeName();
        switch (nodeName) {
            case AIMLTag.text:
                return textParse(node);
            case AIMLTag.template:
                return getTemplateValue(node);
            case AIMLTag.random:
                return randomParse(node);
            case AIMLTag.srai:
                return sraiParse(node);
            case AIMLTag.set:
                setParse(node);
                return "";
            case AIMLTag.think:
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
            String key = node1.getNodeValue();
            String value = getTemplateValue(node);
            predicates.put(key, value);
        }
    }

    private String sraiParse(Node node) {
        Category category = category(AIMLConst.default_topic, XmlHelper.node2String(node));
        return category != null ? getTemplateValue(category.getTemplate()) : AIMLConst.error_bot_response;
    }

    private String randomParse(Node node) {
        List<String> values = new ArrayList<>();
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            if (childNodes.item(i).getNodeName().equals(AIMLTag.li))
                values.add(XmlHelper.node2String(childNodes.item(i)));
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

    private Category category(String topic, String pattern) {
        return topics.get(topic).get(pattern);
    }
}