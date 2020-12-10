package org.aimlang.core.chat;

public class ChatContextStorageFactory {
    public static InMemoryChatContextStorage inMemory() {
        return new InMemoryChatContextStorage();
    }
}
