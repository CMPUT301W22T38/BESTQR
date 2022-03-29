package com.example.bestqr.models;

import java.io.Serializable;

public class BaseProfile implements Serializable {
    private String androidId;
    private String userName;
    private String phoneNumber;
    private String emailAddress;

    public BaseProfile() {
        //
    }

    public BaseProfile(String androidId) {
        this.androidId = androidId;
    }

    public BaseProfile(String androidId, String userName, String phoneNumber, String emailAddress) {
        this.androidId = androidId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    /**
     * This method returns the unique device id of the user
     * @return the unique device id of a profile
     */
    public String getAndroidId() {
        return androidId;
    }


    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    /**
     * This method returns the username of the user
     * @return : the username of the user
     */
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * This method returns the phone number of the user
     * @return : the phone number of the user
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * This method returns the email address of the user
     * @return : the email address of the user
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
