package com.batiaev.aiml.bot;

import com.batiaev.aiml.exception.BotNotInitializedException;
import com.batiaev.aiml.exception.ChatNotStartedException;
import org.springframework.stereotype.Component;

/**
 * Channel
 *
 * @author anton
 * @since 18/04/17
 */
@Component
public interface Channel {
    void startChat(ResponseHandler handler, String userId);

    void send(String phrase) throws ChatNotStartedException, BotNotInitializedException;

    ChannelType getType();
}
