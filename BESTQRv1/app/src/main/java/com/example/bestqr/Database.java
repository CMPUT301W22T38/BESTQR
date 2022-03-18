package com.example.bestqr;


// realtime db
// https://console.firebase.google.com/project/bestqrdb/database/bestqrdb-default-rtdb/data
// storage
// https://console.firebase.google.com/project/bestqrdb/storage/bestqrdb.appspot.com/files

import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Database {
    private FirebaseDatabase database;
    private DatabaseReference user_ref;
//    private Profile currentclient;

    public Database() {
        database = FirebaseDatabase.getInstance();
        user_ref = database.getReference().child("user");


    }

    public Profile get_user(String androidid) {
        Profile profile = new Profile(androidid);

        DatabaseReference currentuser_ref = get_children(user_ref, androidid).getRef();
        DatabaseReference userinfo_ref = get_children(currentuser_ref, "userinfo").getRef();

        DataSnapshot history_data = get_children(currentuser_ref, "history");
        DatabaseReference history_ref = history_data.getRef();


        if (user_exists(androidid)) {

            String loginqrcode = get_child(userinfo_ref, "loginqrcode");

            if (loginqrcode.equals("null")) {
                profile.setUserName(get_child(userinfo_ref, "username"));
                profile.setEmailAddress(get_child(userinfo_ref, "emailaddress"));
                profile.setPhoneNumber(get_child(userinfo_ref, "phonenumber"));

                profile.setScannedCodes(get_qr_list(androidid, history_data));
            }

        }
        else {
            add_child(currentuser_ref, "createdAt", get_current_time());

            add_child(userinfo_ref, "username", "null");
            add_child(userinfo_ref, "phonenumber", "null");
            add_child(userinfo_ref, "emailaddress", "null");
            add_child(userinfo_ref, "loginqrcode", "null");

            history_ref.setValue("null");
        }
        return profile;
    }

    public boolean update_userinfo(String androidid, String key, String value) {
        DatabaseReference ref = user_ref.child(androidid).child("userinfo");

        if (user_exists(androidid) == true) {
            if (key.equals("emailaddress") || key.equals("phonenumber") || key.equals("username")) {
                ref.child(key).setValue(value);
                return true;
            }
        }
        return false;
    }

    private Boolean user_exists(String androidid) {
        DataSnapshot data = get_children(user_ref, androidid);
        if (data.exists()) {
            return true;
        }
        else {
            return false;
        }
    }

    public ArrayList<QRCODE> get_qr_list(String androidid, DataSnapshot data) {
        ArrayList<QRCODE> qrlist = new ArrayList<QRCODE>();
        if (data.getChildrenCount() > 0 ) {
            data.getChildren().forEach((d) -> {
                QRCODE qrcode = get_qrcode(androidid, d.getKey().toString());
                qrlist.add(qrcode);
            });
        }
        return qrlist;
    }



    private QRCODE get_qrcode(String hash) {
        QRCODE qrcode = new QRCODE();
        return qrcode;
    }

    public QRCODE get_qrcode(String androidid, String hash) {
        QRCODE qrcode = new QRCODE();

        DatabaseReference history_ref = user_ref.child(androidid).child("history");
        DataSnapshot qrdata = get_children(history_ref, hash);
        DatabaseReference qr_ref = qrdata.getRef();

        if (user_exists(androidid)) {
            if (qrdata.exists()) {
                qrcode.setHash(qrdata.getKey());
                qrcode.setIsimported(Boolean.valueOf(get_child(qr_ref, "imported")));


                if (!get_child(qr_ref, "location").equals("null")) {
                    DatabaseReference location_ref = get_children(qr_ref, "location").getRef();

                    Location location = new Location(LocationManager.GPS_PROVIDER);
                    location.setLongitude(Double.valueOf(get_child(location_ref, "Longitude")));
                    location.setLatitude(Double.valueOf(get_child(location_ref, "Latitude")));
                    qrcode.setCodeLocation(location);
                } else {
                    qrcode.setCodeLocation(null);
                }
                qrcode.setScore(Integer.valueOf(get_child(qr_ref, "score")));
            }
        }
        return qrcode;
    }

    public void add_qrcode(String androidid, QRCODE qrcode) {
        String hash = qrcode.getHash();

        DatabaseReference history_ref = user_ref.child(androidid).child("history");
        DataSnapshot qrdata = get_children(history_ref, hash);
        DatabaseReference qr_ref = qrdata.getRef();

        if (user_exists(androidid)) {
            if (!qrdata.exists()) {
                add_child(history_ref, hash, "null");

                add_child(qr_ref, "imported", String.valueOf(qrcode.getisImported()));
                add_child(qr_ref, "score", String.valueOf(qrcode.getScore()));
                add_child(qr_ref, "location", "null");


                DatabaseReference location_ref = qr_ref.child("location");
                if (qrcode.getCodeLocation() != null) {
                    add_child(location_ref, "Longitude", String.valueOf(qrcode.getCodeLocation().getLongitude()));
                    add_child(location_ref, "Latitude", String.valueOf(qrcode.getCodeLocation().getLatitude()));
                }

                add_child(qr_ref, "createdat", get_current_time());
            }

        }
    }



    private DataSnapshot get_children(DatabaseReference reference, String key) {
        DataSnapshot data = null;

        DatabaseReference ref = reference.child(key);
        Task<DataSnapshot> task = ref.get();

        while (!task.isComplete()) {}

        if (task.isSuccessful() && task.isComplete()) {
            data = task.getResult();
        }
        return data;
    }

    private String get_child(DatabaseReference reference, String key) {
        DataSnapshot data = get_children(reference, key);
        String value = data.getValue().toString();
        return value;
    }

    private void add_child(DatabaseReference reference, String key, String value) {
        DatabaseReference ref = reference.child(key);
        ref.setValue(value);
    }





    // helper methods //
    private String get_current_time() {
        SimpleDateFormat gmtDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        String timestamp = gmtDateFormat.format(new Date()) + " GMT +00:00";
        return timestamp;
    }
}