//package com.example.bestqr;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class DatabaseTable {
//
//    private DatabaseReference table;
//
//    public DatabaseTable(DatabaseReference database) {
//        this.table = database;
//    }
//
//    public DatabaseTable(DatabaseReference database, String path) {
//        this.table = database.child(path);
//    }
//
//    public DatabaseTable(DatabaseTable table, String path) {
//        this.table = table.getRef().child(path);
////        this.table = table.get
//    }
//
//    public boolean exists(String key) {
//        boolean bool = false;
//        DataSnapshot snapshot = DatabaseMethods.getDataSnapshot(table.child(key).get());
//        if (snapshot.exists()) {
//            bool = true;
//        }
//        return bool;
//    }
//
//    public void add(String key, String value) {
//        table.child(key).setValue(value);
//    }
//
//    public void add(String... args) {
//        ArrayList<String> arglist = new ArrayList<String>(Arrays.asList(args));
//
//        String value = arglist.remove(arglist.size() - 1);
//
//        String key = String.join("/", arglist);
//        this.add(key, value);
//    }
//
//    public String get(String key) {
//        String value = null;
//        DataSnapshot snapshot = DatabaseMethods.getDataSnapshot(table.child(key).get());
//        if (snapshot.exists()) {
//            value = snapshot.getValue().toString();
//        }
//        return value;
//    }
//
//    public String get(String... args) {
//        DatabaseReference reference = null;
//        String key = String.join("/", args);
//
//        return get(key);
//    }
//
//    public DatabaseReference getRef(String key) {
//        return table.child(key);
//    }
//
//    public DatabaseReference getRef(String... args) {
//        String key = String.join("/", args);
//        return table.child(key);
//    }
//
//    public DatabaseReference getRef() {
//        return this.table;
//    }
//
//    static public DataSnapshot getDataSnapshot(DatabaseReference reference) {
//        Task<DataSnapshot> task = reference.get();
//
//        while (!task.isComplete() && !task.isSuccessful()) {}
//
//        return task.getResult();
//    }
//
//
//    public boolean hasChildren(String... args) {
//        boolean bool = true;
//        DataSnapshot snapshot = getDataSnapshot(getRef(args));
//        if (!snapshot.exists()) {
//            bool = false;
//        }
//        else if (snapshot.getValue().toString().equals("null")) {
//            bool = false;
//        }
//        return bool;
//    }
//
//    public Iterable<DataSnapshot> getChildren() {
//        return getDataSnapshot(table).getChildren();
//    }
//
//    public Iterable<DataSnapshot> getChildren(String key) {
//        return getDataSnapshot(table.child(key)).getChildren();
//    }
//
//    public Iterable<DataSnapshot> getChildren(String... args) {
//        String key = String.join("/", args);
//        return getDataSnapshot(table.child(key)).getChildren();
//    }
//
//    public int getChildrenCount() {
//        return (int) getDataSnapshot(table).getChildrenCount();
//    }
//
//
//}
