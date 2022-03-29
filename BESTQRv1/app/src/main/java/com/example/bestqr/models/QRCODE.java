package com.example.bestqr.models;

import android.graphics.Bitmap;
import android.location.Location;

import com.example.bestqr.models.TimeStamp;
import com.example.bestqr.utils.QRmethods;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;


public class QRCODE extends BaseQRCode {
    private Location codeLocation;
    private boolean isimported;
    private int score;
    private Bitmap objectImage;
    private ArrayList<String> comments;
    private TimeStamp timestamp;

    public QRCODE() {
        this.comments = new ArrayList<>();
    }
    /**
     * This constructor is for the QRCODE where location is stored
     * @param codeLocation: This is the Location that is recorded for the QRCODE
     * @param contents: this is the content of the QRCODE
     */
    public QRCODE(Location codeLocation, String contents) {
        super(contents);
        this.codeLocation = codeLocation;
        this.score = QRmethods.calculateScore(this.getHash());
        this.timestamp = new TimeStamp();
        this.comments = new ArrayList<>();
    }


    /**
     * This constructor is for the QRCODE where location is stored
     * @param contents: this is the content of the QRCODE
     */
    public QRCODE(String contents) {
        super(contents);
        this.score = QRmethods.calculateScore(this.getHash());
        this.timestamp = new TimeStamp();
        this.comments = new ArrayList<>();
    }


    public void setScore(int score) {
        this.score = score;
    }

    public void setTimestamp(TimeStamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setisImported(boolean imported) {
        this.isimported = imported;
    }

    public boolean getisImported() {
        return this.isimported;
    }


    public Bitmap getObjectImage() {
        return objectImage;
    }

    public void setObjectImage(Bitmap objectImage) {
        this.objectImage = objectImage;
    }

    // testing
    public String getScannedTime() {return this.timestamp.getTimeStamp();}


    /**
     * This method returns the score of the QRCODE
     * @return :
     */
    public int getScore(){
        return score;
    }

    /**
     * This method returns the location of the QRCODE
     * @return the location of the QRCODE
     */
    public Location getCodeLocation(){
//        Location location = new Location("dummyprovider");
//        location.setLatitude(89.909090);
//        location.setLongitude(89.909090);
//        return location;
        return codeLocation;
    }


    public void addComments(String comment){
        comments.add(comment);
    }


    public ArrayList<String> getComments() {
        return comments;
    }

    // for testing
    public void setCodeLocation(Location location) {
        this.codeLocation = location;
    }


}