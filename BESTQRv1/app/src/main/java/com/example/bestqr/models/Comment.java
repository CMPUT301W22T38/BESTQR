package com.example.bestqr.models;

import java.util.HashMap;

public class Comment {
    private String contents;
    private String androidId;
    private String timeStamp;

    // used when retrieveing a deleted comment object from db//
    public Comment() {
        this.contents = "deleted";
        this.androidId = "unknown";
        this.timeStamp = "unknown";
    }

    public Comment(String contents, String androidId) {
        this.contents = contents;
        this.androidId = androidId;
        this.timeStamp = TimeStamp.currentTime();
    }

    // used when retrieving from db //
    public Comment(String contents, String androidId, String timeStamp) {
        this.contents = contents;
        this.androidId = androidId;
        this.timeStamp = timeStamp;
    }

    public String getContents() {
        return contents;
    }

    public String getAndroidId() {
        return androidId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public HashMap toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("contents", this.contents);
        result.put("writtenby", this.androidId);
        result.put("createdat", this.timeStamp);

        return result;
    }
}
