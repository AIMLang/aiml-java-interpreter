package com.batiaev.aiml.channels;

/**
 * Communication channel
 *
 * @author batiaev
 * @since 18/10/16
 */
public interface Provider {
    String read();

    void write(String message);
}
