package com.example.bestqr;


// realtime db
// https://console.firebase.google.com/project/bestqrdb/database/bestqrdb-default-rtdb/data
// storage
// https://console.firebase.google.com/project/bestqrdb/storage/bestqrdb.appspot.com/files

import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.NonNull;

import com.example.bestqr.ui.leaderboard.LeaderboardScoreBlock;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
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

    private DatabaseTable GLOBAL_QRCODETABLE;
    private DatabaseTable GLOBAL_REGISTRATIONTABLE;
    private DatabaseTable GLOBAL_USERNAMETABLE;
    private DatabaseTable GLOBAL_USERTABLE;

    private Storage storage;



    public Database() {
        database = FirebaseDatabase.getInstance();
        user_ref = database.getReference().child("user");
        username_ref = database.getReference().child("username");
        global_qrcount = database.getReference().child("qrcode");

        GLOBAL_QRCODETABLE = new DatabaseTable(database.getReference().child("qrcode"));
        GLOBAL_USERTABLE = new DatabaseTable(database.getReference().child("user"));
        GLOBAL_USERNAMETABLE = new DatabaseTable(database.getReference().child("username"));
        GLOBAL_REGISTRATIONTABLE = new DatabaseTable(database.getReference().child("registeredandroidid"));

        storage = new Storage();
    }

    public Profile get(String androidid) {
        Profile profile = new Profile(androidid);

        DatabaseTable user_table = new DatabaseTable(GLOBAL_USERTABLE.getRef(), androidid);

        boolean registered = GLOBAL_REGISTRATIONTABLE.exists(androidid);
        if (registered) {
            boolean hasloginqrcode = user_table.hasChildren("userinfo", "loginqrcode");

            if (hasloginqrcode) {

            }

            else {
                __get__user__(user_table, profile);
                boolean hasqrcodes = user_table.hasChildren("history");

                if (hasqrcodes) {
                    DatabaseTable QRListTable = new DatabaseTable(user_table, "history");
                    QRCodeList qrcodelist = get_qrlist(QRListTable);
                    profile.setScannedCodes(qrcodelist);
                }
            }
        }
        else {
            String INITIAL_USERNAME = get_default_username();
            profile.setUserName(INITIAL_USERNAME);

            TimeStamp timestamp = new TimeStamp();
            GLOBAL_REGISTRATIONTABLE.add(androidid, timestamp.getTimeStamp());
            GLOBAL_USERNAMETABLE.add(INITIAL_USERNAME, androidid);

            add_user(user_table, INITIAL_USERNAME, timestamp);
        }

        return profile;
    }

    public void add_user(DatabaseTable usertable, String username, TimeStamp timestamp) {
        usertable.add("createdAt", timestamp.getTimeStamp());
        usertable.add("history", "null");
        usertable.add("userinfo", "phonenumber", "null");
        usertable.add("userinfo", "emailaddress", "null");
        usertable.add("userinfo", "loginqrcode", "null");
        usertable.add("userinfo", "username", username);
    }

    public void __get__user__(DatabaseTable usertable, Profile profile) {
        profile.setUserName(usertable.get("userinfo", "username"));
        profile.setEmailAddress(usertable.get("userinfo", "emailaddress"));
        profile.setPhoneNumber(usertable.get("userinfo", "phonenumber"));
    }

    public QRCodeList get_qrlist(DatabaseTable qrlisttable) {
        QRCodeList qrcodelist = new QRCodeList();

        for (DataSnapshot datasnapshot: qrlisttable.getChildren()) {
            String hash = datasnapshot.getKey();
            DatabaseTable qrcodetable = new DatabaseTable(qrlisttable, hash);
            qrcodelist.add(get_qrcode(qrcodetable, hash));
        }

        return qrcodelist;
    }

    public QRCODE get_qrcode(DatabaseTable qrcodetable, String hash) {
        QRCODE qrcode = new QRCODE();
        qrcode.setHash(hash);
        TimeStamp timestamp = new TimeStamp();
        timestamp.setTimeStamp(qrcodetable.get("createdat"));
        qrcode.setTimestamp(timestamp);
        qrcode.setisImported(Boolean.valueOf(qrcodetable.get("imported")));

        Location location = null;
        if (qrcodetable.hasChildren("location")) {
            location = new Location(LocationManager.GPS_PROVIDER);
            location.setLongitude(Double.valueOf(qrcodetable.get("location", "longitude")));
            location.setLatitude(Double.valueOf(qrcodetable.get("location", "latitude")));
            qrcode.setCodeLocation(location);
        }
        qrcode.setScore(Integer.valueOf(qrcodetable.get("score")));

        return qrcode;
    }

//    public void add_qrcode(Profile profile, QRCODE qrcode) {
//
//    }

    public void add_qrcode(String username, QRCODE qrcode) {
        String androidid = GLOBAL_USERNAMETABLE.get(username);
        DatabaseTable user_table = new DatabaseTable(GLOBAL_USERTABLE, androidid);
        DatabaseTable QRListTable = new DatabaseTable(user_table, "history");

        String hash = qrcode.getHash();
        if (!QRListTable.exists(hash)) {
            TimeStamp timestamp = new TimeStamp();
            QRListTable.add(hash, "createdat", timestamp.getTimeStamp());
            QRListTable.add(hash, "imported", String.valueOf(qrcode.getisImported()));
            QRListTable.add(hash, "score", String.valueOf(qrcode.getScore()));
            QRListTable.add(hash, "location", "null");

            if (qrcode.getCodeLocation() != null) {
                QRListTable.add(hash, "location", "longitude", String.valueOf(qrcode.getCodeLocation().getLongitude()));
                QRListTable.add(hash, "location", "latitude", String.valueOf(qrcode.getCodeLocation().getLatitude()));
            }

            if (GLOBAL_QRCODETABLE.exists(hash)) {
                int total = Integer.valueOf(GLOBAL_QRCODETABLE.get(hash, "count"));
                GLOBAL_QRCODETABLE.add(hash, "count", String.valueOf(total + 1));
            }
            else {
                GLOBAL_QRCODETABLE.add(hash, "count", "1");
            }
            GLOBAL_QRCODETABLE.add(hash, "users", androidid, timestamp.getTimeStamp());

            storage.upload(qrcode, androidid);
        }

    }


    public ArrayList<LeaderboardScoreBlock> get_all_scoring_types(){

        ArrayList<LeaderboardScoreBlock> result = new ArrayList<LeaderboardScoreBlock>();

        for (DataSnapshot dataSnapshot: GLOBAL_USERTABLE.getChildren()) {
            String androidid = dataSnapshot.getKey();
            String username;
            int highest_score = 0;
            int total_scanned = 0;
            int total_score = 0;

            Profile profile = get(androidid);
            username = profile.getUserName();
            QRCodeList list = profile.getScannedCodes();

            if (list != null) {
                highest_score = profile.getHighestScore();
                total_scanned = profile.getNumberCodesScanned();
                total_score = list.getTotalScore();
            }
            result.add(new LeaderboardScoreBlock(androidid, username, highest_score, total_scanned, total_score));
        }
        return result;
    }


//
//
//
//    public DatabaseReference get_userref(String androidid) {
//        return this.user_ref.child(androidid);
//    }
//
//    public DatabaseReference get_userinfo_ref(String androidid) {
//        return get_userref(androidid).child("userinfo");
//    }
//
//    public DatabaseReference get_history_ref(String androidid) {
//        return get_userref(androidid).child("history");
//    }
//
//    public Profile get_user(String androidid) {
//        Profile profile = new Profile(androidid);
//
//        DatabaseReference currentuser_ref = user_ref.child(androidid);
//        DatabaseReference userinfo_ref = currentuser_ref.child("userinfo");
//        DatabaseReference history_ref = currentuser_ref.child("history");
//
//        DataSnapshot history_data = get_children(currentuser_ref, "history");
//
//        if (user_exists(androidid)) {
//            DataSnapshot loginqrcode = get_children(userinfo_ref, "loginqrcodee");
//
//            if (loginqrcode.getChildrenCount() == 0) {
//                profile.setUserName(get_child_value(userinfo_ref, "username"));
//                profile.setEmailAddress(get_child_value(userinfo_ref, "emailaddress"));
//                profile.setPhoneNumber(get_child_value(userinfo_ref, "phonenumber"));
//                profile.setScannedCodes(get_qr_list(androidid, history_data));
//
//            }
//
//        }
//        else {
//            add_child(currentuser_ref, "createdAt", get_current_time());
//            add_child(userinfo_ref, "username", get_default_username());
//            profile.setUserName(get_default_username());
//            add_child(userinfo_ref, "phonenumber", "null");
//            add_child(userinfo_ref, "emailaddress", "null");
//            add_child(userinfo_ref, "loginqrcode", "null");
//            history_ref.setValue("null");
//
//            add_child(username_ref, profile.getUserName(), profile.getAndroidID());
//        }
//        return profile;
//    }

//    public boolean update_userinfo(String androidid, String key, String value) {
//        DatabaseReference ref = user_ref.child(androidid).child("userinfo");
//
//        if (user_exists(androidid) == true) {
//            if (key.equals("username")) {
//
//            }
//            if (key.equals("emailaddress") || key.equals("phonenumber") || key.equals("username")) {
//                ref.child(key).setValue(value);
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * Function for getting the QR codes for all users, and calculating the relevant scores associated.
     * Used by the Leaderboard tab.
     * @return : A list of LeaderboardScoreBlock objects, which each hold scoring types for a specific user
     */
//    public ArrayList<LeaderboardScoreBlock> get_all_scoring_types(){
//        // Object to hold the various score types
//        ArrayList<LeaderboardScoreBlock> result = new ArrayList<LeaderboardScoreBlock>();
//        // Get all users from database
//        DataSnapshot users = this.get_children(database.getReference(), "user");
//        if(users.hasChildren()){
//            Iterable<DataSnapshot> children = users.getChildren();
//            for(DataSnapshot child : children){
//                // TODO: This currently fetches the user's hash
//                // Once the database has userinfo for all users this should be updated to
//                // actually fetch the username.
//                String username = child.getKey();
//                DataSnapshot qr_codes = child.child("history");
//                // Get total amount of qr codes a user has scanned. Cast from long
//                int totalScanned = (int)qr_codes.getChildrenCount();
//                int max = 0;
//                int totalScore = 0;
//                Iterable<DataSnapshot> qr_codes_iterable = qr_codes.getChildren();
//                for(DataSnapshot qr : qr_codes_iterable){
//                    DataSnapshot score = qr.child("score");
//                    int qrscore = 1;
////                    int qrscore = Integer.valueOf((String) score.getValue());
////                    score.getValue();
//                    if(qrscore > max) {
//                        max = qrscore;
//                    }
//                    totalScore += qrscore;
//                    }
//                result.add(new LeaderboardScoreBlock(username, username, max, totalScanned, totalScore));
//                }
//            }
//        return result;
//    }

//    public QRCodeList get_qr_list(String androidid, DataSnapshot data) {
//        QRCodeList qrlist = new QRCodeList();
//        if (data.getChildrenCount() > 0 ) {
//            data.getChildren().forEach((d) -> {
//                QRCODE qrcode = get_qrcode(androidid, d.getKey().toString());
//                qrlist.add(qrcode);
//            });
//        }
//        return qrlist;
//    }
//
//    public QRCODE get_qrcode(String androidid, String hash) {
//        QRCODE qrcode = new QRCODE();
//
//        DatabaseReference history_ref = user_ref.child(androidid).child("history");
//        DataSnapshot qrdata = get_children(history_ref, hash);
//        DatabaseReference qr_ref = qrdata.getRef();
//
//        if (user_exists(androidid)) {
//            if (qrdata.exists()) {
//                qrcode.setHash(qrdata.getKey());
//                qrcode.setisImported(Boolean.valueOf(get_child_value(qr_ref, "imported")));
//
//                if (!get_child_value(qr_ref, "location").equals("null")) {
//                    DatabaseReference location_ref = get_children(qr_ref, "location").getRef();
//
//                    Location location = new Location(LocationManager.GPS_PROVIDER);
//                    location.setLongitude(Double.valueOf(get_child_value(location_ref, "Longitude")));
//                    location.setLatitude(Double.valueOf(get_child_value(location_ref, "Latitude")));
//                    qrcode.setCodeLocation(location);
//                } else {
//                    qrcode.setCodeLocation(null);
//                }
//                qrcode.setScore(Integer.valueOf(get_child_value(qr_ref, "score")));
//            }
//        }
//        return qrcode;
//    }
//
//    public void add_qrcode(String androidid, QRCODE qrcode) {
//        String hash = qrcode.getHash();
//
//        DatabaseReference history_ref = user_ref.child(androidid).child("history");
//        DataSnapshot qrdata = get_children(history_ref, hash);
//        DatabaseReference qr_ref = qrdata.getRef();
//
//        if (user_exists(androidid)) {
//            if (!qrdata.exists()) {
//                add_child(history_ref, hash, "null");
//
//                add_child(qr_ref, "imported", String.valueOf(qrcode.getisImported()));
//                add_child(qr_ref, "score", String.valueOf(qrcode.getScore()));
//                add_child(qr_ref, "location", "null");
//
//
//                DatabaseReference location_ref = qr_ref.child("location");
//                if (qrcode.getCodeLocation() != null) {
//                    add_child(location_ref, "Longitude", String.valueOf(qrcode.getCodeLocation().getLongitude()));
//                    add_child(location_ref, "Latitude", String.valueOf(qrcode.getCodeLocation().getLatitude()));
//                }
//
//                add_child(qr_ref, "createdat", get_current_time());
//
//                DataSnapshot entry = get_children(global_qrcount, hash);
//                DataSnapshot users = get_children(entry, "users");
//                if (entry.getChildrenCount() == 0) {
//                    add_child(entry, "count", "1");
//                    add_child(users, androidid, "1");
//                }
//                else {
//                    add_child(users, androidid, "1");
//                    int count = Integer.valueOf(get_child_value(entry, "count"));
//
//                    add_child(entry, "count", String.valueOf(count + 1));
//                }
//
//            }
//
//        }
//    }


    private Boolean user_exists(String androidid) {
        DataSnapshot data = get_children(user_ref, androidid);

        return (data.exists()) ? true : false;
    }

    private DataSnapshot get_children(DatabaseReference reference, String key) {
        DataSnapshot data = null;

        Task<DataSnapshot> task = reference.child(key).get();

        wait(task);

        data = task.getResult();

        return data;
    }

    private void wait(Task<DataSnapshot> task) {
        while (!task.isComplete()) {};
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
        String string = "user" + String.valueOf(get_usercount() + 1);
        return string;
    }

    private int get_usercount() {
        return GLOBAL_REGISTRATIONTABLE.getChildrenCount();
//        GLOBAL_USERNAMETABLE
//        GLOBAL_USERNAMETABLE.getChildren()
//        System.out.println(GLOBAL_USERNAMETABLE.getChildren())
//
//        DataSnapshot data = null;
//        Task<DataSnapshot> task = username_ref.get();
//
//        while (!task.isComplete() && !task.isSuccessful()) {}
//        data = task.getResult();
//        return (int) data.getChildrenCount();
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