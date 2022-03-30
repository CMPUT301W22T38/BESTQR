package com.example.bestqr.models;

import android.graphics.Bitmap;

import com.example.bestqr.utils.QRmethods;

import java.io.Serializable;

public class BaseQRCode implements Serializable {
    private String hash;
    private Bitmap bitmap;

    public BaseQRCode() {
        //
    }

    public BaseQRCode(String contents) {
        this.hash = QRmethods.calculateHash(contents);
        this.bitmap = QRmethods.calculateBitmap(this.hash);
    }

    public BaseQRCode(String hash, Bitmap bitmap) {
        this.hash = hash;
        this.bitmap = bitmap;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
        this.bitmap = QRmethods.calculateBitmap(this.getHash());
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
