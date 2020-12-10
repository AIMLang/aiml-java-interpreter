package org.aimlang.core.channels;

import org.aimlang.core.bot.Bot;
import org.aimlang.core.exception.BotNotInitializedException;
import org.aimlang.core.exception.ChatNotStartedException;

/**
 * Channel
 *
 * @author anton
 * @since 18/04/17
 */
public interface Channel {

    default void startChat(String userId) {
        getBot().startChat(userId, getType());
    }

    Bot getBot();

    default void send(String phrase) throws ChatNotStartedException, BotNotInitializedException {
        if (getResponseHandler() == null)
            throw new ChatNotStartedException("response handler is not set");
        if (getBot() == null)
            throw new BotNotInitializedException("Bot is not set");
        var respond = getBot().getRespond(phrase);
        getResponseHandler().respond(respond);
    }

    ChannelType getType();

    ResponseHandler getResponseHandler();

    /**
     * ResponseHandler
     *
     * @author anton
     * @since 18/04/17
     */
    @FunctionalInterface
    interface ResponseHandler {
        void respond(String respond);
    }
}
