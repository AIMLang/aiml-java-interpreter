package com.batiaev.aiml.entity;

/**
 * AimlTokenType
 *
 * @author anton
 * @since 07/06/17
 */
public enum AimlTokenType {
    TEMPLATE("template"),
    PATTERN("pattern"),
    TOPIC("topic"),
    WORD("word");

    private String code;

    AimlTokenType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
