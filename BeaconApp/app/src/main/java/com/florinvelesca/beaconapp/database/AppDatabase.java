package com.florinvelesca.beaconapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {BeaconTable.class, BeaconLink.class}, version = 2,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BeaconDao beaconDao();

    public abstract BeaconLinkDao beaconLinkDao();
}
