package com.example.bestqr;


import android.graphics.Bitmap;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Profile{
    private String androidId;
    private String userName;
    private String phoneNumber;
    private String emailAddress;
    private QRCODE deviceQrCode;
    private int score = 0;
    private ArrayList<QRCODE> scannedCodes;

    private ArrayList<Integer> qrScores;
    private ArrayList<Bitmap> qrBitmaps;

    public Profile(String android_id) {
        this.androidId = android_id;
        this.scannedCodes = new ArrayList<QRCODE>();
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

    public void setScannedCodes(ArrayList<QRCODE> qrcodes) {
        this.scannedCodes = qrcodes;
//        this.scannedCodes.addAll(qrcodes);
    }

    public ArrayList<QRCODE> getScannedCodes() {
        return this.scannedCodes;
    }


    public String getAndroidID() { return this.androidId;}

    /**
     * This class returns the unique device id for users  log in with
     * @return the unique device id of a profile
     */
    public String getDeviceID(){
        return this.deviceQrCode.getHash();
    }

    /**
     * This method returns the image of QRCODE for users to log in with
     * @return the unique image QRCODE for the users profile that can be scanned by another device to log in
     */
    public Bitmap getProfileCode(){
        return this.deviceQrCode.getCode();
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
    public ArrayList<Integer> getQrScores(String condition){
        if (condition != "chronological") {
            if (condition == "descending") {
                Collections.sort(scannedCodes, new Comparator<QRCODE>() {
                    @Override
                    public int compare(QRCODE qrcode, QRCODE t1) {
                        return t1.getScore() - (qrcode.getScore());
                    }
                });
            } else if (condition == "ascending") {
                Collections.sort(scannedCodes, new Comparator<QRCODE>() {
                    @Override
                    public int compare(QRCODE qrcode, QRCODE t1) {
                        return qrcode.getScore() - t1.getScore();
                    }
                });
            }
            qrScores = new ArrayList<>();
            for (QRCODE item:scannedCodes) {
                qrScores.add(item.getScore());
            }
        }

        return qrScores;
    }

    /**
     * This method returns the Bitmaps of all qrcodes scanned
     * @return: Bitmaps of all qrCodes scanned
     */
    public ArrayList<Bitmap> getQrBitmaps(String condition){
        qrBitmaps = new ArrayList<>();
        for (QRCODE item : scannedCodes) {
            qrBitmaps.add(item.getCode());
        }

        return qrBitmaps;
    }


    /**
     * this method will return the score value of the highest-scoring
     * QR code.
     * @return : integer score value
     */
    public int getHighestScore(){
        int max = 0;
        for (int i = 0; i < this.scannedCodes.size(); i++){
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