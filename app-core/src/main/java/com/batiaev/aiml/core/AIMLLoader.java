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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by anbat on 21/06/15.
 *
 * @author anbat
 */
public class AIMLLoader {
    private static final Logger LOG = LogManager.getLogger(AIMLLoader.class);
    private CategoryList categoryList;

    public AIMLLoader() {
        categoryList = new CategoryList();
    }

    /**
     * Loading all aiml files from folder
     *
     * @param aimlDir folder contain all aiml files
     */
    CategoryList loadFiles(String aimlDir) {

        File aimls = new File(aimlDir);
        File[] files = aimls.listFiles();
        if (files == null) {
            LOG.warn("Not files in folder: " + aimls.getAbsolutePath());
            return null;
        }
        int countNotAimlFiles = 0;
        for (File file : files) {
            if (file.getName().endsWith(AIMLConst.aiml_file_suffix))
                loadFile(file);
            else ++countNotAimlFiles;
        }
        if (countNotAimlFiles != 0) LOG.warn("Founded " + countNotAimlFiles + " not aiml files in folder " + aimlDir);
        LOG.info(getStat());
        return categoryList;
    }

    /**
     * Loading single aiml file
     *
     * @param aimlFile aiml file
     */
    private void loadFile(File aimlFile) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Document doc = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(aimlFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        if (doc == null) return;

        doc.getDocumentElement().normalize();
        Element aimlRoot = doc.getDocumentElement();
        if (!aimlRoot.getNodeName().equals(AIMLTag.aiml)) {
            LOG.warn(aimlFile.getName() + " is not AIML file");
            return;
        }
        String aimlVersion = aimlRoot.getAttribute("version");
        LOG.debug("Load aiml " + aimlFile.getName() + (aimlVersion.isEmpty() ? "" : " [v." + aimlRoot.getAttribute("version") + "]"));

        NodeList childNodes = aimlRoot.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) aimlParser(childNodes.item(i));
    }

    private void aimlParser(Node node) {
        String nodeName = node.getNodeName();
        switch (nodeName) {
            case AIMLTag.text:
            case AIMLTag.comment:
                break;
            case AIMLTag.topic:
                parseTopic(node);
                break;
            case AIMLTag.category:
                if (!categoryList.add(parseCategory(node)))
                    System.out.println(node2String(node));
                break;
            default:
                LOG.warn("Wrong structure: <aiml> tag contain " + nodeName + " tag");
        }
    }

    private void parseTopic(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            String childNodeName = childNodes.item(i).getNodeName();
            switch (childNodeName) {
                case AIMLTag.text:
                case AIMLTag.comment:
                    break;
                case AIMLTag.category:
                    categoryList.add(parseCategory(childNodes.item(i), node.getAttributes().getNamedItem(AIMLTag.name).getNodeValue()));
                    break;
                default:
                    LOG.warn("Wrong structure: <topic> tag contain " + childNodeName + " tag");
            }
        }
    }

    private Category parseCategory(Node node) {
        return parseCategory(node, AIMLConst.default_topic);
    }

    private Category parseCategory(Node node, String topic) {
        Category category = new Category();
        category.topic = topic;
        category.node = node;
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            String childNodeName = childNodes.item(i).getNodeName();
            switch (childNodeName) {
                case AIMLTag.text:
                case AIMLTag.comment:
                    break;
                case AIMLTag.pattern:
                    category.pattern = node2String(childNodes.item(i));
                    break;
                case AIMLTag.template:
                    category.template = node2String(childNodes.item(i));
                    break;
                case AIMLTag.topic:
                    category.topic = node2String(childNodes.item(i));
                    break;
                case AIMLTag.that:
                    category.that = node2String(childNodes.item(i));
                    break;
                default:
                    LOG.warn("Wrong structure: <category> tag contain " + childNodeName + " tag");
            }
        }
        return category;
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

    public String getStat() {
        return "Loaded " + categoryList.size() + " categories";
    }
}
