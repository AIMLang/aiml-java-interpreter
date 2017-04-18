package com.batiaev.aiml.bot;

import com.batiaev.aiml.channels.ChannelType;
import com.batiaev.aiml.chat.ChatContext;
import com.batiaev.aiml.chat.ChatContextStorage;
import com.batiaev.aiml.core.Named;
import org.springframework.stereotype.Component;

/**
 * Bot
 *
 * @author anton
 * @since 18/04/17
 */
@Component
public interface Bot extends Named {
    String getRespond(String phrase);

    default void startChat(String userId, ChannelType channelType) {
        setChatContext(getChatContextStorage().getContext(userId, channelType));
    }

    ChatContextStorage getChatContextStorage();

    void setChatContext(ChatContext context);
}
