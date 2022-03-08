package com.example.bestqr;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class QR_CODE {
    private Location codeLocation;
    private Bitmap bitmap;
    private String hash;


    public QR_CODE(Location codeLocation,String hash) {
        this.codeLocation = codeLocation;
        this.hash = hash;

    }

    public QR_CODE(String hash) {
        // this enforces the privacy rule for user location
        this.codeLocation = null;
        this.hash = hash;
    }




    public Bitmap getCode(){
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(hash, BarcodeFormat.QR_CODE,350,350);
            BarcodeEncoder encoder = new BarcodeEncoder();
            bitmap = encoder.createBitmap(matrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;

    }

    public String getHash(){
        return hash;
    }

    public void calculateScore(){

    }

}
