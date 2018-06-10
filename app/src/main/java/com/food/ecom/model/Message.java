package com.food.ecom.model;

/**
 * Created by ThetaTeam2 on 15/05/2018.
 */

public class Message {
    String to;
    NotifyData notification;
    String message_id;

    public Message(String to, NotifyData notification, String message_id) {
        this.to = to;
        this.notification = notification;
        this.message_id = message_id;
    }

    public String getMessage_id() {

        return message_id;
    }
}
