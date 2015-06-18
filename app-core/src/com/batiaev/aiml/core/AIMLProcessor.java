package com.batiaev.aiml.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

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
        return "Brain contain: " + topicNodes + " topics, " + categoryNodes + " categories, "
                + sraiNodes + " links, " + sraixNodes + " external links.";
    }

    void aimlParser(Node node) {
        String nodeName = node.getNodeName();
        switch (nodeName) {
            case AIMLTag.topic:
                ++topicNodes;
                break;
            case AIMLTag.category:
                ++categoryNodes;
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
}