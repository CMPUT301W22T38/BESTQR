package com.example.bestqr;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database extends AppCompatActivity {
    private FirebaseDatabase db;
    private DatabaseReference root_ref;
    private DatabaseReference users_ref;


    public Database () {
        db = FirebaseDatabase.getInstance();
        users_ref = db.getReference().child("key_Test");
        users_ref.setValue("val");

    }
}
