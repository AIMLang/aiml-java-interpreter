package com.batiaev.aiml.core;

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

    void loadFiles(File aimlDir) {
        File[] files = aimlDir.listFiles();
        for (File file : files != null ? files : new File[0]) loadFile(file.getAbsolutePath());
        System.out.println("Brain contain: " + topicNodes + " topics, " + categoryNodes + " categories, "
                + sraiNodes + " links, " + sraixNodes + " external links.");
    }

    void loadFile(String path) {
        File aimlFile = new File(path);

        if (!aimlFile.exists()) {
            System.out.println(path + " not found");
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
            System.out.println(aimlFile.getName() + " is not AIML file");
            return;
        }
        String aimlVersion = aimlRoot.getAttribute("version");
        System.out.println("Load aiml \t\t" + aimlFile.getName() + (aimlVersion.isEmpty() ? "" : " [v." + aimlRoot.getAttribute("version") + "]"));

        NodeList childNodes = doc.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) aimlParser(childNodes.item(i));
    }

    void aimlParser(Node node) {
        String nodeName = node.getNodeName();
        if (nodeName.equals(AIMLTag.topic))
            ++topicNodes;
        else if (nodeName.equals(AIMLTag.category))
            ++categoryNodes;
        else if (nodeName.equals(AIMLTag.srai))
            ++sraiNodes;
        else if (nodeName.equals(AIMLTag.sraix))
            ++sraixNodes;
        else if (nodeName.equals(AIMLTag.think))
            ++thinkNodes;

        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) aimlParser(childNodes.item(i));
    }
}