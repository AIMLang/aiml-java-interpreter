package com.batiaev.aiml.entity;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by anbat on 6/18/15.
 *
 * @author anton
 */
public class CategoryList {

    private HashMap<String, HashMap<String, Category>> topics;

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
        HashMap<String, Category> topicMap = topics.get(topic);
        if (topicMap == null) {
            HashMap<String, Category> pattern = new HashMap<>();
            topics.put(topic, pattern);
        }
        return topics.get(topic).keySet();
    }

    public Category category(String topic, String pattern) {
        return topics.get(topic).get(pattern);
    }

    public boolean add(Category category) {
        boolean result;
        if (topics.containsKey(category.getTopic()))
            result = topics.get(category.getTopic()).put(category.getPattern(), category) == null;
        else {
            HashMap<String, Category> pattern = new HashMap<>();
            pattern.put(category.getPattern(), category);
            result = topics.put(category.getTopic(), pattern) == null;
        }
        return result;
    }
}
