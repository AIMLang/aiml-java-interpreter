package com.batiaev.aiml.core;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 * @author batiaev
 * The core AIML parser and interpreter.
 * Implements the AIML 2.0 specification as described in
 * AIML 2.0 Working Draft document
 * https://docs.google.com/document/d/1wNT25hJRyupcG51aO89UcQEiG-HkXRXusukADpFnDs4/pub
 * https://playground.pandorabots.com/en/tutorial/
 * http://blog.pandorabots.com/aiaas-aiml-2-0-support/
 * http://www.alicebot.org/documentation/aiml101.html
 * http://itc.ua/articles/kvest_tyuringa_7667/
 * http://www.eblong.com/zarf/markov/chan.c
 * ---
 * @author Marco
 * Implementation <SET></SET> tag processing on 19/08/2016
 */
public class AIMLProcessor {
    private CategoryList categoryList = null;
    private HashMap<String, String> predicates = new HashMap<String, String>();

    void setCategoryList(CategoryList categories) {
        categoryList = categories;
    }

    public String getStat() {
        if (categoryList == null)
            categoryList = new CategoryList();
        return "Brain contain " + categoryList.topicCount() + " topics " + categoryList.size() + " categories";
    }

    public String match(String input, String topic, String that) {
        Set<String> patterns = categoryList.patterns(topic);
        for (String pattern : patterns) {
            if (isMatching(input.toUpperCase(), pattern))
                return pattern;
        }
        return WildCard.sumbol_1more;
    }

    private boolean isMatching(String input, String pattern) {
        input = input.trim();
        pattern = pattern.trim();
        if (pattern.contains(WildCard.sumbol_1more_higest) || pattern.contains(WildCard.sumbol_1more)
                || pattern.contains(WildCard.sumbol_0more_higest) || pattern.contains(WildCard.sumbol_0more)) {
            String[] inputWords = input.split(" ");
            String[] patternWords = pattern.split(" ");
            int inputIndex = 0;
            boolean tempValue = false;
            for (String patternWord : patternWords) {
                if (patternWord.equals(WildCard.sumbol_1more) || patternWord.equals(WildCard.sumbol_1more_higest)) {
                    ++inputIndex;
                } else if (patternWord.equals(WildCard.sumbol_0more) || patternWord.equals(WildCard.sumbol_0more_higest)) {
                    //
                } else {
                    tempValue = false;
                    for (int i = inputIndex; i < inputWords.length; ++i) {
                        tempValue = patternWord.equals(inputWords[i]);
                        if (tempValue) inputIndex = i;
                    }
                }
            }
            return tempValue;
        }
        else return pattern.equals(input);
    }

    private String node2String(Node node) {
        String nodeName = node.getNodeName();
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            System.out.println("nodeToString Transformer Exception");
        }
        return sw.toString().replaceAll("(\r\n|\n\r|\r|\n)", " ").replaceAll("> ", ">")
                .replaceFirst("<" + nodeName + ">", "").replaceFirst("</" + nodeName + ">", "");
    }

    public String getCategoryValue(Node node) {
        if (node == null)
            return AIMLConst.default_bot_response;

        String result = "";
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            if (childNodes.item(i).getNodeName().equals(AIMLTag.template))
                result = getTemplateValue(childNodes.item(i));
        }
        return result.isEmpty() ? AIMLConst.default_bot_response : result;
    }

    public String getTemplateValue(Node node) {
        String result = "";
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) result += recurseParse(childNodes.item(i));
        return result.isEmpty() ? AIMLConst.default_bot_response : result;
    }

    private String recurseParse(Node node) {
        String nodeName = node.getNodeName();
        switch (nodeName) {
            case AIMLTag.text:
                return node.getNodeValue().replaceAll("(\r\n|\n\r|\r|\n)", "").replaceAll("  ", " ");
            case AIMLTag.template:
                return getTemplateValue(node);
            case AIMLTag.random:
                return randomParse(node);
            case AIMLTag.srai:
                return sraiParse(node);
            case AIMLTag.set:
                setParse(node);
                return "";
        }
        return "";
    }

    private void setParse(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        if (attributes.getLength() > 0) {
            Node node1 = attributes.getNamedItem("name");
            String key = node1.getNodeValue();
            String value = getTemplateValue(node);
            predicates.put(key, value);
        }
        return;
    }

    private String sraiParse(Node node) {
        Category category = categoryList.category(AIMLConst.default_topic, node2String(node));
        return category != null ? getCategoryValue(category.node) : AIMLConst.error_bot_response;
    }

    private String randomParse(Node node) {
        ArrayList<String> values = new ArrayList<>();
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            if (childNodes.item(i).getNodeName().equals(AIMLTag.li)) values.add(node2String(childNodes.item(i)));
        }

        Random rn = new Random();
        int index = rn.nextInt(values.size());
        return values.get(index);
    }

    public String template(String pattern, String topic, String that, HashMap<String, String> predicates) {
        this.predicates = predicates;
        Category category = categoryList.category(topic, pattern);
        if (category == null)
            category = categoryList.category(AIMLConst.default_topic, WildCard.sumbol_1more);
        return category == null ? AIMLConst.default_bot_response : getCategoryValue(category.node);
    }
}