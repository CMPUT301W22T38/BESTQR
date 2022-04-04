package com.example.bestqr.models;


import android.graphics.Bitmap;

import com.example.bestqr.Database.Database;
import com.example.bestqr.QRCodeList;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;

public class Profile extends BaseProfile  {

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

    /**
     * Change the phonenumber of the user
     * @param phoneNumber - the new phone number to be applied
     */
    public void ChangePhoneNumber(String phoneNumber) {
        if (Database.ChangeUserInfo("phonenumber", this.getAndroidId(), this.getPhoneNumber(), phoneNumber)) {
            super.setPhoneNumber(phoneNumber);
        }
    }

    /**
     * Change the user name of the user
     * @param userName - the new username to be applied
     */
    public void ChangeUserName(String userName) {
        if (Database.ChangeUserInfo("username", this.getAndroidId(), this.getUserName(), userName)) {
            super.setUserName(userName);
        }
    }

    /**
     * Change the emailAddress of the user
     * @param emailAddress- the new emailAddress to be applied
     */
    public void ChangeEmailAddress(String emailAddress) {
        //FirebaseApp.initializeApp(FirebaseApp.getInstance().getApplicationContext());
        if (Database.ChangeUserInfo("emailaddress", this.getAndroidId(), this.getEmailAddress(), emailAddress)) {
            super.setEmailAddress(emailAddress);
        }
    }

    /**
     * set scanned codes
     * @param qrcodes - bulk insert a number of qrcodes
     */
    public void setScannedCodes(QRCodeList qrcodes) {
        this.scannedCodes = qrcodes;
    }

    /**
     * gets the scanned codes of this profile
     * @return - the scanned codes of this user
     */
    public QRCodeList getScannedCodes() {return  this.scannedCodes;}

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
    public int getNumberCodesScanned(){
        return this.scannedCodes.size();
    }

    /**
     * This method adds the QRCODE scanned by the user that is decided to be stored
     * @param qrCode: the QRCODE scanned by the user that is decided to be stored
     */
    public void addNewQRCode(QRCODE qrCode) {
        if (Database.addQRCode(this.getAndroidId(), qrCode)) {
            this.scannedCodes.add(qrCode);
            this.score += qrCode.getScore();

        }
    }


    /**
     * This method returns the scores of all qrcodes scanned
     * @return: scores of all qrCodes scanned
     */
    public ArrayList<Integer> getQrScores(){
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
            if (item.getObjectImage() != null) {
                qrBitmaps.add(item.getObjectImage());
            }else{
                qrBitmaps.add(item.getBitmap());
            }

        }
        return qrBitmaps;
    }

    /**
     * This method gets tht timestamps of all qrcodes scanned by this profile
     * @return the timestamps of all qrCodes scanned
     */
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
        if (!this.scannedCodes.isEmpty()) {
            return this.scannedCodes.getHighest().getScore();
        }
        else {
            return 0;
        }
    }

    /**
     * this method removes a QRCODE from the QR CODES scanned by the user and updates he score accordingly
     * @param :   the position of the code to be removed
     */
    public void removeCodebyPosition(int position){
        // also remove it from the owner list of qrCodes if it not exist there

        if (Database.removeQrCode(getAndroidId(),this.scannedCodes.get(position))) {
            if (this.scannedCodes == null || position < 0 || position >= this.scannedCodes.size()) {
                //avoid possible problem
            } else {
                scannedCodes.remove(position);
            }
        }

    }

    /**
     * Get the unique identification QRCode for this profile
     * @return - the QRCode of this profile
     */
    public QRCODE getDeviceQrCode(){
        return new QRCODE(getAndroidId());
    }

    /**
     * Set the unique identification QRCode for this profile
     * @param  - the QRCod to be applied
     */
    public QRCODE getUserNameQrCode(){
        return new QRCODE(getUserName());
    }

}

