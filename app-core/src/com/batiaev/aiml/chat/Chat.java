package com.batiaev.aiml.chat;

import com.batiaev.aiml.core.AIMLConst;
import com.batiaev.aiml.core.Bot;
import com.batiaev.utils.IOUtils;

/**
 * Created by batyaev on 6/18/15.
 */
public class Chat {
    private String nickname = "Human";

    Bot bot = null;

    public Chat(Bot bot) {
        this.bot = bot;
    }

    public void start() {
        System.out.print("Write your nickname: ");
        nickname = IOUtils.read();
        System.out.println("Hello " + nickname + "! Welcome to chat with " + bot.name() + ".");

        String textLine;
        while (true) {
            textLine = read();
            if (textLine == null || textLine.length() < 1)  textLine = AIMLConst.null_input;
            switch (textLine) {
                case "/exit":
                case "/quit":
                case "/q":
                    System.exit(0);
                case "/stat":
                    write(bot.getBrainStats());
                    break;
                case "/reload":
                    bot.reload();
                    break;
                case "/connect russian":
                case "/c russian":
                    bot.setName("russian");
                    bot.reload();
                    break;
                case "/connect alice2":
                case "/c alice2":
                    bot.setName("alice2");
                    bot.reload();
                    break;
                default:
                    String response = AIMLConst.default_bot_response + " " + AIMLConst.error_bot_response;
                    write(response);
                    break;
            }
        }
    }

    public String read() {
        System.out.print(nickname + ": ");
        return IOUtils.read();
    }

    public void write(String message) {
        IOUtils.write(bot.name() + ": " + message);
    }
}
