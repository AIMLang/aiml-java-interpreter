package com.batiaev.aiml.entity;

import com.batiaev.aiml.core.Named;

import java.util.HashSet;
import java.util.Set;

/**
 * Implements AIML Sets
 *
 * @author anton
 */
public class AIMLSet extends HashSet<String> implements Named {
    private String name;

    public AIMLSet(String name, Set<String> data) {
        super(data);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
