package com.batiaev.aiml.entity;

import com.batiaev.aiml.consts.AimlTag;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Aiml category
 *
 * @author anton
 * @since 21/06/15
 */
public class AimlCategory implements AimlElement {
    private String topic = "";
    private String pattern = "";
    private Node template = null;
    private List<AimlToken> templateData = new ArrayList<>();
    private String that = "";

    @Override
    public String getType() {
        return AimlTag.category;
    }

    public String getTopic() {
        return topic;
    }

    public String getPattern() {
        return pattern;
    }

    public Node getTemplate() {
        return template;
    }

    public List<AimlToken> getTemplateData() {
        return templateData;
    }

    public String getThat() {
        return that;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setTemplate(Node template) {
        this.template = template;
    }

    public void setTemplateData(List<AimlToken> templateData) {
        this.templateData = templateData;
    }

    public void setThat(String that) {
        this.that = that;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof AimlCategory)) {
            return false;
        } else {
            AimlCategory other = (AimlCategory) o;
            return Objects.equals(topic, other.getTopic())
                    && Objects.equals(pattern, other.getPattern())
                    && Objects.equals(template, other.getTemplate())
                    && Objects.equals(that, other.getThat());
        }
    }

    @Override
    public AimlCategory clone() {
        AimlCategory category = new AimlCategory();
        category.setTopic(topic);
        category.setPattern(pattern);
        category.setTemplate(template);
        category.setTemplateData(templateData);
        category.setThat(that);
        return category;
    }

    public int hashCode() {
        int result = 1;
        result = 31 * result + (topic == null ? 0 : topic.hashCode());
        result = 31 * result + (pattern == null ? 0 : pattern.hashCode());
        result = 31 * result + (template == null ? 0 : template.hashCode());
        result = 31 * result + (that == null ? 0 : that.hashCode());
        return result;
    }

    public String toString() {
        return "AimlCategory(topic=" + topic + ", pattern=" + pattern + ", template=" + template + ", that=" + that + ")";
    }
}
