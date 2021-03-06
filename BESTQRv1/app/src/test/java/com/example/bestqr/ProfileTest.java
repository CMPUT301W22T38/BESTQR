package com.example.bestqr;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import androidx.annotation.NonNull;

import com.example.bestqr.Database.Database;
import com.example.bestqr.models.BaseProfile;
import com.example.bestqr.models.Profile;
import com.example.bestqr.models.QRCODE;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.local.Scheduler;

import java.util.concurrent.TimeUnit;



public class ProfileTest {

    public String androidId = "eadf4636a1b8c634";
    public String userName = "name";
    public String phoneNumber = "0123456789";


    private BaseProfile mockEmptyBaseProfile3(){
        return new BaseProfile("", "", "", "");
    }
    private Profile mockProfile(){
        return new Profile("");
    }


    @Test
    void testBaseProfile() {

        BaseProfile profile = mockEmptyBaseProfile3();

        assertTrue(profile.getAndroidId().isEmpty());
        assertTrue(profile.getUserName().isEmpty());
        assertTrue(profile.getPhoneNumber().isEmpty());

        profile.setAndroidId(androidId);
        profile.setUserName(userName);
        profile.setPhoneNumber(phoneNumber);

        assertFalse(profile.getAndroidId().isEmpty());
        assertFalse(profile.getUserName().isEmpty());
        assertFalse(profile.getPhoneNumber().isEmpty());
        assertEquals(androidId, profile.getAndroidId());
        assertEquals(userName, profile.getUserName());
        assertEquals(phoneNumber, profile.getPhoneNumber());
    }


    @Test
    void testChangeProfile(){
        BaseProfile profile = mockEmptyBaseProfile3();
        profile.setAndroidId(androidId);
        profile.setPhoneNumber(phoneNumber);
        profile.setUserName(userName);
        profile.setPhoneNumber("987654321");
        profile.setUserName("ChangedUserName");
        assertEquals("987654321", profile.getPhoneNumber());
        assertEquals("ChangedUserName", profile.getUserName());
    }
}