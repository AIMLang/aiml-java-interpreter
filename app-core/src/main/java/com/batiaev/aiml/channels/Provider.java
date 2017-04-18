package com.batiaev.aiml.providers;

/**
 * Communication channel
 *
 * @author anbat
 * @since 18/10/16
 */
public interface Provider {
    String read();

    void write(String message);
}
