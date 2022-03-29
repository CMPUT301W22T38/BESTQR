package com.example.bestqr.ui.qr;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bestqr.Database;
import com.example.bestqr.models.Profile;
import com.example.bestqr.QRCODE;

public class QrViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    private static Profile user_profile;
    private Database db;
    private QRCODE qrcode;

    public QrViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Test");
        this.qrcode = null;
    }
}
