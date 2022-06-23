package org.aimlang.core.channels;

import org.aimlang.core.bot.Bot;
import org.aimlang.core.chat.ChatMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

import static org.aimlang.core.channels.ChannelType.CONSOLE;

/**
 * Console provider
 *
 * @author batiaev
 * @since 18/10/16
 */
public class ConsoleChannel implements Channel {

    private final BufferedReader reader;
    private final String botName;

    public static ConsoleChannel chatWith(Bot bot) {
        return new ConsoleChannel(bot.getName());
    }

    public ConsoleChannel(String botName) {
        this.botName = botName;
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void subscribe(Consumer<ChatMessage> messageHandler) {
        while (true) {
            String textLine = null;
            try {
                textLine = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            messageHandler.accept(new ChatMessage("console", "default", textLine));
        }
    }

    @Override
    public void write(ChatMessage message) {
        System.out.println(botName + ": " + message.getMessage());
    }

    @Override
    public ChannelType getType() {
        return CONSOLE;
    }
}
