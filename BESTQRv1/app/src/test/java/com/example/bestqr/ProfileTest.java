package com.example.bestqr;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class ProfileTest {
    private Profile mockProfile(){
        return new Profile("user", "BFG5DGW54", 58700000, "email.ualberta.ca");
    }

    @Test
    void testProfile() {
        Profile profile = mockProfile();
        assertEquals("BFG5DGW54", profile.getDeviceID());
    }
}
