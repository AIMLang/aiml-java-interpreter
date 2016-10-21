package com.batiaev.aiml.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by anbat on 6/18/15.
 *
 * @author anton
 */
public class CategoryList {

    private Map<String, Map<String, Category>> topics;

    public CategoryList() {
        topics = new HashMap<>();
    }

    public int topicCount() {
        return topics.size();
    }

    public int size() {
        int total = 0;
        if (topics.isEmpty())
            return total;
        for (String topic : topics.keySet())
            total += topics.get(topic).size();
        return total;
    }

    public Set<String> patterns(String topic) {
        if (topics.containsKey(topic)) {
            return topics.get(topic).keySet();
        } else {
            topics.put(topic, new HashMap<>());
            return Collections.emptySet();
        }
    }

    public Category category(String topic, String pattern) {
        return topics.get(topic).get(pattern);
    }

    public boolean add(Category category) {
        boolean result;
        final String topic = category.getTopic();
        final String pattern = category.getPattern();

        if (topics.containsKey(topic))
            result = topics.get(topic).put(pattern, category) == null;
        else {
            Map<String, Category> patterns = new HashMap<>();
            patterns.put(pattern, category);
            result = topics.put(topic, patterns) == null;
        }
        return result;
    }
}
