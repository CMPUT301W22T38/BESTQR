package com.example.bestqr;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import io.grpc.Context;

// https://console.firebase.google.com/project/bestqrdb/database/bestqrdb-default-rtdb/data

public class Database {

    private FirebaseDatabase database;
    private DatabaseReference userename_ref;
    private DatabaseReference user_ref;

    private FirebaseStorage storage;
    private StorageReference storage_ref;

    Profile profile;

    public Database() {
        database = FirebaseDatabase.getInstance();
        user_ref = database.getReference().child("user");
        userename_ref = database.getReference().child("username");

        storage = FirebaseStorage.getInstance();
        storage_ref = storage.getReference();
    }


    public Profile getProfile(String hash) {
//        Profile p = new Profile;

        user_ref.child(hash)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            HashMap<String, Object> map = (HashMap) snapshot.getValue();
                            System.out.println(map);
                        }
                        else {


                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println(error.toException().toString());
                    }
                });
//        return p;
        return profile;
    }





    private void uploadToStorage(QR_CODE qrcode, String androidid) {
        Bitmap bitmap = qrcode.getCode();
        String hash = qrcode.getHash();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // store image in the storage, path = /{androidid}/{hash}.jpg
        StorageReference folder_ref = storage_ref.child(androidid);
        StorageReference file_ref = folder_ref.child(hash + ".jpg");
        file_ref.putBytes(data);
    }

    // need appropriate return value
    private void downloadFromStorage(String hash, String androidid) {
        StorageReference folder_ref = storage_ref.child(androidid);
        StorageReference file_ref = folder_ref.child(hash + ".jpg");

//        InputStream is = new ByteArrayInputStream(new byte[]);
        file_ref
                .getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
//                        is.read(bytes);
                    }
                });

//        return is;
    }

    public void writeQRCode(QR_CODE qrcode, String androidid) {
        user_ref.child(androidid)
                .child("history")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        HashMap<String, Object> meta = new HashMap<String, Object>();
                        String key = qrcode.getHash();

                        meta.put("location", qrcode.getLocation());
                        meta.put("score", qrcode.getScore());

                        map.put(key, meta);

                        DatabaseReference qr_ref = user_ref.child(androidid)
                                .child("history")
                                .child(key);

                        qr_ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (!snapshot.exists()) {
                                            qr_ref.setValue(map);

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void readQRCode(String androidid) {
        user_ref.child(androidid)
                .child("history")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String, Object> map = (HashMap) snapshot.getValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void getAllProfilesForLeaderboard() {

    }



}



