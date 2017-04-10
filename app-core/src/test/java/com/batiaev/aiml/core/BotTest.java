package com.batiaev.aiml.core;

import com.batiaev.aiml.bot.Bot;
import com.batiaev.aiml.bot.BotRepository;
import com.batiaev.aiml.chat.ChatContext;
import com.batiaev.aiml.consts.AimlConst;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by anbat on 22/06/15.
 *
 * @author anbat
 */
public class BotTest extends Assert {
    private Bot bot;

    @Before
    public void setUp() throws Exception {
        AimlConst.setRootPath(System.getProperty("user.dir") + "/aiml-bots/bots");
        bot = BotRepository.get();
        assertTrue(bot.wakeUp());
    }

    @Test
    public void testMultisentenceRespond() throws Exception {
        String request = "Как дела?";
        List<String> correctResponds = Arrays.asList("отлично", "восхитительно", "замечательно", "прекрасно", "превосходно", "изумительно");
        String respond = bot.multisentenceRespond(request, new ChatContext("Human")).trim();
        assertTrue("Request = " + request + ", Respond = " + respond, correctResponds.contains(respond));
    }

    @Test
    public void testMultisentenceRespondWithRandom() throws Exception {
        String request = "Привет";
        String respond = bot.multisentenceRespond(request, new ChatContext("Human")).trim();
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
        String respond = bot.multisentenceRespond(request, new ChatContext("Human")).trim();
        String[] answers = "Здравствуй;Здравствуйте;Мое почтение!;Здарова;Приветствую;Привет;Доброго времени суток".split(";");
        boolean result = false;
        for (String answer : answers) {
            if (respond.equals(answer))
                result = true;
        }
        assertTrue("Request = " + request + ", Respond = " + respond, result);
    }
}
