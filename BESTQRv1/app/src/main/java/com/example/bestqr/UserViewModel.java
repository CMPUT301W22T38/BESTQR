package com.example.bestqr;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bestqr.Database;
import com.example.bestqr.Profile;
import com.example.bestqr.QRCODE;
import com.example.bestqr.ui.leaderboard.LeaderboardScoreBlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// UserViewModel is a ViewModel that can be global to all fragments, as it is owned by the Main Activity
// This Class will allow a user profile to persist across fragment navigation
// And can be used to handle data changes
public class UserViewModel extends ViewModel {

    private Profile user_profile;
    private Database db;

    public Database getDb() {
        return db;
    }

    public void setDb(Database db) {
        this.db = db;
    }

    public void sortListAscendingScore() {
        QRCodeList qrcodelist = this.user_profile.getScannedCodes();
        Collections.sort(qrcodelist, Comparator.comparing(QRCODE::getScore));
    }

    public void sortListDescending() {
        QRCodeList qrcodelist = this.user_profile.getScannedCodes();
        Collections.sort(qrcodelist, Comparator.comparing(QRCODE::getScore).reversed());
    }

    public void sortListChronological() {
        QRCodeList qrcodelist = this.user_profile.getScannedCodes();
        Collections.sort(qrcodelist, Comparator.comparing(QRCODE::getScannedTime));
    }

    /** Sets the main user profile
     * @param profile
     *      a profile object, containing info on a user
     */
    public void setUserProfile(Profile profile){
        user_profile = profile;
    }

    /** Gets the main user profile
     * @return profile
     *      a profile object, containing info on a u
     *      ser
     */
    public Profile getUserProfile(){
        return user_profile;
    }

}