package com.batiaev.aiml.core;

import com.batiaev.aiml.chat.ChatState;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by anton on 22/06/15.
 * ---
 * Copyright © 2015. Anton Batiaev. All Rights Reserved.
 * www.batiaev.com
 */
public class BotTest extends Assert {

    @Ignore
    @Test
    public void testRespond() throws Exception {
        AIMLConst.setRootPath("./app-core");
        Bot bot = new Bot();
        String nickname = "Human";
        ChatState state = new ChatState(nickname);
        String request = "Как дела?";
        String correctRequest = "отлично";
        String respond = bot.respond(request, state);
        assertTrue("Request = " + request + ", Respond = " + respond, respond.equals(correctRequest));
    }
}
