package org.aimlang.core.bot;

import org.aimlang.core.channels.Channel;
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

    Chat buildChat(Channel channel);

    ChatContextStorage getChatContextStorage();

    void setChatContext(ChatContext context);

    boolean wakeUp();
}
