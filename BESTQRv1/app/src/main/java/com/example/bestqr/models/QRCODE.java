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
        this.geoHash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(codeLocation.getLatitude(), codeLocation.getLongitude()));
    }

    public QRCODE(String hash, Bitmap bitmap) {
        super(hash, bitmap);
        this.timestamp = new TimeStamp();
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

    /**
     * sets the score of the qrcode
     * @param score the scor to be applied
     */

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * sets the timestamp of the qrcode
     * @param timestamp - the timestamp Object to be applied
     */
    public void setTimestamp(TimeStamp timestamp) {
        this.timestamp = timestamp;
    }
    /**
     * sets the timestamp of the qrcode
     * @param timestamp - the String timestamp Object to be applied
     */
    public void setTimestamp(String timestamp) {
        this.timestamp.setTimeStamp(timestamp);
    }

    /**
     * Sets isImported to be a value
     * @param imported - the value to set is imported
     */
    public void setisImported(boolean imported) {
        this.isimported = imported;
    }
    /**
     * Gets isimported to be a value
     * @return - the value that is set to isImported
     */
    public boolean getisImported() {
        return this.isimported;
    }


    /**
     * Gets the bitmap of the picture of the qrcode's location or the object it was scanned from
     * @return - The bitmap of the picture of the qrcode's location or the object it was scanned from
     */
    public Bitmap getObjectImage() {
        return objectImage;
    }

    /**
     * Sets the bitmap of the picture of the qrcode's location or the object it was scanned from
     * @param  - The bitmap of the picture of the qrcode's location or the object it was scanned from
     */
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

        return codeLocation;
    }


    /**
     * Adds a comment to the qrcode
     * @param comment - the comment object to be added
     */
    public void addComments(Comment comment){
        comments.add(comment);
    }

    /**
     * gets the comments of the qrcode
     * @return - the comments object of the qrCode
     */
    public Comments getComments() {
        return comments;
    }

    /**
     * Bulk insert comments into the qrcode
     * @param comments - the comments object to be inserted
     */
    public void setComments(Comments comments) {
        this.comments = comments;
    }


    /**
     * sets the location of th qrcode
     * @param location - the location of the qrcode to be applied
     */
    public void setCodeLocation(Location location) {
        // for testing
        this.codeLocation = location;
    }

}