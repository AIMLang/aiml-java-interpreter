package com.batiaev.aiml.entity;

import com.batiaev.aiml.core.Named;

import java.util.HashMap;
import java.util.Map;

import static com.batiaev.aiml.consts.AimlTag.map;

/**
 * Implements AIML Map
 * A map is a function from one string set to another.
 * Elements of the domain are called keys and elements of the range are called values.
 *
 * @author anton
 * @since 19/10/16
 */
public class AimlMap extends HashMap<String, String> implements Named, AimlElement {
    protected final String name;

    public AimlMap(String name, Map<String, String> data) {
        super(data);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return map;
    }
}
