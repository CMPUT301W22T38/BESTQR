package com.example.bestqr;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.io.ByteArrayDataInput;
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
import java.util.Map;
import java.util.regex.Pattern;

import io.grpc.Context;

// https://console.firebase.google.com/project/bestqrdb/database/bestqrdb-default-rtdb/data
// https://console.firebase.google.com/project/bestqrdb/storage/bestqrdb.appspot.com/files
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

    private Pair<byte[], String> createQRImage(QR_CODE qrcode, String androidId) {
        Bitmap bitmap = qrcode.getCode();
        String hash = qrcode.getHash();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        return new Pair<byte[], String>(data, hash);
    }

    private void uploadToStorage(Pair<byte[], String> pair, String androidid) {
        byte[] data = pair.first;
        String hash = pair.second;

        StorageReference folder_ref = storage_ref.child(androidid);
        StorageReference file_ref = folder_ref.child(hash + ".jpg");
        file_ref.putBytes(data);
    }

//    private void uploadToStorage(QR_CODE qrcode, String androidid) {
//        Bitmap bitmap = qrcode.getCode();
//        String hash = qrcode.getHash();
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        // store image in the storage, path = /{androidid}/{hash}.jpg
//        StorageReference folder_ref = storage_ref.child(androidid);
//        StorageReference file_ref = folder_ref.child(hash + ".jpg");
//        file_ref.putBytes(data);
//    }



    public void writeQRCode(QR_CODE qrcode, String androidid) {
        String key = qrcode.getHash();

        DatabaseReference qr_ref = user_ref.child(androidid).child("history").child(key);

        qr_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            HashMap<String, Object> meta = new HashMap<String, Object>();
                            HashMap<String, Object> f = qrcode.getLocation();

                            meta.put("score", qrcode.getScore());
                            meta.put("location", qrcode.getLocation());
//                            meta.put("timestamp", qrcode.getTimeStamp());

                            qr_ref.setValue(meta);

                            Pair<byte[], String> args = createQRImage(qrcode, androidid);
                            uploadToStorage(args, androidid);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    // not sure what to return
    public void getImageFromStorage(String androidid, String hash) {

        String filename = hash + ".jpg";
        StorageReference file_ref = storage_ref.child(androidid).child(filename);
        file_ref.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // do something with bytes
            }
        });
    }


    public HashMap<String, Object> readQRCode(String androidid) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        DatabaseReference qrlist_db_ref = user_ref.child(androidid).child("history");

        qrlist_db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, Object> map = (HashMap) snapshot.getValue();
                    for (Map.Entry<String, Object> entry: map.entrySet()) {
                        String qrcode_hash = entry.getKey();

//
                        getImageFromStorage(androidid, qrcode_hash);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return map;
    }
}



