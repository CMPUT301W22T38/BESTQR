package com.example.bestqr.Database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class EntryExist {

    /**
     * Checks if a user is registered
     * @param androidId - the id of the user getting checked
     * @return True if the user exists and False otherwise
     */
    public static boolean isRegistered(String androidId) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_REGISTRATIONTABLE.child(androidId);
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());

        return dataSnapshot.exists();
    }

    /**
     * checks if a user has scanned and stored a code
     * @param androidId - the id of the user getting checked
     * @return True if the user has a history of qrcodes false otherwise
     */
    public static boolean hasHistory(String androidId) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_HISTORYTABLE.child(androidId);
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());

        if (dataSnapshot.exists()) {
            return !dataSnapshot.getValue().equals("null");
        }
        return false;
    }

    /**
     * Checks it the user has a login qrcode
     * @param androidId - the id of the user getting checked
     * @return - True if the user has login qrcode and false otherwise
     */
    public static boolean hasLoginQRCode(String androidId) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_USERINFOTABLE.child(androidId).child("loginqrcode");
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());

        return !dataSnapshot.getValue().equals("null");
    }

    /**
     * checks if a qrcode has been scanned by a user already
     * @param androidId - the id of the user getting checked
     * @param hash - the hash of the qrcode
     * @return - True if the qrCode has been scaned by the user and false otherwise
     */
    public static boolean QRCodeAlreadyExist(String androidId, String hash) {
        DatabaseReference reference = ReferenceHolder.GLOBAL_HISTORYTABLE.child(androidId).child(hash);
        DataSnapshot dataSnapshot = DatabaseMethods.getDataSnapshot(reference.get());

        return (dataSnapshot.exists()) ? true : false;
    }

}
