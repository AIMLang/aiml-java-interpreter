package com.batiaev.aiml.entity;

import com.batiaev.aiml.consts.AIMLTag;

import java.util.List;

/**
 * Created by anton on 21/10/16.
 *
 * @author anton
 */
public class AimlTopic implements AimlElement {
    private List<Category> categories;
    private String name;

    @Override
    public String getType() {
        return AIMLTag.topic;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
