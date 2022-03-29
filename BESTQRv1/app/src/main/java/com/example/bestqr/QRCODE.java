package com.example.bestqr;

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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class QRCODE implements Serializable {
    private Location codeLocation;
    private Bitmap bitmap;
    private String hash;
    private boolean isimported;
    private String contents;
    private int score;
    private Bitmap objectImage;
    static MessageDigest digest;
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
        this.contents = contents;
        this.codeLocation = codeLocation;
        this.hash = QRmethods.calculateHash(contents);
        this.score = QRmethods.calculateScore(hash);
        this.timestamp = new TimeStamp();
        this.comments = new ArrayList<>();
    }


    /**
     * This constructor is for the QRCODE where location is stored
     * @param contents: this is the content of the QRCODE
     */
    public QRCODE(String contents) {
        // this enforces the privacy rule for user location
        this.contents = contents;
        this.codeLocation = null;
        this.hash = QRmethods.calculateHash(contents);
        this.score = QRmethods.calculateScore(hash);
        this.timestamp = new TimeStamp();
        this.comments = new ArrayList<>();
    }

    //test
    public void setBitmap(Bitmap bitmap) {this.bitmap = bitmap;}

    public void setHash(String hash) {
        this.hash = hash;
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
     * This method generates a QRCODE for the content passed in
     * @return: A Bitmap object that contains the image of the QRCODE
     */
    public Bitmap getCode(){
        MultiFormatWriter writer = new MultiFormatWriter();
            try {
                BitMatrix matrix = writer.encode(hash, BarcodeFormat.QR_CODE, 350, 350);
                BarcodeEncoder encoder = new BarcodeEncoder();
                bitmap = encoder.createBitmap(matrix);
            } catch (WriterException e) {
                e.printStackTrace();
            }

        return bitmap;

    }

    /**
     * This method gets the hash of the QRCODE
     * @return: A string object that contains the hash in Hexadecimal of the QRCODE
     */
    public String getHash(){
        return hash;
    }

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