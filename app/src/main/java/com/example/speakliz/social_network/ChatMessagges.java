package com.example.speakliz.social_network;

import java.util.Date;

public class ChatMessagges {
    private String messaggeText;
    private  String userText;
    private long messaggeTime;

    public ChatMessagges(String messaggeText, String userText) {
        this.messaggeText = messaggeText;
        this.userText = userText;

        messaggeTime = new Date().getTime();

    }

    public ChatMessagges(String messaggeText) {
        this.messaggeText = messaggeText;
    }

    public String getMessaggeText() {
        return messaggeText;
    }

    public void setMessaggeText(String messaggeText) {
        this.messaggeText = messaggeText;
    }

    public String getUserText() {
        return userText;
    }

    public void setUserText(String userText) {
        this.userText = userText;
    }

    public long getMessaggeTime() {
        return messaggeTime;
    }

    public void setMessaggeTime(long messaggeTime) {
        this.messaggeTime = messaggeTime;
    }
}
