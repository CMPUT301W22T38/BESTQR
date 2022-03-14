package com.example.bestqr;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class QRCODE {
    private Location codeLocation;
    private Bitmap bitmap;
    private String hash;
    private int score;
    MessageDigest digest;


    /**
     * This constructor is for the QRCODE where location is stored
     * @param codeLocation: This is the Location that is recorded for the QRCODE
     * @param contents: this is the content of the QRCODE
     */
    public QRCODE(Location codeLocation, String contents) {
        this.codeLocation = codeLocation;
        this.hash = calculateHash(contents);
        this.score = calculateScore(hash);
    }

    /**
     * This constructor is for the QRCODE where location is stored
     * @param contents: this is the content of the QRCODE
     */
    public QRCODE(String contents) {
        // this enforces the privacy rule for user location
        this.codeLocation = null;
        this.hash = calculateHash(contents);
        this.score = calculateScore(hash);
    }

    //    public HashMap<String,Object> toMap() {
//        HashMap<String, Object> asMap = new HashMap<String, Object>();
//
//        return asMap;
//
//    }
    public HashMap<String, Object> getLocation() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (this.codeLocation != null) {
            map.put("Latitude", codeLocation.getLatitude());
            map.put("Longitude", codeLocation.getLongitude());
        }
        return map;
    }

    /**
     * This method generates a QRCODE for the content passed in
     * @return: A Bitmap object that contains the image of the QRCODE
     */
    public Bitmap getCode(){
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(hash, BarcodeFormat.QR_CODE, 350, 350);
            BarcodeEncoder encoder = new BarcodeEncoder();
            bitmap = encoder.createBitmap(matrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;

    }

    /**
     * This method gets the hash of the QRCODE
     * @return: A string object that contains the hash in Hexadecimal of the QRCODE
     */
    public String getHash(){
        return hash;
    }

    /**
     * This method returns the score of the QRCODE
     * @return :
     */
    public int getScore(){
        return score;
    }

    /**
     * This method returns the location of the QRCODE
     * @return the location of the QRCODE
     */
    public Location getCodeLocation(){
        return codeLocation;
    }

    /**
     * This method converts QR contents to encoded hash (bytes)
     * @param contents : The content of the QRCODE
     * @return: The String hexadecimal representation of the content of the QRCODE
     */
    public String calculateHash(String contents) {
        try{digest = MessageDigest.getInstance("SHA-256");}
        catch (NoSuchAlgorithmException e)
        {
         System.err.println("Sorry, something went wrong");
        }
        byte[] encodedHash = digest.digest(
                contents.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    /**
     * This method calculates the score of the QRCODE from the hash
     * @param hash: he hexadecimal representation of the content og the QRCODE
     * @return:  The score of the QRCODE
     */
    public int calculateScore(String hash){

        // Converts hash to QR score
        char current_char;
        int total_score = 0;
        char prev_char = hash.charAt(0);
        int char_multiplier = 0;

        for (int i = 1; i < hash.length(); i++) {

            current_char = hash.charAt(i);

            if (current_char == prev_char) {

                char_multiplier++;

            } else {

                if (char_multiplier > 0) {

                    if (prev_char == '0') {
                        total_score += (int) Math.pow(20, char_multiplier);

                    } else {
                        total_score += (int) Math.pow(Character.digit(prev_char, 16), char_multiplier);

                    }

                }

                char_multiplier = 0;
            }

            prev_char = current_char;
        }

        return total_score;
    }
}