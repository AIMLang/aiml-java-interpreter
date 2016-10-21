package com.batiaev.aiml.entity;

import com.batiaev.aiml.consts.AimlTag;
import org.w3c.dom.Node;

/**
 * Created by anbat on 21/06/15.
 *
 * @author anton
 */
public class AimlCategory implements AimlElement {
    private String topic = "";
    private String pattern = "";
    private Node template = null;
    private String that = "";

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Node getTemplate() {
        return template;
    }

    public void setTemplate(Node template) {
        this.template = template;
    }

    public String getThat() {
        return that;
    }

    public void setThat(String that) {
        this.that = that;
    }

    @Override
    public String getType() {
        return AimlTag.category;
    }
}
