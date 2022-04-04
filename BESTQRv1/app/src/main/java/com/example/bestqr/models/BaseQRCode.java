package com.example.bestqr.models;

import android.graphics.Bitmap;

import com.example.bestqr.utils.QRmethods;

import java.io.Serializable;

public class BaseQRCode{
    private String hash;
    private Bitmap bitmap;

    public BaseQRCode() {

    }

    public BaseQRCode(String contents) {
        this.hash = QRmethods.calculateHash(contents);
        this.bitmap = QRmethods.calculateBitmap(this.hash);
    }

    public BaseQRCode(String hash, Bitmap bitmap) {
        this.hash = hash;
        this.bitmap = bitmap;
    }

    /**
     * gets the hash of the qrCode
     * @return - the hash of the qrCode
     */
    public String getHash() {
        return hash;
    }

    /**
     * sets the hash of the qrCode
     * @param hash - the hash that is getting applied
     */
    public void setHash(String hash) {
        this.hash = hash;
        this.bitmap = QRmethods.calculateBitmap(this.getHash());
    }

    /**
     * gets the bitmap of the qrCode
     * @return the bitmap of the qrCode
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * sets the bitmap of the qrcode
     * @param bitmap- the bitmap that is being applied
     */
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
