package com.batiaev.aiml.chat;

import com.batiaev.aiml.core.AIMLConst;
import com.batiaev.aiml.core.Bot;
import com.batiaev.utils.IOUtils;

/**
 * @author batiaev
 * Created by batyaev on 6/18/15.
 * ---
 * Copyright © 2015. Anton Batiaev. All Rights Reserved.
 * www.batiaev.com
 */
public class Chat {
    private String nickname = "Human";
    private Bot bot = null;
    ChatState state;

    public Chat(Bot bot) {
        this.bot = bot;
    }

    public void start() {
        System.out.print("Write your nickname: ");
        nickname = IOUtils.read();
        state = new ChatState(nickname);
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
                case "/s":
                    write(bot.getBrainStats());
                    break;
                case "/reload":
                case "/r":
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
                case "/debug on":
                case "/debug true":
                    AIMLConst.debug = true;
                    break;
                case "/debug off":
                case "/debug false":
                    AIMLConst.debug = false;
                    break;
                default:
                    String response = bot.multisentenceRespond(textLine, state);
                    state.newState(textLine, response);
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
