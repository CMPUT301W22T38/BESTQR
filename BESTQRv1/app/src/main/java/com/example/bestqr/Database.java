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

// realtime db
// https://console.firebase.google.com/project/bestqrdb/database/bestqrdb-default-rtdb/data
// storage
// https://console.firebase.google.com/project/bestqrdb/storage/bestqrdb.appspot.com/files

public class Database {

    private FirebaseDatabase database;
    private DatabaseReference userename_ref;
    private DatabaseReference user_ref;

    private FirebaseStorage storage;
    private StorageReference storage_ref;

    /**
     * This constructor initializes database object, consisting of
     * firebase realtime db and firebase storage
     */
    public Database() {
        // create reference objects to firebase realtime db and storage
        database = FirebaseDatabase.getInstance();
        user_ref = database.getReference().child("user");

        storage = FirebaseStorage.getInstance();
        storage_ref = storage.getReference();
    }

    /**
     * look up user table based on the device's android_id
     * @param android_id: device's unique android id
     * @return remapped Profile object
     */
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

    /**
     * This method takes care of storing QRCODE object both in db and storage
     * @param qrcode: QRCODE object that the user scanned
     * @param androidid: device's unique android id
     */
    public void writeQRCode(QRCODE qrcode, String androidid) {
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

    /**
     * converts bitmap object to bytes in low level
     * @param qrcode an object containing information about qr code such as bitmap, and its unique hash
     * @param androidId device's unique android id
     * @return a pair storing an image in byte format, and its encrypted hash
     */
    private Pair<byte[], String> createQRImage(QRCODE qrcode, String androidId) {
        Bitmap bitmap = qrcode.getCode();
        String hash = qrcode.getHash();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        return new Pair<byte[], String>(data, hash);
    }

    /**
     * retrieves a list of QR codes scanned by a specific user
     * @param android_id
     * @return a list of qr codes remapped as encrypted hash, image in byte form
     */
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

    /**
     * access firebase storage using user's android id and encrypted hash
     * @param android_id used as a unique folder
     * @param hash used as filename
     * @return the actual jpg in byte form
     */
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

    /**
     * upload image to firebase storage
     * @param pair containing the image in byte form, and filename
     * @param androidid
     */
    private void uploadQRImageToStorage(Pair<byte[], String> pair, String androidid) {
        // upload a QR code image to firebase storage
        byte[] data = pair.first;
        String hash = pair.second;

        StorageReference folder_ref = storage_ref.child(androidid);
        StorageReference file_ref = folder_ref.child(hash + ".jpg");
        file_ref.putBytes(data);
    }
}
// to be used later
//    public void test_get_user(String androidid) {
//        DatabaseReference ref = user_ref.child("46adc3359ed34a4d");
////        DatabaseReference ref = user_ref.child(androidid);
//        Task<DataSnapshot> task = ref.get();
//
//        while (!task.isComplete()) {}
//
//        if (task.isSuccessful() && task.isComplete()) {
//            DataSnapshot data = task.getResult();
//
//            if (data.exists()) {
//                DataSnapshot userinfo_data = data.child("userinfo");
//                DataSnapshot history_data = data.child("history");
//
//                HashMap<String, Object> userinfo_map = test_get_userinfo(userinfo_data);
//                test_get_history(history_data);
//            }
//        }
//    }
//
//    private HashMap<String, Object> test_get_userinfo(String androidid) {
//        DatabaseReference ref = user_ref.child(androidid).child("userinfo");
//        Task<DataSnapshot> task = ref.get();
//
//        while (!task.isComplete()) {}
//
//        if (task.isSuccessful() && task.isComplete()) {
//            DataSnapshot data = task.getResult();
//
//            if (data.exists()) {
//                
//
//            }
//        }
//
//
//    }
//
//    private HashMap<String, Object> test_get_userinfo(DataSnapshot data) {
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        for (String key: Arrays.asList("username", "emailaddress", "phonenumber")) {
//            map.put(key, null);
//        }
//        if (data.exists()) {
//            HashMap<String, Object> datamap = (HashMap) data.getValue();
//            map.putAll(datamap);
//        }
//        return map;
//    }
//
//    private HashMap<String, Object> test_get_history(DataSnapshot data) {
////        HashMap<String, Object> map = new HashMap<String, Object>();
////          add more child keys if required
////        for (String key: Arrays.asList("score", "location")) {
////            map.put(key, null);
////        }
////
////        if (data.exists()) {
////            HashMap<String, Object> datamap = (HashMap) data.getValue();
////            map.putAll(datamap);
////
////
////        }
////        return map;
//
//        if (data.exists()) {
//
//            System.out.println(data.getValue());
//        }
//        return null;
//    }
//
//    private void test_get_qrcode(DataSnapshot data) {
//
//    }
//
//    public void test_add_user() {
//
//    }
//
//    public void test_add_qr_code(String androidid, ) {
//
//    }
//
//    public void update_userinfo(String androidid, String oldvalue, String newvalue, String fieldkey) {
//
//        DatabaseReference ref = user_ref.child(androidid).child("uesrinfo");
//        Task<DataSnapshot> task = ref.get();
//
//        while (!task.isComplete()) {}
//
//        DataSnapshot data = task.getResult();
//
//        if (data.exists()) {
//
//
//            }
//        }
//    }
//}
