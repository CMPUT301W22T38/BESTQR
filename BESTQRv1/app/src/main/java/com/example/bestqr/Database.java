package com.example.bestqr;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {
    private FirebaseDatabase db;
    private DatabaseReference root_ref;
    private DatabaseReference users_ref;


    public Database () {
        db = FirebaseDatabase.getInstance();
        users_ref = db.getReference().child("key_Test");
        users_ref.setValue("val");

    }
}
