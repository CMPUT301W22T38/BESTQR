package com.example.bestqr;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bestqr.models.Profile;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<Profile> profileMutableLiveData;

    public ProfileViewModel(Profile profile) {
        profileMutableLiveData = new MutableLiveData<>();
        profileMutableLiveData.setValue(profile);
    }

    public void setProfile(Profile profile) {
        this.profileMutableLiveData.postValue(profile);
    }

    public MutableLiveData<Profile> getProfileMutableLiveData() {
        if (profileMutableLiveData == null) {
            profileMutableLiveData = new MutableLiveData<>();
        }
        return profileMutableLiveData;
    }
}
