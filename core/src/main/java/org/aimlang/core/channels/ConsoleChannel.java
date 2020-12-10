package org.aimlang.core.channels;

import org.aimlang.core.bot.Bot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.aimlang.core.channels.ChannelType.CONSOLE;

/**
 * Console provider
 *
 * @author batiaev
 * @since 18/10/16
 */
public class ConsoleChannel implements Provider, Channel {

    private BufferedReader reader;
    private Bot bot;

    public static ConsoleChannel chatWith(Bot bot) {
        return new ConsoleChannel(bot);
    }

    public ConsoleChannel(Bot bot) {
        this.bot = bot;
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public Bot getBot() {
        return bot;
    }

    @Override
    public String read() {
        String textLine = null;
        try {
            textLine = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textLine;
    }

    @Override
    public void write(String message) {
        System.out.print(message);
    }

    @Override
    public ChannelType getType() {
        return CONSOLE;
    }

    @Override
    public ResponseHandler getResponseHandler() {
        return System.out::println;
    }
}
