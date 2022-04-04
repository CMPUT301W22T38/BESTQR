package com.example.bestqr.Database;

import android.graphics.Bitmap;

import com.example.bestqr.Owner;
import com.example.bestqr.QRCodeList;
import com.example.bestqr.models.Comment;
import com.example.bestqr.models.Comments;
import com.example.bestqr.models.Location;
import com.example.bestqr.models.Profile;
import com.example.bestqr.models.QRCODE;
import com.example.bestqr.models.TimeStamp;
import com.example.bestqr.ui.leaderboard.LeaderboardScoreBlock;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;


public class Database{

    /**
     *
     * @param androidId - the id of the user being retrieved
     * @return the complete profile of the user
     */
    public static Profile getUser(String androidId) {
        Profile profile = new Profile(androidId);

        if (EntryExist.isRegistered(androidId)) {
            if (EntryExist.hasLoginQRCode(androidId)) {

            } else {
                LoadInfo(profile);
                LoadHistory(profile);
            }
        } else {
            Register(profile);
        }
        return profile;
    }

    /**
     *
     * @param username - the username of the user
     * @return - the id of the user
     */
    public static String getAndroidIdByName(String username){
        String androidId = DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_USERNAMETABLE.child(username).get()).getValue().toString();
        return androidId;
    }

    /**
     * This method gets all codes that are in the game along with the
     * distinct locations that they have been scanned from
     * @return - the list of QrCodes in the game
     */
    public static QRCodeList getAllCodes(){
        QRCODE newQr;
        QRCodeList qrCodes = new QRCodeList();
        DatabaseReference reference = ReferenceHolder.GLOBAL_QRCODETABLE;
        for (DataSnapshot dataSnapshot : DatabaseMethods.getDataSnapshot(reference.get()).getChildren()) {
            DatabaseReference newReference = ReferenceHolder.GLOBAL_QRCODETABLE.child(dataSnapshot.getKey()).child("location");
            DataSnapshot newDataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());
            int score = Integer.valueOf(dataSnapshot.child("score").getValue().toString());
            if (newDataSnapshot.exists()){
                String allLocations = dataSnapshot.child("location").getValue().toString();
                String[] location = allLocations.split(",");

                for (String qrLocation: location) {
                    newQr = new QRCODE();
                    newQr.setScore(score);
                    if (qrLocation != "") {
                        String[] latLng = qrLocation.split(";");
                        Location scannedLocation = new Location(Double.valueOf(latLng[0]), Double.valueOf(latLng[1]));
                        newQr.setCodeLocation(scannedLocation);
                        newQr.setScore(score);
                        qrCodes.add(newQr);
                    }
                }
            }

        }
        return qrCodes;

    }

    /**
     * Registers the owner to the database
     * @param owner - the admin of the game
     */
    public static void registerOwner(Owner owner){
        owner.setUserName("Owner");
        owner.setAndroidId("This is Owner Id");
        QRCODE ownerQr = new QRCODE(owner.getAndroidId());
        String ownerHash = ownerQr.getHash();
        owner.setAndroidId(ownerHash);
        owner.setPhoneNumber("null");
        owner.setEmailAddress("null");

        ReferenceHolder.GLOBAL_OWNER.child(ownerHash).child("username").setValue(owner.getUserName());
        ReferenceHolder.GLOBAL_OWNER.child(ownerHash).child("phoneNumber").setValue(owner.getPhoneNumber());
        ReferenceHolder.GLOBAL_OWNER.child(ownerHash).child("emailAddress").setValue(owner.getEmailAddress());
        ReferenceHolder.GLOBAL_OWNER.child(ownerHash).child("id").setValue(ownerHash);
    }

    /**
     * checks if a user is an owner
     * @param androidId - the id of the user
     * @return - True or false if the user is the owner
     */
    public static boolean isOwner(String androidId) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_OWNER.child(androidId);
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());

        return (dataSnapshot.exists()) ? true : false;
    }

    /**
     * Gets the owner object
     * @param owner - an owner object that has the owner id
     * @return - The complete owner object
     */
    public static Owner getOwner(Owner owner){
        owner.setUserName(DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_OWNER.child(owner.getAndroidId()).child("username").get()).getValue().toString());
        owner.setPhoneNumber(DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_OWNER.child(owner.getAndroidId()).child("phoneNumber").get()).getValue().toString());
        owner.setEmailAddress(DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_OWNER.child(owner.getAndroidId()).child("emailAddress").get()).getValue().toString());
      return owner;
    }

    /**
     * gets the id of the owner
     * @return - the id of the owner
     */
    public static String getOwnerId(){
        return ReferenceHolder.GLOBAL_OWNER.getKey();
    }

    /**
     * Registers a user
     * @param profile - the profile to be registered in the database
     */
    public static void Register(Profile profile) {
        String createdTime = TimeStamp.currentTime();

        profile.setUserName(getDefaultUserName());
        profile.setPhoneNumber("null");
        profile.setEmailAddress("null");

        ReferenceHolder.GLOBAL_REGISTRATIONTABLE.child(profile.getAndroidId()).setValue(createdTime);
        ReferenceHolder.GLOBAL_USERNAMETABLE.child(profile.getUserName()).setValue(profile.getAndroidId());


        ReferenceHolder.GLOBAL_USERINFOTABLE.child(profile.getAndroidId()).child("username").setValue(profile.getUserName());
        ReferenceHolder.GLOBAL_USERINFOTABLE.child(profile.getAndroidId()).child("emailaddress").setValue("null");
        ReferenceHolder.GLOBAL_USERINFOTABLE.child(profile.getAndroidId()).child("phonenumber").setValue("null");
        ReferenceHolder.GLOBAL_USERINFOTABLE.child(profile.getAndroidId()).child("loginqrcode").setValue("null");

        ReferenceHolder.GLOBAL_HISTORYTABLE.child(profile.getAndroidId()).setValue("null");

  }

    /**
     * loads the profile information into the profile object
      * @param profile - The profile object with the loaded information
     */
    public static void LoadInfo(Profile profile) {
        profile.setUserName(DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_USERINFOTABLE.child(profile.getAndroidId()).child("username").get()).getValue().toString());
        profile.setPhoneNumber(DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_USERINFOTABLE.child(profile.getAndroidId()).child("phonenumber").get()).getValue().toString());
        profile.setEmailAddress(DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_USERINFOTABLE.child(profile.getAndroidId()).child("emailaddress").get()).getValue().toString());
    }

    /**
     * Loads the history of qrcodes scanned to the profile object
     * @param profile- The complete profile object with the previously scanned qrCodes
     */
    public static void LoadHistory(Profile profile) {
        QRCodeList qrCodeList = null;
        String androidId = profile.getAndroidId();
        if (EntryExist.hasHistory(androidId)) {
            qrCodeList = new QRCodeList();

            DatabaseReference reference = ReferenceHolder.GLOBAL_HISTORYTABLE.child(androidId);

            for (DataSnapshot dataSnapshot : DatabaseMethods.getDataSnapshot(reference.get()).getChildren()) {
                String key = dataSnapshot.getKey();

                Bitmap bitmap = Storage.download(androidId, key);
                QRCODE qrcode = new QRCODE(key, bitmap);
                qrcode.setTimestamp(dataSnapshot.child("createdat").getValue().toString());
                qrcode.setScore(Integer.valueOf(dataSnapshot.child("score").getValue().toString()));
                qrcode.setComments(getAllComments(profile.getAndroidId(),qrcode.getHash()));

                DataSnapshot locationsnapshot = dataSnapshot.child("location");

                if (!locationsnapshot.getValue().toString().equals("null")) {
                    double latitude = Double.valueOf(locationsnapshot.child("coordinates/latitude").getValue().toString());
                    double longitude = Double.valueOf(locationsnapshot.child("coordinates/longitude").getValue().toString());

                    qrcode.setCodeLocation(new Location(latitude, longitude));
                }
                qrCodeList.add(qrcode);
            }
            profile.setScannedCodes(qrCodeList);
        }
    }

    /**
     * Calculate the total number of users registered in the game
     * @return - the number of users registered
     */
    public static int getTotalUserCount() {
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_REGISTRATIONTABLE.get());
        return (int) dataSnapshot.getChildrenCount();
    }

    /**
     * gets a default username for a new user
     * @return - the default username for a user
     */
    public static String getDefaultUserName() {
        String username = "user" + String.valueOf(getTotalUserCount());

        int i = 0;
        while (userNameExist(username)) {
            username = "user" + String.valueOf(getTotalUserCount() + i);
            i++;
        }
        return username;
    }

    /**
     * Gets a leaderboard score block
     * @return - returns the information for the users to be displayed on the leaderboard
     */
    public static ArrayList<LeaderboardScoreBlock> get_all_scoring_types() {
        ArrayList<LeaderboardScoreBlock> result = new ArrayList<LeaderboardScoreBlock>();

        for (DataSnapshot dataSnapshot : DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_USERINFOTABLE.get()).getChildren()) {
            String androidId = dataSnapshot.getKey();
            String username = dataSnapshot.child("username").getValue().toString();

            DataSnapshot dataSnapshot1 = DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_HISTORYTABLE.child(androidId).get());

            int num = (int) dataSnapshot1.getChildrenCount();
            int totalscore = 0;
            int highest = 0;
            ArrayList<Integer> arr = null;

//            if (hasHistory(androidId)) {
            if (num != 0) {
                arr = new ArrayList<>();
                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                    arr.add(Integer.valueOf(dataSnapshot2.child("score").getValue().toString()));
                }
                highest = Collections.max(arr);
                totalscore = arr.stream().collect(Collectors.summingInt(Integer::intValue));
            }
            result.add(new LeaderboardScoreBlock(androidId, username, num, totalscore, highest));
        }
        return result;
    }

    /**
     * adds a qr code to the database based on the user
     * @param androidId - the id of the user who is adding a qrcode
     * @param qrcode - the qrcode that is to be added
     * @return - True if the addition was successful and False otherwise
     */
    public static boolean addQRCode(String androidId, QRCODE qrcode) {
        // double checking //
        if (!EntryExist.QRCodeAlreadyExist(androidId, qrcode.getHash())) {
            ReferenceHolder.GLOBAL_HISTORYTABLE.child(androidId).child(qrcode.getHash()).child("createdat").setValue(qrcode.getScannedTime());
            ReferenceHolder.GLOBAL_HISTORYTABLE.child(androidId).child(qrcode.getHash()).child("score").setValue(qrcode.getScore());
            ReferenceHolder.GLOBAL_HISTORYTABLE.child(androidId).child(qrcode.getHash()).child("imported").setValue(qrcode.getisImported());
            ReferenceHolder.GLOBAL_QRCODETABLE.child(qrcode.getHash()).child("score").setValue(qrcode.getScore());
            ReferenceHolder.GLOBAL_HISTORYTABLE.child(androidId).child(qrcode.getHash()).child("location").setValue("null");

            if (qrcode.getCodeLocation() != null) {
                ReferenceHolder.GLOBAL_HISTORYTABLE.child(androidId).child(qrcode.getHash()).child("location").setValue(qrcode.getCodeLocation().toMap());
                ReferenceHolder.GLOBAL_QRLOCATION.child(androidId).child(qrcode.getHash()).setValue(qrcode.getCodeLocation().toMap());
                DatabaseReference reference = ReferenceHolder.GLOBAL_QRCODETABLE.child(qrcode.getHash()).child("location");
                DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());
                String location = "";
                if (dataSnapshot.exists()){
                    location += DatabaseMethods.getDataSnapshot(reference.get()).getValue().toString();
                    ReferenceHolder.GLOBAL_QRCODETABLE.child(qrcode.getHash()).child("location").setValue(location + qrcode.getCodeLocation().getLatitude() + ";" + qrcode.getCodeLocation().getLongitude()+",");
                }
                else{
                    ReferenceHolder.GLOBAL_QRCODETABLE.child(qrcode.getHash()).child("location").setValue(qrcode.getCodeLocation().getLatitude() + ";" + qrcode.getCodeLocation().getLongitude()+",");
                }

            }

            ReferenceHolder.GLOBAL_QRCODETABLE.child(qrcode.getHash()).child("users").child(androidId).setValue(qrcode.getScannedTime());
            updateAssociatedUserCount(qrcode.getHash());

            Storage.upload(qrcode, androidId);
            return true;
        }
        return false;
    }

    /**
     * removes the existence of a qrcode from the users history
     * @param androidId - the id of the user whose code is being deleted
     * @param qrcode - the qrcode that is being deleted
     * @return - True if the deletion was successful and False otherwise
     */
    public static boolean removeQrCode(String androidId, QRCODE qrcode){
        if (EntryExist.QRCodeAlreadyExist(androidId, qrcode.getHash())) {
            ReferenceHolder.GLOBAL_HISTORYTABLE.child(androidId).child(qrcode.getHash()).removeValue();

            ReferenceHolder.GLOBAL_QRCODETABLE.child(qrcode.getHash()).child("users").child(androidId).removeValue();
            updateAssociatedUserCount(qrcode.getHash());

            return true;
        }
        return false;
    }

    /**
     * updates the nuber of customers who have scanned a particular code
     * @param hash - the hash of the qrcode
     */
    public static void updateAssociatedUserCount(String hash) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_QRCODETABLE.child(hash).child("users");
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());

        ReferenceHolder.GLOBAL_QRCODETABLE.child(hash).child("count").setValue(dataSnapshot.getChildrenCount());
//        reference.setValue(Integer.valueOf(dataSnapshot.getValue().toString()) + 1);
    }

    /**
     *
     * @param field - the of information being changed (username, emailaddress,phonenumber)
     * @param androidId- the id of the user
     * @param oldvalue - the old value of the field
     * @param newvalue - the new value of the field
     * @return True if the change is successful and False otherwise
     */
    public static boolean ChangeUserInfo(String field, String androidId, String oldvalue, String newvalue) {
        //FirebaseApp.initializeApp(FirebaseApp.getInstance().getApplicationContext());
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

        ReferenceHolder.GLOBAL_USERINFOTABLE.child(androidId).child(field).setValue(newvalue);
        return true;
    }

    /**
     * checks if the username already exists in the database
     * @param value - the username that is being checked
     * @return - True if the username exists and False otherwise
     */
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

    /**
     * gets the users who have scanned a particular code
     * @param hash - the hash of the qrcode
     * @return - the usernames of users who have scanned a particular code
     */
    public static ArrayList<String> getAssociatedUsers(String hash) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_QRCODETABLE.child(hash).child("users");
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());
        ArrayList<String> associatedUsers = new ArrayList<>();

        for (DataSnapshot data : dataSnapshot.getChildren()) {
            DatabaseReference username_reference = ReferenceHolder.GLOBAL_USERINFOTABLE.child(data.getKey()).child("username");
            String username = DatabaseMethods.getDataSnapshot(username_reference.get()).getValue().toString();
            associatedUsers.add(username);
        }

        return associatedUsers;
    }


    /**
     * Removes a player from all instances of the database
     * @param profile - the player that is getting removed
     */
    public static void deletePlayer(Profile profile){
        if (EntryExist.isRegistered(profile.getAndroidId())){
            ReferenceHolder.GLOBAL_USERINFOTABLE.child(profile.getAndroidId()).removeValue();
            ReferenceHolder.GLOBAL_USERNAMETABLE.child(profile.getUserName()).removeValue();
            ReferenceHolder.GLOBAL_REGISTRATIONTABLE.child(profile.getAndroidId()).removeValue();
            ReferenceHolder.GLOBAL_HISTORYTABLE.child(profile.getAndroidId()).removeValue();
            ReferenceHolder.GLOBAL_REGISTRATIONTABLE.child(profile.getAndroidId()).removeValue();

            for (QRCODE item: profile.getScannedCodes()) {
                ReferenceHolder.GLOBAL_QRCODETABLE.child(item.getHash()).child("users").child(profile.getAndroidId()).removeValue();
                updateAssociatedUserCount(item.getHash());
            }

        }
    }

    /**
     * Remove a qrcode from all instances of the database
     * @param qrcode - thr qrcode to be removed
     */
    public static void removeAllQRCode(QRCODE qrcode){
        DatabaseReference reference = ReferenceHolder.GLOBAL_QRCODETABLE.child(qrcode.getHash()).child("users");
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());

        for (DataSnapshot data : dataSnapshot.getChildren()) {
            removeQrCode(Database.getUser(data.getKey()).getAndroidId(), qrcode);
        }
        try{
        ReferenceHolder.GLOBAL_QRCODETABLE.child(qrcode.getHash()).removeValue();
        }catch (Exception e){

        }

    }


    /**
     * Retrieve the nearby codes
     * @param location - the location os the user
     * @param radius- the radius defined as nearby
     */
    public static void getNearBy(Location location, double radius) {

//        ArrayList<Location> arr = new ArrayList<>();

        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_QRLOCATION.get());
        for (DataSnapshot children: dataSnapshot.getChildren()) {
            for (DataSnapshot children1 : children.getChildren()) {
                DataSnapshot coordinates_datasnapshot =
                        DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_QRLOCATION.child(children.getKey()).child(children1.getKey()).child("coordinates").get());
                double latitude = Double.valueOf(coordinates_datasnapshot.child("latitude").getValue().toString());
                double longitude = Double.valueOf(coordinates_datasnapshot.child("longitude").getValue().toString());
//                System.out.println(GeoUtils.distance(location.getLatitude(), location.getLatitude(), latitude, longitude));
//                arr.add(new Location(longitude, latitude));
            }
        }
    }

    /**
     * Adds a comment to a qrCode
     * @param androidId - the id of the user who is writing
     * @param hash - the hash of the qrcode
     * @param comment - the comment to ba added
     */
    public static void addComment(String androidId, String hash, Comment comment) {

        if (EntryExist.QRCodeAlreadyExist(androidId, hash)) {
            DataSnapshot comments_datasnapshot =
                    DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_HISTORYTABLE.child(androidId).child(hash).child("comments").get());

            int comment_num = (int) comments_datasnapshot.getChildrenCount();
            comments_datasnapshot.child(String.valueOf(comment_num)).getRef().setValue(comment.toMap());
        }
    }

    public static void deleteComment(String androidId, String hash, String contents, String writtenby) {
        if (EntryExist.QRCodeAlreadyExist(androidId, hash)) {
            DataSnapshot comments_datasnapshot =
                    DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_HISTORYTABLE.child(androidId).child(hash).child("comments").get());

            for (DataSnapshot dataSnapshot : comments_datasnapshot.getChildren()) {
                String db_contents = dataSnapshot.child("contents").getValue().toString();
                String db_writtenby = dataSnapshot.child("writtenby").getValue().toString();

                if (db_contents.equals(contents) && db_writtenby.equals(writtenby)) {
                    comments_datasnapshot.child(dataSnapshot.getKey()).getRef().setValue("deleted");

                    break;
                }
            }
        }
    }

    /**
     * Gets all the comments associated with a qrcode
     * @param androidId - the id of the user who scanned the comment
     * @param hash - the hash of the qrcode
     * @return - Comments object that has all the comments associated with the qrcode
     */
    public static Comments getAllComments(String androidId, String hash) {
        Comments comments = null;

        if (EntryExist.QRCodeAlreadyExist(androidId, hash)) {
            comments = new Comments();
            DataSnapshot comments_datasnapshot =
                    DatabaseMethods.getDataSnapshot(ReferenceHolder.GLOBAL_HISTORYTABLE.child(androidId).child(hash).child("comments").get());

            for (DataSnapshot dataSnapshot : comments_datasnapshot.getChildren()) {
                if (dataSnapshot.getValue().toString().equals("deleted")) {
                    comments.add(new Comment());
                }
                else {
                    String contents = dataSnapshot.child("contents").getValue().toString();
                    String writtenby = dataSnapshot.child("writtenby").getValue().toString();
                    String timeStamp = dataSnapshot.child("createdat").getValue().toString();

                    comments.add(new Comment(contents, writtenby, timeStamp));
                }

            }
        }
        return comments;
    }
}
