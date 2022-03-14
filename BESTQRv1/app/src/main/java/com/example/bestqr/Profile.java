package com.example.bestqr;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;

public class Profile {
    private String androidId;
    private String userName;
    private String phoneNumber;
    private String emailAddress;
    private QRCODE deviceQrCode;
    private int score;
    private ArrayList<QRCODE> scannedCodes;

    public Profile(String android_id) {
        this.androidId = android_id;
    }

    /**
     * This constructor initializes the information of the user's profile
     *
     * @param user_name : The username of the user
     * @param deviceQrCode: The unique QRCODE for the users profile that can be scanned by another device to log in
     * @param phoneNumber : The phone number of user
     * @param emailAddress: The email address of the user
     */
    public Profile(String user_name, QRCODE deviceQrCode, String phoneNumber, String emailAddress) {
        this.userName = user_name;
        this.deviceQrCode = deviceQrCode;
    }

    /**
     * This class returns the unique device id for users  log in with
     * @return the unique device id of a profile
     */
    public String getDeviceID(){
        return deviceQrCode.getHash();
    }

    /**
     * This method returns the image of QRCODE for users to log in with
     * @return the unique image QRCODE for the users profile that can be scanned by another device to log in
     */
    public Bitmap getProfileCode(){
        return deviceQrCode.getCode();
    }


    /**
     * This method returns the username of the user
     * @return : the username of the user
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method returns the phone number of the user
     * @return : the phone number of the user
     */
    public int getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * This method returns the email address of the user
     * @return : the email address of the user
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * This method returns the total score of the user so far
     * @return the total score of the user so far
     */
    public int getScore() {
        return score;
    }

    /**
     * This method returns the number of scanned QR CODES
     * @return the number of scanned QR CODES
     */
    public int getNumberCodesScanned(){
        return scannedCodes.size();
    }

    /**
     * This method adds the QRCODE scanned by the user that is decided to be stored
     * @param qrCode: the QRCODE scanned by the user that is decided to be stored
     */
    public void addNewQRCode(QRCODE qrCode) {
        // the hash should be stored not the image itself
        // if geolocation is allowed the it should be shown on the map
        // also add it to the owner list of qrCodes if it does not exist there
    }

    /**
     * this method adds to the score of the user when a new QRCODE is scanned and stored
     */
    public void addScore(){
        // this method adds to the user's points if he chooses to store the code
    }

    /**
     * this method displays all the QR CODES that the user has scanned and stored
     */
    public void displayQrCodes(){
        //displays all the qr codes the user has stored
    }

    /**
     * this method removes a QRCODE from the QR CODES scanned by the user and updates he score accordingly
     * @param qrCode - the QRCODE to be removed
     */
    public void removeCode(QRCODE qrCode){
        // also remove it from the owner list of qrCodes if it not exist there
        scannedCodes.remove(qrCode);
        score -= qrCode.getScore();
    }

}