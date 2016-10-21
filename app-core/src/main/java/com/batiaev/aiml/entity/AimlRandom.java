package com.batiaev.aiml.entity;

import com.batiaev.aiml.consts.AimlTag;
import com.batiaev.aiml.utils.AppUtils;

import java.util.List;

/**
 * Created by anton on 21/10/16.
 *
 * @author anton
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
