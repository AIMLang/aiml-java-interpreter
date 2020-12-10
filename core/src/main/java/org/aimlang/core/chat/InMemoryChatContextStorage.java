package org.aimlang.core.chat;

import org.aimlang.core.channels.ChannelType;

import java.util.HashMap;
import java.util.Map;

/**
 * InMemoryChatContextStorage
 *
 * @author batiaev
 * @since 24/05/17
 */
public class InMemoryChatContextStorage implements ChatContextStorage {
    private Map<String, Map<ChannelType, ChatContext>> contexts;

    public InMemoryChatContextStorage() {
        contexts = new HashMap<>();
    }

    @Override
    public ChatContext getContext(String userId, ChannelType channelType) {
        var userContexts = contexts.get(userId);
        if (userContexts == null || userContexts.isEmpty()) {
            var context = new ChatContext(userId);
            userContexts = new HashMap<>();
            userContexts.put(channelType, context);
            contexts.put(userId, userContexts);
            return context;
        } else {
            return userContexts.computeIfAbsent(channelType, k -> new ChatContext(userId));
        }
    }
}
