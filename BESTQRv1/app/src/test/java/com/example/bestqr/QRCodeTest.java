package com.example.bestqr;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.bestqr.models.Comment;
import com.example.bestqr.models.Comments;
import com.example.bestqr.models.QRCODE;
import com.example.bestqr.models.TimeStamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class QRCodeTest {

    private Date dateObj = new Date();
    SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public TimeStamp mockTimeStamp(){
        return new TimeStamp();
    }

    public Comment mockComment(){
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return new Comment("This is a comment","368b2f968478511b",
                DateFormat.format(new Date()) + " GMT +00:00");
    }

    @Test
    void testComment(){
        TimeStamp timeStamp = mockTimeStamp();
        Comment comment = mockComment();

        assertEquals("This is a comment", comment.getContents());
        assertEquals("368b2f968478511b", comment.getAndroidId());
        assertEquals(timeStamp.getTimeStamp(), comment.getTimeStamp());
    }


}