package com.younsukkoh.miscellaneous.mychat;

/**
 * Holds message content, time and user
 * Created by Younsuk on 3/5/2017.
 */

public class Message {

    // The user who sent the message
    String user;
    // Content of the message
    String content;
    // Time the message was sent
    long timeSent;

    // Needs to be here for Firebase POJO rule
    public Message() {
    }

    public Message(String user, String content, long timeSent) {
        setUser(user);
        setContent(content);
        setTimeSent(timeSent);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(long timeSent) {
        this.timeSent = timeSent;
    }
}
