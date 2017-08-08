package com.batiaev.aiml.core;

import com.batiaev.aiml.bot.BotInfo;
import com.batiaev.aiml.consts.AimlConst;
import com.batiaev.aiml.consts.AimlTag;
import com.batiaev.aiml.entity.AimlCategory;
import com.batiaev.aiml.utils.AppUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.regex.Pattern;

import static com.batiaev.aiml.consts.AimlTag.*;
import static com.batiaev.aiml.consts.WildCard.*;

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
    private BotInfo botInfo;

    public AIMLProcessor(List<AimlCategory> categories, BotInfo botInfo) {
        this.predicates = new HashMap<>();
        this.topics = convert(categories);
        this.categories = categories;
        this.botInfo = botInfo;
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
        if (!AimlConst.default_topic.equals(topic))
            patterns.addAll(patterns(AimlConst.default_topic));

        String result = OneMore.get();
        for (String pattern : patterns) {
            if (OneMore.get().equals(pattern)
                    || OneMorePriority.get().equals(pattern)
                    || ZeroMore.get().equals(pattern)
                    || ZeroMorePriority.get().equals(pattern))
                result = pattern;
            else if (isMatching(request, pattern))
                return pattern;
        }
        return result;
    }

    public String template(String pattern, String topic, String that, Map<String, String> predicates) {
        this.predicates = predicates;
        AimlCategory category = category(topic, pattern);
        if (category == null)
            category = category(AimlConst.default_topic, OneMore.get());
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
        regex_pattern = regex_pattern.replace(OneMorePriority.get(), ".+");
        regex_pattern = regex_pattern.replace(OneMore.get(), ".+");
        regex_pattern = regex_pattern.replace(ZeroMorePriority.get(), ".*");
        regex_pattern = regex_pattern.replace(ZeroMore.get(), ".*");
        return Pattern.matches(regex_pattern, input);
    }

    private String getTemplateValue(Node node) {
        StringBuilder result = new StringBuilder();
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            result.append(recurseParse(childNodes.item(i)));
        }
        return (result.length() == 0) ? AimlConst.default_bot_response : result.toString();
    }

    private String recurseParse(Node node) {
        node.normalize();
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
            case bot:
                return botInfoParse(node);
            case star:
                return starParse(node);
            case think:
                getTemplateValue(node);
                return "";
        }
        return "";
    }

    private String starParse(Node node) {
        return ""; //FIXME unresolved yet
    }

    private String botInfoParse(Node node) {
        String param = node.getAttributes().getNamedItem("name").getNodeValue();
        return botInfo.getValue(param);
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