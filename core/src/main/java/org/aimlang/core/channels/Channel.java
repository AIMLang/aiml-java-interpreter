package org.aimlang.core.channels;

import org.aimlang.core.chat.ChatMessage;

import java.util.function.Consumer;

/**
 * Communication channel
 *
 * @author batiaev
 * @since 18/10/16
 */
public interface Channel {
    void subscribe(Consumer<ChatMessage> messageHandler);

    void write(ChatMessage message);

    ChannelType getType();

    default void close() {}
}
