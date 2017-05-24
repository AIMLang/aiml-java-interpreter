package com.batiaev.aiml;

import com.batiaev.aiml.bot.BotImpl;
import com.batiaev.aiml.bot.BotRepository;
import com.batiaev.aiml.channels.Channel;
import com.batiaev.aiml.channels.ConsoleChannel;
import com.batiaev.aiml.channels.Provider;
import com.batiaev.aiml.chat.Chat;
import com.batiaev.aiml.exception.BotNotInitializedException;
import com.batiaev.aiml.exception.ChatNotStartedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * @author batiaev
 * @since 30/06/15
 */
@SpringBootApplication
public class App {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }

    @Autowired
    private BotRepository botRepository;

    @PostConstruct
    public void init() {
        botRepository.setRootPath("./app-core/aiml-bots/bots");
//        BotImpl bot = (BotImpl) botRepository.get();
        BotImpl bot = (BotImpl) botRepository.get("jokebot");
        Provider provider = new ConsoleChannel(bot);

        Channel consoleChannel = new ConsoleChannel(bot);
        consoleChannel.startChat("Tony");
        try {
            consoleChannel.send("Hello!");
        } catch (ChatNotStartedException | BotNotInitializedException e) {
            e.printStackTrace();
        }

        if (!bot.wakeUp())
            return;
        Chat chat = new Chat(bot, provider);
        chat.start();
    }
}