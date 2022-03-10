package com.example.bestqr.ui.leaderboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LeaderboardViewModelQR extends ViewModel {

    private MutableLiveData<String> mText;

    public LeaderboardViewModelQR() {
        mText = new MutableLiveData<>();
        mText.setValue("This is leaderboard fragment");
    }



    public LiveData<String> getText() {
        return mText;
    }
}