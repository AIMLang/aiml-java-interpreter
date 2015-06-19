package com.batiaev.aiml.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashMap;

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
 */
public class AIMLProcessor {
    int topicNodes = 0;
    int categoryNodes = 0;
    int sraiNodes = 0;
    int sraixNodes = 0;
    int thinkNodes = 0;
    private HashMap<String, HashMap<String, Node>> topics = null; // { topic : { pattern : <category>..</category> } }
    private static final Logger LOG = LogManager.getLogger(AIMLProcessor.class);

    void loadFiles(File aimlDir) {
        File[] files = aimlDir.listFiles();
        for (File file : files != null ? files : new File[0]) loadFile(file.getAbsolutePath());
        LOG.info(getStat());
    }

    void loadFile(String path) {
        File aimlFile = new File(path);

        if (!aimlFile.exists()) {
            LOG.warn(path + " not found");
            return;
        }
        if (!aimlFile.getName().contains(AIMLConst.aiml_file_suffix)) {
            LOG.warn(aimlFile.getName() + " is not AIML file");
            return;
        }
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Document doc = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(aimlFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        if (doc == null)
            return;

        doc.getDocumentElement().normalize();
        Element aimlRoot = doc.getDocumentElement();
        if (!aimlRoot.getNodeName().equals(AIMLTag.aiml)) {
            LOG.warn(aimlFile.getName() + " is not AIML file");
            return;
        }
        String aimlVersion = aimlRoot.getAttribute("version");
        LOG.debug("Load aiml \t\t" + aimlFile.getName() + (aimlVersion.isEmpty() ? "" : " [v." + aimlRoot.getAttribute("version") + "]"));

        NodeList childNodes = doc.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) aimlParser(childNodes.item(i));
    }

    public String getStat() {
        return "Brain contain: " + topicNodes + " topics, " + categoryNodes + " patterns, "
                + sraiNodes + " links, " + sraixNodes + " external links.";
    }

    void aimlParser(Node node) {
        String nodeName = node.getNodeName();
        switch (nodeName) {
            case AIMLTag.topic:
                ++topicNodes;
                parseTopic(node);
                break;
            case AIMLTag.category:
                ++categoryNodes;
                parseCategory(node);
                break;
            case AIMLTag.srai:
                ++sraiNodes;
                break;
            case AIMLTag.sraix:
                ++sraixNodes;
                break;
            case AIMLTag.think:
                ++thinkNodes;
                break;
        }

        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) aimlParser(childNodes.item(i));
    }

    private void parseTopic(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeName().equals(AIMLTag.category)) parseCategory(childNode);
        }
    }

    private String getTopicValue(Node node) {
        return node.getAttributes().getNamedItem("name").getNodeValue();
    }

    int count = 0;
    private void parseCategory(Node node) {
        if (topics == null)
            topics = new HashMap<String, HashMap<String, Node> >();
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            String childNodeName = childNodes.item(i).getNodeName();
            if (childNodeName.equals(AIMLTag.pattern)) {
                String topicName = node.getParentNode().getNodeName().equals(AIMLTag.topic)
                        ? getTopicValue(node.getParentNode())
                        : AIMLConst.default_topic;
                if (topics.containsKey(topicName)) {
                    if (topics.get(topicName).put(node2String(childNodes.item(i)), node) != null) {
                        ++count;
                        LOG.debug("### DUBLICATE CATEGORY!! " + topicName + " !! " + node2String(childNodes.item(i)));
                    }
                }
                else {
                    HashMap<String, Node> category = new HashMap<String, Node>();
                    if (category.put(node2String(childNodes.item(i)), node) != null) {
                        ++count;
                        LOG.debug("### DUBLICATE CATEGORY!! " + topicName + " !! " + node2String(childNodes.item(i)));
                    }
                    if (topics.put(topicName, category) != null) {
                        ++count;
                        LOG.debug("### DUBLICATE TOPIC!! " + topicName + " !! " + node2String(childNodes.item(i)));
                    }
                }
            }
        }
    }

    public String match(String pattern) {
        String tmpPattern = pattern.toUpperCase();
        int total = 0;
        topics.keySet().forEach(s -> LOG.debug("#TOPIC: " + s + " " + topics.get(s).size()));
        for (String key : topics.keySet()) total += topics.get(key).size();
        LOG.info("#Total patterns: " + total);
        LOG.info("#Total dublicates: " + count);
        return AIMLConst.default_bot_response;
    }

    private String node2String(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            System.out.println("nodeToString Transformer Exception");
        }
        return sw.toString().replaceAll("(\r\n|\n\r|\r|\n)", " ").replaceAll("> ", ">").replaceFirst("<pattern>", "").replaceFirst("</pattern>", "");
    }
}