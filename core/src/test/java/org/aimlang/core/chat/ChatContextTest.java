package org.aimlang.core.chat;

import org.aimlang.core.consts.AimlConst;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ChatContextTest
 *
 * @author batiaev
 * @since 6/22/15
 */
public class ChatContextTest {

    @Test
    public void testTopic() {
        var state = new ChatContext("test");
        assertEquals(state.topic(), AimlConst.default_topic, "Default Topic = unknown, result = " + state.topic());
        var newTopic = "new topic";
        state.setTopic(newTopic);
        assertEquals(state.topic(), newTopic, "Topic = " + newTopic + ", result = " + state.topic());
    }

    @Test
    public void testThat() {
        var state = new ChatContext("test");
        assertEquals(state.that(), AimlConst.default_that, "Default That = unknown, result = " + state.that());
        var newThat = "new topic";
        state.setTopic(newThat);
        assertEquals(state.topic(), newThat, "That = " + newThat + ", result = " + state.topic());
    }

    @Test
    public void testNewState() {
        var state = new ChatContext("test");
        var testRequest = "test request";
        var testRespond = "test respond";
        state.newState(testRequest, testRespond);
        assertTrue(state.request().equals(testRequest) && state.respond().equals(testRespond),
                format("New state {request = %s, respond = %s}, result: {request = %s, respond = %s}",
                        testRequest, testRespond, state.request(), state.respond()));
    }

    @Test
    public void testRequest() {
        var state = new ChatContext("test");
        assertEquals("", state.request(), "Default Request = \"\", result = " + state.request());
        var newRequest = "new Request";
        state.setRequest(newRequest);
        assertEquals(state.request(), newRequest, "Request = " + newRequest + ", result = " + state.request());
    }

    @Test
    public void testRespond() {
        var state = new ChatContext("test");
        assertEquals(state.respond(), AimlConst.default_that, "Default Respond = unknown, result = " + state.respond());
        var newRespond = "new Respond";
        state.setRespond(newRespond);
        assertEquals(state.respond(), newRespond, "Respond = " + newRespond + ", result = " + state.respond());
    }

    @Test
    public void testSetTopic() {
        var state = new ChatContext("test");
        var newTopic = "new topic";
        state.setTopic(newTopic);
        assertEquals(state.topic(), newTopic, "Set topic = " + newTopic + ", result = " + state.topic());
    }

    @Test
    public void testSetRespond() {
        var state = new ChatContext("test");
        var newRespond = "new respond";
        state.setRespond(newRespond);
        assertEquals(state.respond(), newRespond, "Set respond = " + newRespond + ", result = " + state.respond());
    }

    @Test
    public void testSetRequest() {
        var state = new ChatContext("test");
        var newRequest = "new request";
        state.setRequest(newRequest);
        assertEquals(state.request(), newRequest, "Set request = " + newRequest + ", result = " + state.request());
    }
}
