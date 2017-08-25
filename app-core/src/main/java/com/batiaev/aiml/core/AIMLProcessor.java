package com.batiaev.aiml.core;

import com.batiaev.aiml.bot.BotInfo;
import com.batiaev.aiml.consts.AimlConst;
import com.batiaev.aiml.consts.AimlTag;
import com.batiaev.aiml.entity.AimlCategory;
import com.batiaev.aiml.utils.AppUtils;
import org.slf4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.batiaev.aiml.consts.AimlTag.*;
import static com.batiaev.aiml.consts.WildCard.*;
import static org.slf4j.LoggerFactory.getLogger;

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
 * @since 19/10/16
 */
public class AIMLProcessor {
    private static final Logger log = getLogger(AIMLProcessor.class);

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

    public String match(final String input, String topic, String that, List<String> stars) {
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
            else if (isMatching(request, pattern, stars))
                return pattern;
        }
        return result;
    }

    public String template(List<String> stars, String pattern, String topic, String that, Map<String, String> predicates) {
        this.predicates = predicates;
        AimlCategory category = category(topic, pattern);
        if (category == null)
            category = category(AimlConst.default_topic, OneMore.get());
        return category == null ? AimlConst.default_bot_response : getTemplateValue(category.getTemplate(), stars);
    }

    public int getTopicCount() {
        return topics.size();
    }

    public int getCategoriesCount() {
        return categories.size();
    }

    private boolean isMatching(String input, String pattern, List<String> stars) {
        input = input.trim();
        String regex = pattern.trim();
        regex = regex.replace(OneMorePriority.get(), "(.+)");
        regex = regex.replace(OneMore.get(), "(.+)");
        regex = regex.replace(ZeroMorePriority.get(), "(.*)");
        regex = regex.replace(ZeroMore.get(), "(.*)");

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        if (m.matches()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                if (i > 0) //skip first group because that is contain full input
                    stars.add(m.group(i).toLowerCase());
            }
            return true;
        }
        return false;
    }

    private String getTemplateValue(Node node, List<String> stars) {
        StringBuilder result = new StringBuilder();
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            result.append(recurseParse(childNodes.item(i), stars));
        }
        return (result.length() == 0) ? AimlConst.default_bot_response : result.toString();
    }

    private String recurseParse(Node node, List<String> stars) {
        node.normalize();
        String nodeName = node.getNodeName();
        switch (nodeName) {
            case text:
                return textParse(node);
            case template:
                return getTemplateValue(node, stars);
            case random:
                return randomParse(node);
            case srai:
                return sraiParse(node, stars);
            case set:
                setParse(node, stars);
                return "";//FIXME?
            case bot:
                return botInfoParse(node);
            case star:
                return starParse(node, stars);
            case think:
                getTemplateValue(node, stars);
                return "";
        }
        return "";
    }

    private String starParse(Node node, List<String> stars) {
        if (stars.isEmpty()) return "";
        if (node.hasAttributes()) {
            Element element = (Element) node;
            try {
                int index = Integer.parseInt(element.getAttribute("index")) - 1;
                if (stars.size() > index)
                    return stars.get(index);
            } catch (Exception e) {
                log.error("Invalid index format {}: {}", element.getAttribute("index"), e.getLocalizedMessage());
            }
        } else if (node.hasChildNodes()) {
            NodeList childNodes = node.getChildNodes();
            if (childNodes.getLength() == 1) {
                Node item = childNodes.item(0);
                if (index.equals(item.getNodeName())) {
                    String sIndex = item.getNodeValue();
                    try {
                        int index = Integer.parseInt(sIndex) - 1;
                        if (stars.size() > index)
                            return stars.get(index);
                    } catch (Exception e) {
                        log.error("Invalid index format {}: {}", sIndex, e.getLocalizedMessage());
                    }
                }
            }
        }
        return stars.get(0);
    }

    private String botInfoParse(Node node) {
        String param = node.getAttributes().getNamedItem("name").getNodeValue();
        return botInfo.getValue(param);
    }

    private String textParse(Node node) {
        return node.getNodeValue().replaceAll("(\r\n|\n\r|\r|\n)", "").replaceAll("  ", " ");
    }

    private void setParse(Node node, List<String> stars) {
        NamedNodeMap attributes = node.getAttributes();
        if (attributes.getLength() > 0) {
            Node node1 = attributes.getNamedItem("getName");
            if (node1 == null) return;
            String key = node1.getNodeValue();
            String value = getTemplateValue(node, stars);
            predicates.put(key, value);
        }
    }

    private String sraiParse(Node node, List<String> stars) {
        AimlCategory category = category(AimlConst.default_topic, AppUtils.node2String(node));
        return category != null ? getTemplateValue(category.getTemplate(), stars) : AimlConst.error_bot_response;
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