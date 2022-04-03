package com.example.bestqr.models;

public class Comment {
    private String contents;
    private String androidId;
    private TimeStamp timeStamp;

    public Comment(String contents, String androidId) {
        this.contents = contents;
        this.androidId = androidId;
        this.timeStamp = new TimeStamp();
    }

    public String getContents() {
        return contents;
    }

    public String getAndroidId() {
        return androidId;
    }

    public TimeStamp getTimeStamp() {
        return timeStamp;
    }
}
