package org.aimlang.core.bot;

import org.aimlang.core.channels.ChannelType;
import org.aimlang.core.chat.Chat;
import org.aimlang.core.chat.ChatContext;
import org.aimlang.core.chat.ChatContextStorage;
import org.aimlang.core.core.Named;

/**
 * Bot
 *
 * @author anton
 * @since 18/04/17
 */
public interface Bot extends Named {
    String getRespond(String phrase);

    default void startChat(String userId, ChannelType channelType) {
        setChatContext(getChatContextStorage().getContext(userId, channelType));
        buildChat().start();
    }

    Chat buildChat();

    ChatContextStorage getChatContextStorage();

    void setChatContext(ChatContext context);

    boolean wakeUp();
}
