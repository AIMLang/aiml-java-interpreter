package com.batiaev.aiml.chat;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * ChatHistory
 *
 * @author batiaev
 * @since 18/06/15
 */
public class ChatHistory {
    private final UUID chatUid;
    private final Date startDate;
    private final String userName;
    private ArrayList<String> requests;
    private ArrayList<String> responds;

    public ChatHistory(UUID chatUid, String user) {
        this.chatUid = chatUid;
        startDate = new Date();
        userName = user;
        requests = new ArrayList<>();
        responds = new ArrayList<>();
    }

    public boolean addRequest(String record) {
        return requests.add(record);
    }

    public boolean addRespond(String record) {
        return responds.add(record);
    }

    public String getRequest(int index) {
        return requests.get(index);
    }

    public String getRespond(int index) {
        return responds.get(index);
    }
}
