package com.batiaev.aiml.entity;

import com.batiaev.aiml.consts.AimlTag;

import java.util.List;

/**
 * Aiml Topic
 *
 * @author anton
 * @since 21/10/16
 */
public class AimlTopic implements AimlElement {
    private List<AimlCategory> categories;
    private String name;

    @Override
    public String getType() {
        return AimlTag.topic;
    }

    public List<AimlCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<AimlCategory> categories) {
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
