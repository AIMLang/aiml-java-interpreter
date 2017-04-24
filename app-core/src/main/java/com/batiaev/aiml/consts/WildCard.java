package com.batiaev.aiml.consts;

/**
 * Wildcards
 *
 * @author batiaev
 * @since 19/06/15
 */
public enum WildCard {
    ZeroMore("^"),
    OneMore("*"),
    ZeroMorePriority("#"),
    OneMorePriority("_");

    private final String sumbol;

    WildCard(String sumbol) {
        this.sumbol = sumbol;
    }

    public String get() {
        return sumbol;
    }
}