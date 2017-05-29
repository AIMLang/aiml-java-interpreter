package com.batiaev.aiml.loaders;

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Load root element from xml file
 *
 * @author batiaev
 * @since 25/10/16
 */
public class XmlLoader implements FileLoader<Element> {
    private static final Logger log = getLogger(XmlLoader.class);

    @Override
    public Element load(File file) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Document doc = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        if (doc == null) return null;

        Element rootElement = doc.getDocumentElement();
        rootElement.normalize();
        return rootElement;
    }

    @Override
    public Map<String, Element> loadAll(File... files) {
        Map<String, Element> data = new HashMap<>();
        for (File file : files)
            data.put(file.getName(), load(file));
        log.info("Loaded {} files", data.size());
        return data;
    }
}
