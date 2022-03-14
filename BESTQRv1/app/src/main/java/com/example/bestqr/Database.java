package com.example.bestqr;

import android.graphics.Bitmap;
import android.util.Pair;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

import java.util.HashMap;
import java.util.Map;

// https://console.firebase.google.com/project/bestqrdb/database/bestqrdb-default-rtdb/data
// https://console.firebase.google.com/project/bestqrdb/storage/bestqrdb.appspot.com/files
public class Database {

    private FirebaseDatabase database;
    private DatabaseReference userename_ref;
    private DatabaseReference user_ref;

    private FirebaseStorage storage;
    private StorageReference storage_ref;

    public Database() {
        // create reference objects to firebase realtime db and storage
        database = FirebaseDatabase.getInstance();
        user_ref = database.getReference().child("user");

        storage = FirebaseStorage.getInstance();
        storage_ref = storage.getReference();
    }

    public Profile getProfile(String android_id) {
        Profile profile = new Profile(android_id);
        user_ref.child(android_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // if the current user's android id is being found in firebase realtiem db,
                        // create a profile object accordingly and return it. Otherwise, just return
                        // an empty object
                        if (snapshot.exists()) {
                            HashMap<String, Object> map = (HashMap) snapshot.getValue();

                            HashMap<String, Object> userinfo = (HashMap) map.get("userinfo");
                            if (userinfo != null) {
                                profile.setUserName((String) userinfo.get("username"));
                                profile.setEmailAddress((String) userinfo.get("emailaddress"));
                                profile.setPhoneNumber((String) userinfo.get("phonenumber").toString());
                            }

                            HashMap<String, Object> history = (HashMap) map.get("history");
                            if (history != null) {
                                for (Map.Entry entry : history.entrySet()) {
                                    // <QR code hash, bytes to create an image>
                                    HashMap<String, Object> QR_list = getQRCode(android_id);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
//                        System.out.println(error.toException().toString());
                    }
                });
        return profile;
    }

        // upload qrcode to realtime db
        String key = qrcode.getHash();

        DatabaseReference qr_ref = user_ref.child(androidid).child("history").child(key);

        qr_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // if the entry being added doesnt exist in realtime db
                if (!snapshot.exists()) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    HashMap<String, Object> meta = new HashMap<String, Object>();

                    // append some metadata
                    meta.put("score", qrcode.getScore());
                    meta.put("location", qrcode.getLocation());

                    // upload it to db
                    qr_ref.setValue(meta);

                    Pair<byte[], String> QR_image = createQRImage(qrcode, androidid);
                    // store it in the firebase storage
                    uploadQRImageToStorage(QR_image, androidid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

        Bitmap bitmap = qrcode.getCode();
        String hash = qrcode.getHash();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        return new Pair<byte[], String>(data, hash);
    }

    public HashMap<String, Object> getQRCode(String android_id) {
        // return a list of <QR code hash, byte[]> that a certain user has
        HashMap<String, Object> map = new HashMap<String, Object>();
        DatabaseReference qrlist_db_ref = user_ref.child(android_id).child("history");

        qrlist_db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, Object> qr_db = (HashMap) snapshot.getValue();
                    for (Map.Entry<String, Object> entry : qr_db.entrySet()) {
                        String key = entry.getKey();

                        byte[] bytes = getQRImageFromStorage(android_id, key);
                        map.put(key, bytes);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return map;
    }

    public byte[] getQRImageFromStorage(String android_id, String hash) {
        // return QR code image a user queried
        HashMap<String, byte[]> temp = new HashMap<String, byte[]>();

        String filename = hash + ".jpg";
        StorageReference file_ref = storage_ref.child(android_id).child(filename);

        file_ref.getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        temp.put("bytes", bytes);
                    }

                });
        return temp.get("bytes");
    }

    private void uploadQRImageToStorage(Pair<byte[], String> pair, String androidid) {
        // upload a QR code image to firebase storage
        byte[] data = pair.first;
        String hash = pair.second;

        StorageReference folder_ref = storage_ref.child(androidid);
        StorageReference file_ref = folder_ref.child(hash + ".jpg");
        file_ref.putBytes(data);
    }
}

//
//    private void uploadQRImageToStorage(QR_CODE qrcode, String androidid) {
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
//}

