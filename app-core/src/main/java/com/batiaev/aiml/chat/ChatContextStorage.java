package com.batiaev.aiml.bot;

import com.batiaev.aiml.channels.ChannelType;
import com.batiaev.aiml.chat.ChatContext;
import org.springframework.stereotype.Repository;

/**
 * ChatContextStorage
 *
 * @author anton
 * @since 19/04/17
 */
@Repository
public interface ChatContextStorage {
    ChatContext getContext(String userId, ChannelType channelType);
}
