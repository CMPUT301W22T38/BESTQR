package com.example.bestqr;

import androidx.lifecycle.ViewModel;

import com.example.bestqr.models.Profile;
import com.example.bestqr.models.QRCODE;
import com.example.bestqr.ui.leaderboard.LeaderboardScoreBlock;

import java.util.Collections;
import java.util.Comparator;

// UserViewModel is a ViewModel that can be global to all fragments, as it is owned by the Main Activity
// This Class will allow a user profile to persist across fragment navigation
// And can be used to handle data changes
public class UserViewModel extends ViewModel {

    private Profile user_profile;
    private Profile guest_profile;
    private QRCODE qrcode;
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

    public Profile getGuestProfile() {
        return guest_profile;
    }

    public void setGuestProfile(Profile guest_profile) {
        this.guest_profile = guest_profile;
    }

    public void setSelectedQrcode(QRCODE qrcode1){
        qrcode = qrcode1;
    }

    public QRCODE getSelectedQrcode() {
        return qrcode;
    }

}