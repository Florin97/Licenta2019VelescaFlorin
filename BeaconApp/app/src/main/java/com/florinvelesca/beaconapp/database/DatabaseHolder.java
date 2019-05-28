package com.florinvelesca.beaconapp.database;

import android.arch.persistence.room.Room;
import android.content.Context;



public class DatabaseHolder {
    private static AppDatabase database;

    public static AppDatabase getDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "my_database").build();
        }
        return database;
    }
}
