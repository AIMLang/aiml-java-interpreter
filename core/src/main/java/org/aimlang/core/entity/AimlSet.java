package org.aimlang.core.entity;

import org.aimlang.core.consts.AimlTag;
import org.aimlang.core.core.Named;

import java.util.HashSet;
import java.util.Set;

/**
 * Implements AIML Sets
 *
 * @author anton
 * @since 19/10/16
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
