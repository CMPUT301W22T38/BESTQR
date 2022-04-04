package com.example.bestqr.Database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

public class DatabaseMethods {

    /**
     * Gets the snapshot of a database refrence
     * @param task - the task
     * @return a Data Snapshot object
     */
    public static DataSnapshot getDataSnapshot(Task<DataSnapshot> task) {
        while (!task.isComplete() && !task.isSuccessful()) {}

        return task.getResult();
    }

}
