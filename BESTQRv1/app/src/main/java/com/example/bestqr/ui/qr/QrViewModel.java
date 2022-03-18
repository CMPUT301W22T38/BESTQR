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

    public QrViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Test");
    }

}
