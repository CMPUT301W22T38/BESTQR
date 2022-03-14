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

    public void setUserProfile(Profile profile){
        user_profile = profile;
    }

    public Profile getUserProfile(){
        return user_profile;
    }

    public MutableLiveData<String> getText(){
        return mText;
    }

    public void setText(MutableLiveData<String> str){
        mText = str;
    }

}