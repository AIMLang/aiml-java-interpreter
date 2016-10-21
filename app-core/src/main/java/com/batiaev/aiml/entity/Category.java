package com.batiaev.aiml.entity;

import org.w3c.dom.Node;

/**
 * Created by anbat on 21/06/15.
 *
 * @author anton
 */
public class Category {
    private String topic = "";
    private String pattern = "";
    private String template = "";
    private String that = "";
    private Node node = null;

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

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getThat() {
        return that;
    }

    public void setThat(String that) {
        this.that = that;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
