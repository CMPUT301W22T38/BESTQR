package com.example.bestqr.Database;

import com.example.bestqr.Storage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReferenceHolder {
    public static FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();

    public static DatabaseReference GLOBAL_REGISTRATIONTABLE = DATABASE.getReference().child("registeredandroidid");
    public static DatabaseReference GLOBAL_USERINFOTABLE = DATABASE.getReference().child("userinfo");
    public static DatabaseReference GLOBAL_USERNAMETABLE = DATABASE.getReference().child("username");
    public static DatabaseReference GLOBAL_HISTORYTABLE = DATABASE.getReference().child("history");
    public static DatabaseReference GLOBAL_QRCODETABLE = DATABASE.getReference().child("qrcode");
    public static DatabaseReference GLOBAL_OWNER = DATABASE.getReference().child("Owner");

    public static DatabaseReference GLOBAL_QRLOCATION = DATABASE.getReference().child("qrlocation");

    public static Storage STORAGE;
}