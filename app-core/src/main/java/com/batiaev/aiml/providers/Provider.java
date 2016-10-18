package com.batiaev.aiml.providers;

/**
 * Created by anbat on 18/10/16.
 *
 * @author anbat
 */
public interface Provider {
    String read();

    void write(String message);
}
