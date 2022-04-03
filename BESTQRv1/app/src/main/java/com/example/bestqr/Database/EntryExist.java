package com.example.bestqr.Database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class EntryExist {

    public static boolean isRegistered(String androidId) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_REGISTRATIONTABLE.child(androidId);
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());

        return dataSnapshot.exists();
    }
    public static boolean hasHistory(String androidId) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_HISTORYTABLE.child(androidId);
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());

        return !dataSnapshot.getValue().equals("null");
    }

    public static boolean hasLoginQRCode(String androidId) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_USERINFOTABLE.child(androidId).child("loginqrcode");
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());

        return !dataSnapshot.getValue().equals("null");
    }

    public static boolean QRCodeAlreadyExist(String androidId, String hash) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_HISTORYTABLE.child(androidId).child(hash);
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());

        return (dataSnapshot.exists()) ? true : false;
    }

}
