package com.example.bestqr;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Profile {
    private String user_name;
    private int phoneNumber;
    private String emailAddress;
    private QR_CODE deviceQrCode;
    private int score;
    private ArrayList<QR_CODE> scannedCodes;


    public Profile(String user_name, QR_CODE deviceQrCode2,int phoneNumber,String emailAddress) {
        this.user_name = user_name;
        this.deviceQrCode = deviceQrCode2;
    }


    public String getDeviceID(){
        return deviceQrCode.getHash();
    }

    public Bitmap getProfileCode(){
        return deviceQrCode.getCode();
    }

    public int getScore() {
        return score;
    }

    public int getNumberCodesScanned(){
        return scannedCodes.size();
    }

    public void getDeviceQRCode(){
        // the unique qrcode for the profile that can be used to log into another account
        // ideally the hash should be the unique username
    }

    public void addNewQRCode(QR_CODE qrCode) {
        // the hash should be stored not the image itself
        // if geolocation is allowed the it should be shown on the map
        // also add it to the owner list of qrCodes if it does not exist there
    }

    public void addScore(){
        // this method adds to the user's points if he chooses to store the code
    }

    public void displayQrCodes(){
        //displays all the qr codes the user has stored
    }

    public void removeCode(QR_CODE qrCode){
        // also remove it from the owner list of qrCodes if it not exist there
        scannedCodes.remove(qrCode);
    }

}
