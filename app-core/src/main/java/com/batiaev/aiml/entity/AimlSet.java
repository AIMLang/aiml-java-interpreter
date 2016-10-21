package com.batiaev.aiml.entity;

import com.batiaev.aiml.consts.AimlTag;
import com.batiaev.aiml.core.Named;

import java.util.HashSet;
import java.util.Set;

/**
 * Implements AIML Sets
 *
 * @author anton
 */
public class AimlSet extends HashSet<String> implements Named, AimlElement {
    private final String name;

    public AimlSet(String name, Set<String> data) {
        super(data);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return AimlTag.set;
    }
}
