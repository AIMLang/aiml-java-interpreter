package com.batiaev.aiml;

import com.batiaev.aiml.bot.BotImpl;
import com.batiaev.aiml.bot.BotRepository;
import com.batiaev.aiml.channels.Channel;
import com.batiaev.aiml.channels.ConsoleChannel;
import com.batiaev.aiml.channels.Provider;
import com.batiaev.aiml.chat.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * @author batiaev
 * @since 30/06/15
 */
@SpringBootApplication
public class App {

    @Value("app.debug")
    private static boolean debug;
    private final BotRepository botRepository;

    public App(BotRepository botRepository) {
        this.botRepository = botRepository;
    }

    public static void main(String[] args) {
        if (args.length > 0 && "debug".equals(args[0]))
            debug = true;

        SpringApplication.run(App.class, args);
    }

    @PostConstruct
    public void init() {
        BotImpl bot;
        if (debug) {
            bot = (BotImpl) botRepository.get("russian");
        } else {
            bot = (BotImpl) botRepository.get();
        }
        Provider provider = new ConsoleChannel(bot);

        Channel consoleChannel = new ConsoleChannel(bot);
        consoleChannel.startChat("Tony");

        if (!bot.wakeUp())
            return;
        Chat chat = new Chat(bot, provider);
        chat.start();
    }
}