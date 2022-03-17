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
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.scannedCodes = new ArrayList<QRCODE>();
    }

    /**
     * TODO: Temporary test method, generates some qr codes to display.
     * This code should be removed once the profile's scannedCodes list is
     * populated from the database, with the score updated accordingly!
     */
    public void generateTestCodes(){
        QRCODE qr1, qr2, qr3;
        qr1 = new QRCODE("BFG5DGW54");
        qr2 = new QRCODE("BFG5DGX23");
        qr3 = new QRCODE("UKR6LXA01");
        this.score += qr1.getScore() + qr2.getScore() + qr3.getScore();

        this.scannedCodes.add(qr1);
        this.scannedCodes.add(qr2);
        this.scannedCodes.add(qr3);
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
    public String getPhoneNumber() {
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
        
        scannedCodes.add(qrCode);
        addScore(qrCode.getScore());
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
     * this method will return the score value of the highest-scoring
     * QR code.
     * @return : integer score value
     */
    public int getHighestScore(){
        int max = 0;
        for (int i = 1; i < this.scannedCodes.size(); i++){
            if(this.scannedCodes.get(i).getScore() > max){
                max = this.scannedCodes.get(i).getScore();
            }
        }
        return max;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
