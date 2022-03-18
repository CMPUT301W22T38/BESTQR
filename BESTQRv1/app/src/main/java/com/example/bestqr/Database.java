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
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.sql.DataSource;

public class Database {
    private FirebaseDatabase database;
    private DatabaseReference user_ref;
    private DatabaseReference username_ref;
    private DatabaseReference global_qrcount;

//    private Profile currentclient;

    public Database() {
        database = FirebaseDatabase.getInstance();
        user_ref = database.getReference().child("user");
        username_ref = database.getReference().child("username");
        global_qrcount = database.getReference().child("qrcode");
    }

    public Profile get_user(String androidid) {
        Profile profile = new Profile(androidid);

        DatabaseReference currentuser_ref = user_ref.child(androidid);

        DatabaseReference userinfo_ref = currentuser_ref.child("userinfo");
        DatabaseReference history_ref = currentuser_ref.child("history");

        DataSnapshot history_data = get_children(currentuser_ref, "history");

        if (user_exists(androidid)) {
            DataSnapshot loginqrcode = get_children(userinfo_ref, "loginqrcode");

            if (loginqrcode.getChildrenCount() == 0) {
                profile.setUserName(get_child_value(userinfo_ref, "username"));
                profile.setEmailAddress(get_child_value(userinfo_ref, "emailaddress"));
                profile.setPhoneNumber(get_child_value(userinfo_ref, "phonenumber"));

                profile.setScannedCodes(get_qr_list(androidid, history_data));
            }

        }
        else {
            add_child(currentuser_ref, "createdAt", get_current_time());
            add_child(userinfo_ref, "username", get_default_username());
            profile.setUserName(get_default_username());
            add_child(userinfo_ref, "phonenumber", "null");
            add_child(userinfo_ref, "emailaddress", "null");
            add_child(userinfo_ref, "loginqrcode", "null");
            history_ref.setValue("null");

            add_child(username_ref, profile.getUserName(), profile.getAndroidID());
        }
        return profile;
    }

    public boolean update_userinfo(String androidid, String key, String value) {
        DatabaseReference ref = user_ref.child(androidid).child("userinfo");

        if (user_exists(androidid) == true) {
            if (key.equals("username")) {

            }
            if (key.equals("emailaddress") || key.equals("phonenumber") || key.equals("username")) {
                ref.child(key).setValue(value);
                return true;
            }
        }
        return false;
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

//    private QRCODE get_qrcode(String hash) {
//        QRCODE qrcode = new QRCODE();
//        return qrcode;
//    }

    public QRCODE get_qrcode(String androidid, String hash) {
        QRCODE qrcode = new QRCODE();

        DatabaseReference history_ref = user_ref.child(androidid).child("history");
        DataSnapshot qrdata = get_children(history_ref, hash);
        DatabaseReference qr_ref = qrdata.getRef();

        if (user_exists(androidid)) {
            if (qrdata.exists()) {
                qrcode.setHash(qrdata.getKey());
                qrcode.setIsimported(Boolean.valueOf(get_child_value(qr_ref, "imported")));


                if (!get_child_value(qr_ref, "location").equals("null")) {
                    DatabaseReference location_ref = get_children(qr_ref, "location").getRef();

                    Location location = new Location(LocationManager.GPS_PROVIDER);
                    location.setLongitude(Double.valueOf(get_child_value(location_ref, "Longitude")));
                    location.setLatitude(Double.valueOf(get_child_value(location_ref, "Latitude")));
                    qrcode.setCodeLocation(location);
                } else {
                    qrcode.setCodeLocation(null);
                }
                qrcode.setScore(Integer.valueOf(get_child_value(qr_ref, "score")));
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

                DataSnapshot entry = get_children(global_qrcount, hash);
                DataSnapshot users = get_children(entry, "users");
                if (entry.getChildrenCount() == 0) {
                    add_child(entry, "count", "1");
                    add_child(users, androidid, "1");
                }
                else {
                    add_child(users, androidid, "1");
                    int count = Integer.valueOf(get_child_value(entry, "count"));

                    add_child(entry, "count", String.valueOf(count + 1));
                }

            }

        }
    }


    private Boolean user_exists(String androidid) {
        DataSnapshot data = get_children(user_ref, androidid);

        return (data.exists()) ? true : false;
    }

    private DataSnapshot get_children(DatabaseReference reference, String key) {
        DataSnapshot data = null;

        Task<DataSnapshot> task = reference.child(key).get();

        while (!task.isComplete()) {}

        if (task.isSuccessful() && task.isComplete()) {
            data = task.getResult();
        }
        return data;
    }

    private DataSnapshot get_children(DataSnapshot reference, String key) {
        return get_children(reference.getRef(), key);
    }

    private String get_child_value(DatabaseReference reference, String key) {
        DataSnapshot data = get_children(reference, key);
        String value = data.getValue().toString();
        return value;
    }

    private String get_child_value(DataSnapshot reference, String key) {
        return get_child_value(reference.getRef(), key);
    }

    private void add_child(DatabaseReference reference, String key, String value) {
        reference.child(key).setValue(value);
    }

    private void add_child(DataSnapshot reference, String key, String value) {
        add_child(reference.getRef(), key, value);
    }

    // current time //
    static public String get_current_time() {
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        String string = DateFormat.format(new Date()) + " GMT +00:00";
        return string;
    }

    private String get_default_username() {
        String string = "user" + String.valueOf(this.get_usercount() + 1);
        return string;
    }

    private int get_usercount() {
        DataSnapshot data = null;
        Task<DataSnapshot> task = username_ref.get();

        while (!task.isComplete() && !task.isSuccessful()) {}
        data = task.getResult();
        return (int) data.getChildrenCount();
    }


}

//    private String get_child_value(DatabaseReference reference, String key) {
//        String str = null;
//
//        Task<DataSnapshot> task = reference.child(key).get();
//
//        while (!task.isComplete()) {}
//
//        if (task.isSuccessful() && task.isComplete()) {
//
//            DataSnapshot data = task.getResult();
//            if (data.exists() && data.getChildrenCount() == 0) {
//                str = data.toString();
//            }
//        }
//        return str;
//    }
//
//    private DataSnapshot get_children(DatabaseReference reference, String key) {
//        DataSnapshot data = null;
//
//        Task<DataSnapshot> task = reference.child(key).get();
//
//        while (!task.isComplete()) {}
//
//        if (task.isSuccessful() && task.isComplete()) {
//            data = task.getResult();
//            if (!data.exists() || data.getChildrenCount() == 0) {
//                data = null;
//            }
//        }
//        return data;
//    }






//    private String get_child_value(DatabaseReference reference, String key) {
//        DataSnapshot data = get_children(reference, key);
//        String value = data.getValue().toString();
//        return value;
//    }
//
//    private String get_child_value(DataSnapshot reference, String key) {
//        return get_child_value(reference.getRef(), key);
//    }