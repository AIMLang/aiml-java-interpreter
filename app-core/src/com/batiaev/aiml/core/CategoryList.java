package com.batiaev.aiml.core;

import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by batyaev on 6/18/15.
 */
public class CategoryList extends HashSet<Category> {

    HashMap<String, HashMap<String, Node>> toTopicMap() {
        HashMap<String, HashMap<String, Node>> topic = new HashMap<String, HashMap<String, Node>>();
        for (Category category : this) {
            if (topic.containsKey(category.topic))
                topic.get(category.topic).put(category.pattern, category.node);
            else {
                HashMap<String, Node> pattern = new HashMap<>();
                pattern.put(category.pattern, category.node);
                topic.put(category.topic, pattern);
            }
        }
        return topic;
    }
}
