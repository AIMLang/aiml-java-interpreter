package com.batiaev.aiml.chat;

import com.batiaev.aiml.core.AIMLConst;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by anbat on 18/06/15.
 *
 * @author anbat
 * @author Marco Piovesan
 *         Added predicates on 29/08/16
 */
public class ChatState {
    private UUID chatUid;
    private ChatHistory history;
    private String request = "";
    private String topic = AIMLConst.default_topic;
    private String that = AIMLConst.default_that;
    private HashMap<String, String> predicates = new HashMap<>();


    public ChatState(String userName) {
        chatUid = UUID.randomUUID();
        history = new ChatHistory(chatUid, userName);
    }

    public void newState(String request, String respond) {
        this.request = request;
        this.that = respond;
        history.addRequest(this.request);
        history.addRespond(this.that);
    }

    public String topic() {
        if (predicates.containsKey("topic")) {
            setTopic(predicates.get("topic"));
        }
        return topic;
    }

    public String that() {
        return that;
    }

    public String request() {
        return request;
    }

    public String respond() {
        return that;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setRespond(String respond) {
        this.that = respond;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public HashMap<String, String> getPredicates() {
        return predicates;
    }

}
