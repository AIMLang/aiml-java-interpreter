package com.batiaev.aiml.core;

import com.batiaev.aiml.chat.ChatState;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by anton on 22/06/15.
 * ---
 * Copyright © 2015. Anton Batiaev. All Rights Reserved.
 * www.batiaev.com
 */
public class BotTest extends Assert {

    @Test
    public void testMultisentenceRespond() throws Exception {
        AIMLConst.setRootPath(".");
        Bot bot = new Bot();
        String request = "Как дела?";
        String correctRequest = "отлично";
        String respond = bot.multisentenceRespond(request, new ChatState("Human")).trim();
        assertTrue("Request = " + request + ", Respond = " + respond, respond.equals(correctRequest));
    }

    @Test
    public void testMultisentenceRespondWithRandom() throws Exception {
        AIMLConst.setRootPath(".");
        Bot bot = new Bot();
        String request = "Привет";
        String respond = bot.multisentenceRespond(request, new ChatState("Human")).trim();
        String[] answers = "Здравствуй;Здравствуйте;Мое почтение!;Здарова;Приветствую;Привет;Доброго времени суток".split(";");
        boolean result = false;
        for (String answer : answers) {
            if (respond.equals(answer))
                result = true;
        }
        assertTrue("Request = " + request + ", Respond = " + respond, result);
    }

    @Test
    public void testMultisentenceRespondWithSrai() throws Exception {
        AIMLConst.setRootPath(".");
        Bot bot = new Bot();
        String request = "Здравствуй";
        String respond = bot.multisentenceRespond(request, new ChatState("Human")).trim();
        String[] answers = "Здравствуй;Здравствуйте;Мое почтение!;Здарова;Приветствую;Привет;Доброго времени суток".split(";");
        boolean result = false;
        for (String answer : answers) {
            if (respond.equals(answer))
                result = true;
        }
        assertTrue("Request = " + request + ", Respond = " + respond, result);
    }
}
