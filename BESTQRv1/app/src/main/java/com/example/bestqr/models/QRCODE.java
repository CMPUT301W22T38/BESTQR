package com.example.bestqr.models;

import android.graphics.Bitmap;

import com.example.bestqr.Database.Database;
import com.example.bestqr.models.TimeStamp;
import com.example.bestqr.utils.QRmethods;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
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
    private Comments comments = new Comments();
    private TimeStamp timestamp;
    private String geoHash;
    private LatLng latLng;

    public QRCODE() {
        this.codeLocation = null;
//        this.comments = new ArrayList<>();
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
//        this.comments = new ArrayList<>();
        this.geoHash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(codeLocation.getLatitude(), codeLocation.getLongitude()));
    }

    public QRCODE(String hash, Bitmap bitmap) {
        super(hash, bitmap);
//        this.score = QRmethods.calculateScore(this.getHash());
        this.timestamp = new TimeStamp();
//        this.comments = new ArrayList<>();
    }


    /**
     * This constructor is for the QRCODE where location is stored
     * @param contents: this is the content of the QRCODE
     */
    public QRCODE(String contents) {
        super(contents);
        this.score = QRmethods.calculateScore(this.getHash());
        this.timestamp = new TimeStamp();
//        this.comments = new ArrayList<>();
    }


    public void setScore(int score) {
        this.score = score;
    }

    public void setTimestamp(TimeStamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp.setTimeStamp(timestamp);
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


    public void addComments(Comment comment){
        comments.add(comment);
    }


    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }

    // for testing
    public void setCodeLocation(Location location) {
        this.codeLocation = location;
    }

}