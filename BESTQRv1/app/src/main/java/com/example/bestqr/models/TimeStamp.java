package com.example.bestqr.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeStamp implements Serializable {
    private String timestamp;


    public TimeStamp () {
        this.setTimeStamp();
    }

    /**
     * get the timestamp
     * @return Timestamp object
     */
    public String getTimeStamp() {
        return this.timestamp;
    }

    /**
     * set the timestamp
     * @param -  the Timestamp object
     */
    public void setTimeStamp() {
        this.timestamp = currentTime();
    }

    /**
     * set the timestamp
     * @param -  the string Timestamp object
     */
    public void setTimeStamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * gets the current time
     * @return the current time in string
     */
    static public String currentTime() {
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return DateFormat.format(new Date()) + " GMT +00:00";
    }
}
