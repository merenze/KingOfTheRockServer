package com.example.frontend.SupportingClasses;

import com.example.frontend.MemberData;

public class Message {
    private String text;
    private MemberData memberData;
    private boolean myMessage;

    public Message(String text, boolean myMessage) {
        this.text = text;
        this.myMessage = myMessage;
    }

    public String getText() {
        return text;
    }

    public MemberData getMemberData() {
        return memberData;
    }

    public boolean isMyMessage() {
        return myMessage;
    }

}
