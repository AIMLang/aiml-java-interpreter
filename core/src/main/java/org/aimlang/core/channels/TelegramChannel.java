package org.aimlang.core.channels;

import org.aimlang.core.chat.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.function.Consumer;

public class TelegramChannel extends TelegramLongPollingBot implements Channel {
    private static final Logger log = LoggerFactory.getLogger(TelegramChannel.class);
    private final String username;
    private final String token;
    private Consumer<ChatMessage> messageConsumer;

    public TelegramChannel(String username, String token) {
        this.username = username;
        this.token = token;
    }

    @Override
    public void subscribe(Consumer<ChatMessage> messageHandler) {
        this.messageConsumer = messageHandler;
    }

    @Override
    public void close() {
        this.onClosing();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            messageConsumer.accept(new ChatMessage(
                            update.getMessage().getFrom().getId().toString(),
                    chatId,
                            update.getMessage().getText()
                    )
            );
        }
    }

    @Override
    public void write(ChatMessage message) {
        var sendMessage = SendMessage.builder()
                .chatId(message.getChatId())
                .text(message.getMessage())
                .build();
        try {
            this.sendApiMethod(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Exception when sending message: ", e);
        }
    }

    @Override
    public ChannelType getType() {
        return ChannelType.TELEGRAM;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
