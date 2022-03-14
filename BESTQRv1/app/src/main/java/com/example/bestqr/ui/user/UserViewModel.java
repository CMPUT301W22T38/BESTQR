package com.example.bestqr.ui.user;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bestqr.Profile;

public class UserViewModel extends ViewModel {

    private Profile user_profile;
    private MutableLiveData<String> mText;

    public UserViewModel(){
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
    public Profile getUserProfile(){
        return user_profile;
    }

}