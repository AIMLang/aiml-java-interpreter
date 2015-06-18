package com.batiaev.aiml.chat;

import java.util.UUID;

/**
 * Created by anton on 18/06/15.
 * ---
 * Copyright © 2015. Anton Batiaev. All Rights Reserved.
 * www.batiaev.com
 */
public class ChatState {
    private UUID chatUid;
    private ChatHistory history;
    private String request = "";
    private String respond = "";
    private String topic = "";
    private String that = "";

    public ChatState(String userName) {
        chatUid = UUID.randomUUID();
        history = new ChatHistory(chatUid, userName);
    }

    public void newState(String request, String respond) {
        this.request = request;
        this.respond = respond;
        history.addRequest(this.request);
        history.addRespond(this.respond);
    }

    public String topic() {
        return topic;
    }

    public String that() {
        return that;
    }

    public String request() {
        return request;
    }

    public String respond() {
        return respond;
    }
}
