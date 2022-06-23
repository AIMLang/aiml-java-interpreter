package org.aimlang.core.chat;

import org.aimlang.core.bot.BotImpl;
import org.aimlang.core.channels.Channel;
import org.aimlang.core.consts.AimlConst;

import java.util.Optional;

import static java.lang.String.format;

/**
 * Chat
 *
 * @author batiaev
 * @since 6/18/15
 */
public class Chat {
    private final static String DEFAULT_NICKNAME = "Human";
    private final BotImpl bot;
    private final Channel channel;
    private ChatContext state;
    private boolean started;

    public Chat(BotImpl bot, Channel channel) {
        this.bot = bot;
        this.channel = channel;
    }

    public void start() {
        start(DEFAULT_NICKNAME);
    }

    public void start(String nickname) {
        String chatId = "";
        channel.write(new ChatMessage(nickname, chatId, "Welcome to chat with " + bot.getName() + ".\n" + nickname + ": "));
        started = true;
        state = new ChatContext(nickname);
        channel.subscribe(this::handle);
    }

    private void handle(ChatMessage message) {
        if (started)
            process(message)
                    .ifPresent(channel::write);
    }

    private Optional<ChatMessage> process(ChatMessage msg) {
        var textLine = msg.getMessage();
        var message = textLine == null || textLine.isEmpty() ? AimlConst.null_input : textLine.trim();
        msg = msg.response(message);
        if (message.startsWith("/")) {
            return parseCommand(msg);
        } else {
            String response = bot.multisentenceRespond(message, state);
            state.newState(message, response);
            return Optional.of(new ChatMessage(msg.getUserId(), msg.getChatId(), response));
        }
    }

    public void stop() {
        started = false;
        channel.close();
    }

    private Optional<ChatMessage> parseCommand(final ChatMessage msg) {
        var command = msg.getMessage();
        switch (command) {
            case ChatCommand.exit:
            case ChatCommand.quit:
                stop();
                System.exit(0);
            case ChatCommand.stat:
                return Optional.of(msg.response(bot.getBrainStats()));
            case ChatCommand.reload:
                bot.wakeUp();
                return Optional.of(msg.response(format("Bot %s reloaded", bot.getName())));
            case "/connect russian":
            case "/c russian":
                bot.setName("russian");
                bot.wakeUp();
                return Optional.of(msg.response(format("Connected to bot %s", bot.getName())));
            case "/connect alice2":
            case "/c alice2":
                bot.setName("alice2");
                bot.wakeUp();
                return Optional.of(msg.response(format("Connected to bot %s", bot.getName())));
            case "/debug on":
            case "/debug true":
                AimlConst.debug = true;
                return Optional.empty();
            case "/debug off":
            case "/debug false":
                AimlConst.debug = false;
                return Optional.empty();
            default:
                var response = bot.multisentenceRespond(command, state);
                state.newState(command, response);
                return Optional.of(msg.response(response));
        }
    }
}
