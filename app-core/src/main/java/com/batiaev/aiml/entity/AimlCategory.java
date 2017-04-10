package com.batiaev.aiml.entity;

import com.batiaev.aiml.consts.AimlTag;
import lombok.Data;
import org.w3c.dom.Node;

/**
 * Aiml category
 *
 * @author anton
 * @since 21/06/15
 */
@Data
public class AimlCategory implements AimlElement {
    private String topic = "";
    private String pattern = "";
    private Node template = null;
    private String that = "";

    @Override
    public String getType() {
        return AimlTag.category;
    }
}
