package com.example.bestqr.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class QRmethods {
    static MessageDigest digest;

    /**
     * This method converts QR contents to encoded hash (bytes)
     * @param contents : The content of the QRCODE
     * @return: The String hexadecimal representation of the content of the QRCODE
     */
    public static String calculateHash(String contents) {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
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
    public static int calculateScore(String hash){

        // Converts hash to score
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

    /**
     * This method generates a QRCODE for the content passed in
     * @return: A Bitmap object that contains the image of the QRCODE
     */
    public static Bitmap calculateBitmap(String contents) {
        MultiFormatWriter writer = new MultiFormatWriter();
        Bitmap bitmap = null;
        try {
            BitMatrix matrix = writer.encode(contents, BarcodeFormat.QR_CODE, 350, 350);
            BarcodeEncoder encoder = new BarcodeEncoder();
            bitmap = encoder.createBitmap(matrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;

    }
}
