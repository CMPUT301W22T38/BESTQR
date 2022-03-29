package com.example.bestqr.models;


import android.graphics.Bitmap;

import com.example.bestqr.QRCODE;
import com.example.bestqr.QRCodeList;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Profile extends BaseProfile {

    private QRCODE deviceQrCode;
    private int score = 0;
    private QRCodeList scannedCodes;

    private ArrayList<Integer> qrScores;
    private ArrayList<Bitmap> qrBitmaps;
    private ArrayList<String> qrTimestamps;

    /**
     * This constructor is used to initialize an empty Profile object within Database.java
     * @param androidId: android Id of the user
     */
    public Profile(String androidId) {
        super(androidId);
        this.scannedCodes = new QRCodeList();
    }

//    /**
//     * This constructor initializes the information of the user's profile
//     *
//     * @param user_name : The username of the user
//     * @param deviceQrCode: The unique QRCODE for the users profile that can be scanned by another device to log in
//     * @param phoneNumber : The phone number of user
//     * @param emailAddress: The email address of the user
//     */
//    public Profile(String user_name, QRCODE deviceQrCode, String phoneNumber, String emailAddress) {
//        this.userName = user_name;
//        this.deviceQrCode = deviceQrCode;
//        this.phoneNumber = phoneNumber;
//        this.emailAddress = emailAddress;
//        this.scannedCodes = new QRCodeList();
//    }

    // add constructors as needed

    public void setScannedCodes(QRCodeList qrcodes) {
        this.scannedCodes = qrcodes;
        // Need to iterate through qr codes to update profile's score correctly
        this.score = 0;
        for(QRCODE qr : qrcodes){
            this.score += qr.getScore();
        }
    }

    public QRCodeList getScannedCodes() {return  this.scannedCodes;}



    /**
     * This method returns the image of QRCODE for users to log in with
     * @return the unique image QRCODE for the users profile that can be scanned by another device to log in
     */
    public Bitmap getProfileCode(){
        return this.deviceQrCode.getCode();
    }


    /**
     * This method returns the total score of the user so far
     * @return the total score of the user so far
     */
    public int getScore() {
        return this.score;
    }

    /**
     * This method returns the number of scanned QR CODES
     * @return the number of scanned QR CODES
     */

    public void recountTotalScore(){
        this.score = 0;
        for(int i = 0; i < this.scannedCodes.size(); i++){
            this.score += this.scannedCodes.get(i).getScore();
        }
    }

    public int getNumberCodesScanned(){
        return this.scannedCodes.size();
    }

    /**
     * This method adds the QRCODE scanned by the user that is decided to be stored
     * @param qrCode: the QRCODE scanned by the user that is decided to be stored
     */
    public void addNewQRCode(QRCODE qrCode) {
        // the hash should be stored not the image itself
        // if geolocation is allowed the it should be shown on the map
        // also add it to the owner list of qrCodes if it does not exist there

        scannedCodes.add(qrCode);
        this.score += qrCode.getScore();
    }

    /**
     * this method adds to the score of the user when a new QRCODE is scanned and stored
     */
    public void addScore(){
        // this method adds to the user's points if he chooses to store the code
    }

    /**
     * This method returns the scores of all qrcodes scanned
     * @return: scores of all qrCodes scanned
     */
    public ArrayList<Integer> getQrScores(){
//        if (condition == "ascending"){
//            scannedCodes.ascendingSort();
//        }
//        else if (condition == "descending"){
//            scannedCodes.descendingSort();
//        }
//        else{
//            scannedCodes.chronologicalSort();
//        }

        qrScores = new ArrayList<>();
        for (QRCODE item:scannedCodes) {
            qrScores.add(item.getScore());
        }
        return qrScores;
    }

    /**
     * This method returns the Bitmaps of all qrcodes scanned
     * @return: Bitmaps of all qrCodes scanned
     */
    public ArrayList<Bitmap> getQrBitmaps(){
        qrBitmaps = new ArrayList<>();
        for (QRCODE item:scannedCodes) {
            qrBitmaps.add(item.getCode());
        }
        return qrBitmaps;
    }

    public ArrayList<String> getQrTimestamps() {
        qrTimestamps = new ArrayList<>();
        for (QRCODE item: scannedCodes) {
            qrTimestamps.add(item.getScannedTime());
        }
        return qrTimestamps;
    }


    /**
     * this method will return the score value of the highest-scoring
     * QR code.
     * @return : integer score value
     */
    public int getHighestScore(){
        return this.scannedCodes.getHighest().getScore();
    }

    /**
     * this method removes a QRCODE from the QR CODES scanned by the user and updates he score accordingly
     */
    public void removeCodebyPosition(int position){
        // also remove it from the owner list of qrCodes if it not exist there
        if(this.scannedCodes == null || position < 0 || position >= this.scannedCodes.size()){
            //avoid possible problem
        }else{
            scannedCodes.remove(position);
        }

    }

    public QRCODE getDeviceQrCode(){
        return new QRCODE(getAndroidid());
    }

}

