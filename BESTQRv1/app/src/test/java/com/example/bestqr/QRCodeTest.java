package com.example.bestqr;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;

import com.example.bestqr.Database.Storage;
import com.example.bestqr.models.BaseQRCode;
import com.example.bestqr.models.Comment;
import com.example.bestqr.models.Comments;
import com.example.bestqr.models.Location;
import com.example.bestqr.models.QRCODE;
import com.example.bestqr.models.TimeStamp;
import com.example.bestqr.utils.QRmethods;
import com.google.firebase.FirebaseApp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class QRCodeTest {

    public String contents = "BFG5DGW54";

    public TimeStamp mockTimeStamp(){
        return new TimeStamp();
    }

    public BaseQRCode mockBaseQRCode(){
        QRCODE qrcode = new QRCODE();
        qrcode.setObjectImage(Storage.download("eadf4636a1b8c634", QRmethods.calculateHash(contents)));
        Bitmap bitmap = qrcode.getBitmap();
        return new BaseQRCode(QRmethods.calculateHash(contents), bitmap);
    }

    public Comment mockComment(){
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return new Comment("This is a comment","c16acc52aa485b817f8c5c631fc0d031d19148717f732eba1a7a5970ec6331a1",
                DateFormat.format(new Date()) + " GMT +00:00");
    }

    public QRCODE mockQRCODE(){
        Location location = new Location(51.33465, 78.3574);
        return new QRCODE(location, "BFG5DGW54");
    }

    @Ignore
    void qRCodeTest(){
        FirebaseApp.initializeApp(FirebaseApp.getInstance().getApplicationContext());
        BaseQRCode baseQRCode = mockBaseQRCode();
        baseQRCode.setHash(QRmethods.calculateHash(contents));
        assertEquals("8227ad036b504e39fe29393ce170908be2b1ea636554488fa86de5d9d6cd2c32", baseQRCode.getHash());
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
        assertEquals("c16acc52aa485b817f8c5c631fc0d031d19148717f732eba1a7a5970ec6331a1", comment.getAndroidId());
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