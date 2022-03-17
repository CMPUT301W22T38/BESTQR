package com.example.bestqr.ui.qr;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bestqr.Database;
import com.example.bestqr.Profile;

public class QrViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    private static Profile user_profile;
    private Database db;

    public QrViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("Test");

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
     *      a profile object, containing info on a user
     */
    public static Profile getUserProfile(){
        return user_profile;
    }

}
