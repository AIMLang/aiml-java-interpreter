package com.batiaev.aiml.chat;

import com.batiaev.aiml.channels.ChannelType;

/**
 * ChatContextStorage
 *
 * @author anton
 * @since 19/04/17
 */
public interface ChatContextStorage {
    ChatContext getContext(String userId, ChannelType channelType);
}
