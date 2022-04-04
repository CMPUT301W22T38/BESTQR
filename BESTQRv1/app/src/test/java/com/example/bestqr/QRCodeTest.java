package com.example.bestqr;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.bestqr.models.Comment;
import com.example.bestqr.models.Comments;
import com.example.bestqr.models.Location;
import com.example.bestqr.models.QRCODE;
import com.example.bestqr.models.TimeStamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class QRCodeTest {

    public TimeStamp mockTimeStamp(){
        return new TimeStamp();
    }

    public Comment mockComment(){
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return new Comment("This is a comment","368b2f968478511b",
                DateFormat.format(new Date()) + " GMT +00:00");
    }

    public QRCODE mockQRCODE(){
        Location location = new Location(51.33465, 78.3574);
        return new QRCODE(location, "BFG5DGW54");
    }

    @Test
    /**
     * Test Comment Class and TimeStamp Class
     */
    void testComment(){
        TimeStamp timeStamp = mockTimeStamp();
        Comment comment = mockComment();
        // can get comments
        assertEquals("This is a comment", comment.getContents());
        // can get android id
        assertEquals("368b2f968478511b", comment.getAndroidId());
        // can get time stamp
        assertEquals(timeStamp.getTimeStamp(), comment.getTimeStamp());
    }

//    @Test
//    void testQRCODEComment(){
//        QRCODE qrcode = mockQRCODE();
//        Comment comment = mockComment();
//        qrcode.addComments(comment);
//    }
}