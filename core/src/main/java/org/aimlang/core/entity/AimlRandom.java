package org.aimlang.core.entity;

import org.aimlang.core.consts.AimlTag;
import org.aimlang.core.utils.AppUtils;

import java.util.List;

/**
 * Aiml random tag
 *
 * @author anton
 * @since 21/10/16
 */
public class AimlRandom implements AimlElement {
    private final List<String> options;

    public AimlRandom(List<String> options) {
        this.options = options;
    }

    @Override
    public String getType() {
        return AimlTag.random;
    }

    public String getValue() {
        return AppUtils.getRandom(options);
    }
}
