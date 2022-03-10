package com.example.bestqr.ui.leaderboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LeaderboardViewModelMain extends ViewModel {

    private MutableLiveData<String> mText;

    public LeaderboardViewModelMain() {
        mText = new MutableLiveData<>();
        mText.setValue("This is leaderboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}