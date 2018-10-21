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
    public void testTopic() {
        ChatContext state = new ChatContext("test");
        assertEquals("Default Topic = unknown, result = " + state.topic(), state.topic(), AimlConst.default_topic);
        String newTopic = "new topic";
        state.setTopic(newTopic);
        assertEquals("Topic = " + newTopic + ", result = " + state.topic(), state.topic(), newTopic);
    }

    @Test
    public void testThat() {
        ChatContext state = new ChatContext("test");
        assertEquals("Default That = unknown, result = " + state.that(), state.that(), AimlConst.default_that);
        String newThat = "new topic";
        state.setTopic(newThat);
        assertEquals("That = " + newThat + ", result = " + state.topic(), state.topic(), newThat);
    }

    @Test
    public void testNewState() {
        ChatContext state = new ChatContext("test");
        String testRequest = "test request";
        String testRespond = "test respond";
        state.newState(testRequest, testRespond);
        assertTrue("New state {request = " + testRequest + ", respond = " + testRespond + "}, result: {request = "
                        + state.request() + ", respond = " + state.respond() + "}",
                state.request().equals(testRequest) && state.respond().equals(testRespond));
    }

    @Test
    public void testRequest() {
        ChatContext state = new ChatContext("test");
        assertEquals("Default Request = \"\", result = " + state.request(), "", state.request());
        String newRequest = "new Request";
        state.setRequest(newRequest);
        assertEquals("Request = " + newRequest + ", result = " + state.request(), state.request(), newRequest);
    }

    @Test
    public void testRespond() {
        ChatContext state = new ChatContext("test");
        assertEquals("Default Respond = unknown, result = " + state.respond(), state.respond(), AimlConst.default_that);
        String newRespond = "new Respond";
        state.setRespond(newRespond);
        assertEquals("Respond = " + newRespond + ", result = " + state.respond(), state.respond(), newRespond);
    }

    @Test
    public void testSetTopic() {
        ChatContext state = new ChatContext("test");
        String newTopic = "new topic";
        state.setTopic(newTopic);
        assertEquals("Set topic = " + newTopic + ", result = " + state.topic(), state.topic(), newTopic);
    }

    @Test
    public void testSetRespond() {
        ChatContext state = new ChatContext("test");
        String newRespond = "new respond";
        state.setRespond(newRespond);
        assertEquals("Set respond = " + newRespond + ", result = " + state.respond(), state.respond(), newRespond);
    }

    @Test
    public void testSetRequest() {
        ChatContext state = new ChatContext("test");
        String newRequest = "new request";
        state.setRequest(newRequest);
        assertEquals("Set request = " + newRequest + ", result = " + state.request(), state.request(), newRequest);
    }
}
