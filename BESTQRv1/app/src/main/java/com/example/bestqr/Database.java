package com.example.bestqr;

import android.graphics.Bitmap;

import com.example.bestqr.models.BaseProfile;
import com.example.bestqr.models.Profile;
import com.example.bestqr.models.QRCODE;
import com.example.bestqr.models.TimeStamp;
import com.example.bestqr.ui.leaderboard.LeaderboardScoreBlock;
import com.example.bestqr.utils.QRmethods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.sql.Ref;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class Database {
    public static class ReferenceHolder {
        public static FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();

        public static DatabaseReference GLOBAL_QRCODETABLE = DATABASE.getReference().child("qrcode");
        public static DatabaseReference GLOBAL_REGISTRATIONTABLE = DATABASE.getReference().child("registeredandroidid");
        public static DatabaseReference GLOBAL_USERNAMETABLE = DATABASE.getReference().child("username");
        public static DatabaseReference GLOBAL_USERTABLE = DATABASE.getReference().child("user");

        public static Storage STORAGE;
    }

    ReferenceHolder referenceHolder;

    public static Profile getUser(String androidId) {
        Profile profile = new Profile(androidId);

        if (isRegistered(androidId)) {
            if (hasLoginQRCode(androidId)) {

            } else {
                LoadInfo(profile);
                LoadHistory(profile);
            }
        } else {
            Register(profile);
        }

        return profile;
    }

    public static void Register(Profile profile) {
        String createdTime = TimeStamp.currentTime();

        profile.setUserName(getDefaultUserName());
        profile.setPhoneNumber("null");
        profile.setEmailAddress("null");

        ReferenceHolder.GLOBAL_REGISTRATIONTABLE.child(profile.getAndroidId()).setValue(createdTime);
        ReferenceHolder.GLOBAL_USERNAMETABLE.child(profile.getUserName()).setValue(profile.getAndroidId());

        ReferenceHolder.GLOBAL_USERTABLE.child(profile.getAndroidId()).child("createdAt").setValue(createdTime);
        ReferenceHolder.GLOBAL_USERTABLE.child(profile.getAndroidId()).child("history").setValue("null");
        ReferenceHolder.GLOBAL_USERTABLE.child(profile.getAndroidId()).child("userinfo/username").setValue(profile.getUserName());
        ReferenceHolder.GLOBAL_USERTABLE.child(profile.getAndroidId()).child("userinfo/emailaddress").setValue("null");
        ReferenceHolder.GLOBAL_USERTABLE.child(profile.getAndroidId()).child("userinfo/phonenumber").setValue("null");
        ReferenceHolder.GLOBAL_USERTABLE.child(profile.getAndroidId()).child("userinfo/loginqrcode").setValue("null");
    }

    public static void LoadInfo(Profile profile) {
        profile.setUserName(DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_USERTABLE.child(profile.getAndroidId()).child("userinfo/username").get()).getValue().toString());
        profile.setPhoneNumber(DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_USERTABLE.child(profile.getAndroidId()).child("userinfo/phonenumber").get()).getValue().toString());
        profile.setEmailAddress(DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_USERTABLE.child(profile.getAndroidId()).child("userinfo/emailaddress").get()).getValue().toString());
    }

    public static void LoadHistory(Profile profile) {
        QRCodeList qrCodeList = null;
        if (hasHistory(profile.getAndroidId())) {
            DatabaseReference reference = ReferenceHolder.GLOBAL_USERTABLE.child(profile.getAndroidId()).child("history");

            qrCodeList = new QRCodeList();
            for (DataSnapshot dataSnapshot : DatabaseMethods.getDataSnapshot(reference.get()).getChildren()) {
                String key = dataSnapshot.getKey();
                Bitmap bitmap = Storage.download(profile.getAndroidId(), key);
                QRCODE qrcode = new QRCODE(key, bitmap);

                qrcode.setTimestamp(dataSnapshot.child("createdat").getValue().toString());
                qrcode.setScore(Integer.valueOf(dataSnapshot.child("score").getValue().toString()));
                qrCodeList.add(qrcode);

            }
            profile.setScannedCodes(qrCodeList);
        }
    }

    public static boolean hasHistory(String androidId) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_USERTABLE.child(androidId).child("history");
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());

        return dataSnapshot.getValue().equals("null") ? false : true;
    }

    public static boolean isRegistered(String androidId) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_REGISTRATIONTABLE.child(androidId);
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());

        return (dataSnapshot.exists()) ? true : false;
    }

    public static boolean hasLoginQRCode(String androidId) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_USERTABLE.child(androidId).child("userinfo/loginqrcode");
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());

        return dataSnapshot.getValue().equals("null") ? false : true;
    }

    public static int getTotalUserCount() {
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_REGISTRATIONTABLE.get());
        return (int) dataSnapshot.getChildrenCount();
    }

    public static String getDefaultUserName() {
        String username = "user" + String.valueOf(getTotalUserCount());

        int i = 0;
        while (userNameExist(username)) {
            username = "user" + String.valueOf(getTotalUserCount() + i);
            i++;
        }
        return username;
    }

    public static ArrayList<LeaderboardScoreBlock> get_all_scoring_types() {
        ArrayList<LeaderboardScoreBlock> result = new ArrayList<LeaderboardScoreBlock>();

        for (DataSnapshot dataSnapshot : DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_USERTABLE.get()).getChildren()) {
            String androidId = dataSnapshot.getKey();

            String username = dataSnapshot.child("userinfo/username").getValue().toString();
            //            BaseProfile profile = new BaseProfile(androidid, username);

            int num = (int) dataSnapshot.child("history").getChildrenCount();
            int totalscore = 0;
            int highest = 0;
            ArrayList<Integer> arr = new ArrayList<>();

            if (num != 0) {
                for (DataSnapshot qrcodedataSnapshot : dataSnapshot.child("history").getChildren()) {
                    arr.add(Integer.valueOf(qrcodedataSnapshot.child("score").getValue().toString()));
                }
                highest = Collections.max(arr);
                totalscore = arr.stream().collect(Collectors.summingInt(Integer::intValue));

            }
            result.add(new LeaderboardScoreBlock(androidId, username, num, totalscore, highest));
        }
        return result;
    }

    public static boolean addQRCode(String androidId, QRCODE qrcode) {
        // double checking //
        if (!QRCodeAlreadyExist(androidId, qrcode.getHash())) {
            ReferenceHolder.GLOBAL_USERTABLE.child(androidId).child("history").child(qrcode.getHash()).child("createdat").setValue(qrcode.getScannedTime());
            ReferenceHolder.GLOBAL_USERTABLE.child(androidId).child("history").child(qrcode.getHash()).child("score").setValue(qrcode.getScore());

            ReferenceHolder.GLOBAL_USERTABLE.child(androidId).child("history").child(qrcode.getHash()).child("imported").setValue(qrcode.getisImported());

            if (qrcode.getCodeLocation() == null) {
                ReferenceHolder.GLOBAL_USERTABLE.child(androidId).child("history").child(qrcode.getHash()).child("location").setValue("null");
            } else {
                ReferenceHolder.GLOBAL_USERTABLE.child(androidId).child("history").child(qrcode.getHash()).child("location/longitude").setValue(qrcode.getCodeLocation().getLongitude());
                ReferenceHolder.GLOBAL_USERTABLE.child(androidId).child("history").child(qrcode.getHash()).child("location/latitude").setValue(qrcode.getCodeLocation().getLatitude());
            }

            ReferenceHolder.GLOBAL_USERTABLE.child(androidId).child("history").child(qrcode.getHash()).child("location").setValue(qrcode.getScannedTime());


            ReferenceHolder.GLOBAL_QRCODETABLE.child(qrcode.getHash()).child("users").child(androidId).setValue(qrcode.getScannedTime());
            updateAssociatedUserCount(qrcode.getHash());
//            ReferenceHolder.GLOBAL_QRCODETABLE.child(qrcode.getHash()).child("count").setValue(getAssociatedUserCount(qrcode.getHash()) + 1);

            Storage.upload(qrcode, androidId);
            return true;
        }
        return false;
    }

    public static boolean QRCodeAlreadyExist(String androidId, String hash) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_USERTABLE.child(androidId).child("history").child(hash);
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());


        return (dataSnapshot.exists()) ? true : false;
    }

    public static void updateAssociatedUserCount(String hash) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_QRCODETABLE.child(hash).child("users");
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());

        ReferenceHolder.GLOBAL_QRCODETABLE.child(hash).child("count").setValue(dataSnapshot.getChildrenCount());
//        reference.setValue(Integer.valueOf(dataSnapshot.getValue().toString()) + 1);
    }

    public static boolean ChangeUserInfo(String field, String androidId, String oldvalue, String newvalue) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_USERNAMETABLE.child(newvalue);
        if (newvalue == null || newvalue.equals("")) {
            return false;
        }
        if (field.equals("username")) {
            if (!userNameExist(newvalue)) {
                ReferenceHolder.GLOBAL_USERNAMETABLE.child(oldvalue).removeValue();
                reference.setValue(androidId);
            } else {
                return false;
            }
        }

        ReferenceHolder.GLOBAL_USERTABLE.child(androidId).child("userinfo").child(field).setValue(newvalue);

        return true;
    }

    public static boolean userNameExist(String value) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_USERNAMETABLE.child(value);
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());

        return dataSnapshot.exists() ? true : false;
    }


//    public static int getAssociatedUserCount(String hash) {
//        DatabaseReference reference = ReferenceHolder.GLOBAL_USERTABLE.child(hash).child("users");
//        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());
//
//        return (int) dataSnapshot.getChildrenCount();
//    }

//    public static boolean hasAssociatedUsers(String hash) {
//        DatabaseReference reference = ReferenceHolder.GLOBAL_USERTABLE.child(hash).child("users");
//        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());
//
//        return (dataSnapshot.exists()) ? true : false;
//    }

    public static ArrayList<String> getAssociatedUsers(String hash) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_QRCODETABLE.child(hash).child("users");
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());
        ArrayList<String> associatedUsers = new ArrayList<>();

        for (DataSnapshot data : dataSnapshot.getChildren()) {
            associatedUsers.add(data.getKey());
        }

        return associatedUsers;
    }

}
