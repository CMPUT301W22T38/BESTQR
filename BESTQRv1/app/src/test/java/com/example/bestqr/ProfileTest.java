package com.example.bestqr;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.bestqr.models.BaseProfile;
import com.example.bestqr.models.Profile;
import com.example.bestqr.models.QRCODE;

public class ProfileTest {
    private Profile mockProfile(){
        return (Profile) new BaseProfile("user", "useName", "587000000", "email.ualberta.ca");
    }
    void testAdd() {
        Profile profile = mockProfile();
        assertFalse(profile.getUserName().isEmpty());
        assertFalse(profile.getAndroidId().isEmpty());
        assertFalse(profile.getPhoneNumber().isEmpty());
        assertFalse(profile.getEmailAddress().isEmpty());

        profile.setUserName("userName");
        profile.setEmailAddress("email@ualberta.ca");
        profile.setPhoneNumber("587000000");

        assertEquals("userName", profile.getUserName());
        assertEquals("email@ualberta.ca", profile.getEmailAddress());
        assertEquals("587000000", profile.getPhoneNumber());

    }
}