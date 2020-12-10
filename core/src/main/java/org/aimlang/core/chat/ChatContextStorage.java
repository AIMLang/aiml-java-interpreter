package org.aimlang.core.chat;

import org.aimlang.core.channels.ChannelType;

/**
 * ChatContextStorage
 *
 * @author anton
 * @since 19/04/17
 */
public interface ChatContextStorage {
    ChatContext getContext(String userId, ChannelType channelType);
}
