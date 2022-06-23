package org.aimlang.core.chat;

import java.util.Objects;

public class ChatMessage {
    private final String userId;
    private final String chatId;
    private final String message;

    public ChatMessage(String userId, String chatId, String message) {
        this.userId = userId;
        this.chatId = chatId;
        this.message = message;
    }

    public ChatMessage response(String message) {
        return new ChatMessage(userId, chatId, message);
    }

    public String getUserId() {
        return userId;
    }

    public String getChatId() {
        return chatId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "userId='" + userId + '\'' +
                ", chatId='" + chatId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage message1 = (ChatMessage) o;
        return Objects.equals(userId, message1.userId) && Objects.equals(chatId, message1.chatId) && Objects.equals(message, message1.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, chatId, message);
    }
}
