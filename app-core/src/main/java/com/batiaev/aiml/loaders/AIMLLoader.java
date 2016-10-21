package com.batiaev.aiml.loaders;

import com.batiaev.aiml.consts.AIMLConst;
import com.batiaev.aiml.consts.AIMLTag;
import com.batiaev.aiml.entity.Category;
import com.batiaev.aiml.entity.CategoryList;
import com.batiaev.aiml.utils.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

/**
 * Created by anbat on 21/06/15.
 *
 * @author anton
 */
public class AIMLLoader {
    private static final Logger LOG = LoggerFactory.getLogger(AIMLLoader.class);
    private CategoryList categoryList;

    public AIMLLoader() {
        categoryList = new CategoryList();
    }

    /**
     * Loading all aiml files from folder
     *
     * @param aimlDir folder contain all aiml files
     * @return categoryList
     */
    public CategoryList loadFiles(String aimlDir) {

        File aimls = new File(aimlDir);
        File[] files = aimls.listFiles();
        if (files == null || files.length == 0) {
            LOG.warn("Not files in folder {} ", aimls.getAbsolutePath());
            return categoryList;
        }
        int countNotAimlFiles = 0;
        for (File file : files) {
            if (file.getName().endsWith(AIMLConst.aiml_file_suffix))
                loadFile(file);
            else ++countNotAimlFiles;
        }
        if (countNotAimlFiles != 0)
            LOG.warn("Founded {} not aiml files in folder {}", countNotAimlFiles, aimlDir);
        LOG.info("Loaded {} categories", categoryList.size());
        return categoryList;
    }

    /**
     * Loading single aiml file
     *
     * @param aimlFile aiml file
     */
    private void loadFile(File aimlFile) {
        Element aimlRoot = XmlHelper.loadXml(aimlFile);
        if (aimlRoot == null)
            return;

        if (!aimlRoot.getNodeName().equals(AIMLTag.aiml)) {
            LOG.warn(aimlFile.getName() + " is not AIML file");
            return;
        }
        String aimlVersion = aimlRoot.getAttribute("version");
        LOG.debug("Load aiml " + aimlFile.getName() + (aimlVersion.isEmpty() ? "" : " [v." + aimlVersion + "]"));

        NodeList childNodes = aimlRoot.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i)
            aimlParser(childNodes.item(i));
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
                    LOG.debug(XmlHelper.node2String(node));
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
                    categoryList.add(parseCategory(childNodes.item(i), getAttribute(node, AIMLTag.name)));
                    break;
                default:
                    LOG.warn("Wrong structure: <topic> tag contain " + childNodeName + " tag");
            }
        }
    }

    private String getAttribute(Node node, String attributeName) {
        return node.getAttributes().getNamedItem(attributeName).getNodeValue();
    }

    private Category parseCategory(Node node) {
        return parseCategory(node, AIMLConst.default_topic);
    }

    private Category parseCategory(Node node, String topic) {
        Category category = new Category();
        category.setTopic(topic);
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); ++i) {
            String childNodeName = childNodes.item(i).getNodeName();
            switch (childNodeName) {
                case AIMLTag.text:
                case AIMLTag.comment:
                    break;
                case AIMLTag.pattern:
                    category.setPattern(XmlHelper.node2String(childNodes.item(i)));
                    break;
                case AIMLTag.template:
                    category.setTemplate(childNodes.item(i));
                    break;
                case AIMLTag.topic:
                    category.setTopic(XmlHelper.node2String(childNodes.item(i)));
                    break;
                case AIMLTag.that:
                    category.setThat(XmlHelper.node2String(childNodes.item(i)));
                    break;
                default:
                    LOG.warn("Wrong structure: <category> tag contain " + childNodeName + " tag");
            }
        }
        return category;
    }
}
