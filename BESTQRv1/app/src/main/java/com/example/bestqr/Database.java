package com.example.bestqr;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Pattern;

// https://console.firebase.google.com/project/bestqrdb/database/bestqrdb-default-rtdb/data

public class Database {

    private FirebaseDatabase database;
    private DatabaseReference user_ref;

    private FirebaseStorage storage;
    private StorageReference storage_ref;

    Profile profile;

    public Database() {
        database = FirebaseDatabase.getInstance();
        user_ref = database.getReference().child("user");

        storage = FirebaseStorage.getInstance();
        storage_ref = storage.getReference();

    }

    public Profile readProfile(String hash) {
        Profile p = new Profile(hash);

        user_ref.child(hash)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            HashMap<String, Object> map = (HashMap) snapshot.getValue();
                            p.fromMap(map);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println(error.toException().toString());
                    }
                });
        return p;
    }

//    public void QRCodeReceivedFromCameraActivity(QR_CODE qrcode, String androidid) {
//        qrcode
//    }


    public void writeImage(QR_CODE qrcode, String androidid) {
        Bitmap bitmap = qrcode.getCode();
        String hash = qrcode.getHash();

        profile.getandroidId()



        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference folder_ref = storage_ref.child(androidid);
        StorageReference img_ref = folder_ref.child(hash + ".jpg");
        img_ref.putBytes(data);
    }


    public void uploadImage(FirebaseDatabase db_ref, StorageReference st_ref) {

    }






}



