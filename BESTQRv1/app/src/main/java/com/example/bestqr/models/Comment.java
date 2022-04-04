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

    /**
     * gets the content of the comment
     * @return - The string representation of the comment
     */
    public String getContents() {
        return contents;
    }

    /**
     * gets the android id of the user who added the comment
     * @return - the android id of the user who added the comment
     */
    public String getAndroidId() {
        return androidId;
    }

    /**
     * The time when the comment was created
     * @return -  the time when the comment  was added
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * converts the comment to a hashmap that will be stored in database
     * @return - the hashMap object that contains the info of the user
     */
    public HashMap toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("contents", this.contents);
        result.put("writtenby", this.androidId);
        result.put("createdat", this.timeStamp);

        return result;
    }
}
