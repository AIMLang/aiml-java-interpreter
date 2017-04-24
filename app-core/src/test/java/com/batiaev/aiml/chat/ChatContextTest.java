package com.batiaev.aiml.chat;

import com.batiaev.aiml.consts.AimlConst;
import org.junit.Assert;
import org.junit.Test;

/**
 * ChatContextTest
 *
 * @author batiaev
 * @since 6/22/15
 */
public class ChatContextTest extends Assert {

    @Test
    public void testTopic() throws Exception {
        ChatContext state = new ChatContext("test");
        assertTrue("Default Topic = unknown, result = " + state.topic(), state.topic().equals(AimlConst.default_topic));
        String newTopic = "new topic";
        state.setTopic(newTopic);
        assertTrue("Topic = " + newTopic + ", result = " + state.topic(), state.topic().equals(newTopic));
    }

    @Test
    public void testThat() throws Exception {
        ChatContext state = new ChatContext("test");
        assertTrue("Default That = unknown, result = " + state.that(), state.that().equals(AimlConst.default_that));
        String newThat = "new topic";
        state.setTopic(newThat);
        assertTrue("That = " + newThat + ", result = " + state.topic(), state.topic().equals(newThat));
    }

    @Test
    public void testNewState() throws Exception {
        ChatContext state = new ChatContext("test");
        String testRequest = "test request";
        String testRespond = "test respond";
        state.newState(testRequest, testRespond);
        assertTrue("New state {request = " + testRequest + ", respond = " + testRespond + "}, result: {request = "
                        + state.request() + ", respond = " + state.respond() + "}",
                state.request().equals(testRequest) && state.respond().equals(testRespond));
    }

    @Test
    public void testRequest() throws Exception {
        ChatContext state = new ChatContext("test");
        assertTrue("Default Request = \"\", result = " + state.request(), state.request().equals(""));
        String newRequest = "new Request";
        state.setRequest(newRequest);
        assertTrue("Request = " + newRequest + ", result = " + state.request(), state.request().equals(newRequest));
    }

    @Test
    public void testRespond() throws Exception {
        ChatContext state = new ChatContext("test");
        assertTrue("Default Respond = unknown, result = " + state.respond(), state.respond().equals(AimlConst.default_that));
        String newRespond = "new Respond";
        state.setRespond(newRespond);
        assertTrue("Respond = " + newRespond + ", result = " + state.respond(), state.respond().equals(newRespond));
    }

    @Test
    public void testSetTopic() throws Exception {
        ChatContext state = new ChatContext("test");
        String newTopic = "new topic";
        state.setTopic(newTopic);
        assertTrue("Set topic = " + newTopic + ", result = " + state.topic(), state.topic().equals(newTopic));
    }

    @Test
    public void testSetRespond() throws Exception {
        ChatContext state = new ChatContext("test");
        String newRespond = "new respond";
        state.setRespond(newRespond);
        assertTrue("Set respond = " + newRespond + ", result = " + state.respond(), state.respond().equals(newRespond));
    }

    @Test
    public void testSetRequest() throws Exception {
        ChatContext state = new ChatContext("test");
        String newRequest = "new request";
        state.setRequest(newRequest);
        assertTrue("Set request = " + newRequest + ", result = " + state.request(), state.request().equals(newRequest));
    }
}
