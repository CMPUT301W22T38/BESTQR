package com.example.bestqr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Profile {
    private String androidId;
    private String userName;
    private String phoneNumber;
    private String emailAddress;
    private QR_CODE deviceQrCode;
    private int score;
    private ArrayList<QR_CODE> scannedCodes;

    public Profile(String hash) {
        this.androidId = hash;
        this.deviceQrCode = new QR_CODE(hash);
    }

    public void fromMap(HashMap<String, Object> map) {
        if (map.containsKey("userinfo")) {
            HashMap<String, Object> userinfo = (HashMap) map.get("userinfo");

            if (userinfo.containsKey("username")) {
                this.userName = (String) userinfo.get("username");
            }

            if (userinfo.containsKey("emailAddress")) {
                this.emailAddress = (String) userinfo.get("emailaddress");
            }

            if (userinfo.containsKey("phonenumber")) {
                this.phoneNumber = (String) userinfo.get("phonenumber").toString();
            }
        }

        if (map.containsKey("history")) {
            HashMap<String, Object> history = (HashMap) map.get("history");



//            for (String key: history.KeySet()) {
//                QR_CODE qrcode = new QR_CODE(key, history.get(key));


//                scannedCodes.add()
        }

//                this.scannedCodes.add


//                QR_CODE qrcode = this.history.get(s);
    }



    public HashMap<String, Object> toMap() {
        HashMap<String, Object> asMap = new HashMap<String, Object>();
        HashMap<String, Object> historyMap = new HashMap<String, Object>();
        HashMap<String, Object> userinfoMap = new HashMap<String, Object>();
        HashMap<String, Object> PairMap = new HashMap<String, Object>();

        asMap.put(androidId, asMap);

        PairMap.put("history", historyMap);
        PairMap.put("userinfo", userinfoMap);

        for (QR_CODE q: scannedCodes) {
        }
//            historyMap.put()


        return asMap;
    }


//    public void toMap() {
//        ProfileMap = new HashMap<String, Object>();
//        HashMap<String, Object>HistoryMap = new HashMap<String, Object>();
//        HashMap<String, Object>UserInfoMap = new HashMap<String, Object>();
//
//        HashMap<Object, Object>DataMap = new HashMap<Object, Object>() {{
//            put("UserInfoMap", UserInfoMap);
//            put("HistoryMap", HistoryMap);
//        }};
//
//        ProfileMap.put(androidId, DataMap);
//
//        UserInfoMap.put("username",     userName);
//        UserInfoMap.put("phonenumber",  phoneNumber);
//        UserInfoMap.put("emailaddress", null);
////        UserInfoMap.put("emailaddress", emailAddress);
////        UserInfoMap.put("deviceqrcode", deviceQrCode);
//
//
//
//
//    }



    public String getandroidId() {
        return this.androidId;
    }

    public void setandroidId(String androidid) {
        this.androidId = androidid;
    }

    public String getuserName() {
        return this.userName;
    }

    public String getehoneNumber() {
        return this.phoneNumber;
    }

    public String getemailAddress() {
        return this.emailAddress;
    }

//    public String get
//    public Profile(String user_name, QR_CODE deviceQrCode2,int phoneNumber,String emailAddress) {
//        this.user_name = user_name;
//        this.deviceQrCode = deviceQrCode2;
//    }


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
        this.deviceQrCode = new QR_CODE(androidId);
        // the unique qrcode for the profile that can be used to log into another account
        // ideally the hash should be the unique username
    }

    public void addNewQRCode(QR_CODE qrCode) {

        this.scannedCodes.add(qrCode);
        // the hash should be stored not the image itself
        // if geolocation is allowed the it should be shown on the map
        // also add it to the owner list of qrCodes if it does not exist there
    }

    public void addScore(){
        // this method adds to the user's points if he chooses to store the code
    }

//    public void displayQrCodes(){
//        //displays all the qr codes the user has stored
//    }

//    public void removeCode(QR_CODE qrCode){
//        // also remove it from the owner list of qrCodes if it not exist there
//        scannedCodes.remove(qrCode);
//    }

}
