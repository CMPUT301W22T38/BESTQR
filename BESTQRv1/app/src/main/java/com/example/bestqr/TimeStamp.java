package com.example.bestqr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeStamp {

    private String timestamp;

    public TimeStamp () {
        this.setTimeStamp();
    }

    public String getTimeStamp() {
        return this.timestamp;
    }

    public void setTimeStamp() {
        this.timestamp = currentTime();
    }

    public void setTimeStamp(String timestamp) {
        this.timestamp = timestamp;
    }

    static public String currentTime() {
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return DateFormat.format(new Date()) + " GMT +00:00";
    }

}
