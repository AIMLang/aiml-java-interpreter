package com.batiaev.aiml.chat;

import com.batiaev.aiml.consts.AIMLConst;
import com.batiaev.aiml.bot.Bot;
import com.batiaev.aiml.providers.Provider;

/**
 * Created by anbat on 6/18/15.
 *
 * @author anbat
 */
public class Chat {
    private static String DEFAULT_NICKNAME = "Human";
    private String nickname = DEFAULT_NICKNAME;
    private Bot bot = null;
    private ChatState state;
    private Provider provider;
    private String nickName;

    public Chat(Bot bot, Provider provider) {
        this.bot = bot;
        this.provider = provider;
    }

    public void start() {
        nickname = getNickName();
        state = new ChatState(nickname);

        String message;
        while (true) {
            message = read();
            if (message.startsWith("/")) {
                parseCommand(message);
            } else {
                String response = bot.multisentenceRespond(message, state);
                state.newState(message, response);
                write(response);
            }
        }
    }

    private void parseCommand(final String command) {
        switch (command) {
            case ChatCommand.exit:
            case ChatCommand.quit:
                System.exit(0);
            case ChatCommand.stat:
                write(bot.getBrainStats());
                break;
            case ChatCommand.reload:
                bot.wakeUp();
                break;
            case "/connect russian":
            case "/c russian":
                bot.setName("russian");
                bot.wakeUp();
                break;
            case "/connect alice2":
            case "/c alice2":
                bot.setName("alice2");
                bot.wakeUp();
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
                String response = bot.multisentenceRespond(command, state);
                state.newState(command, response);
                write(response);
                break;
        }
    }

    private String read() {
        provider.write(nickname + ": ");
        String textLine = provider.read();
        textLine = textLine == null || textLine.isEmpty() ? AIMLConst.null_input : textLine.trim();
        return textLine;
    }

    private void write(String message) {
        provider.write(bot.getName() + ": " + message + "\n");
    }

    private String getNickName() {
        provider.write("Write your nickname: ");
        String nickname = provider.read();
        if (nickname == null)
            nickname = DEFAULT_NICKNAME;
        provider.write("Hello " + nickname + "! Welcome to chat with " + bot.getName() + ".\n");
        return nickname;
    }
}
