package com.batiaev.aiml.core;

import com.batiaev.aiml.bot.Bot;
import com.batiaev.aiml.bot.BotRepository;
import com.batiaev.aiml.chat.ChatState;
import com.batiaev.aiml.consts.AIMLConst;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by anbat on 22/06/15.
 *
 * @author anbat
 */
public class BotTest extends Assert {
    private Bot bot;

    @Before
    public void setUp() throws Exception {
        AIMLConst.setRootPath(System.getProperty("user.dir") + "/app-core/aiml-bots/bots");
        bot = BotRepository.get();
        assertTrue(bot.wakeUp());
    }

    @Test
    public void testMultisentenceRespond() throws Exception {
        String request = "Как дела?";
        String correctRequest = "отлично";
        String respond = bot.multisentenceRespond(request, new ChatState("Human")).trim();
        assertTrue("Request = " + request + ", Respond = " + respond, respond.equals(correctRequest));
    }

    @Test
    public void testMultisentenceRespondWithRandom() throws Exception {
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
