package com.florinvelesca.beaconapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface BeaconDao {
    @Query("SELECT * FROM BeaconTable")
    List<BeaconTable> getAllBeacons();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBeacon(BeaconTable beaconTable);

    @Query("SELECT classRoomName FROM BeaconTable WHERE uuid like :uuid AND minor like :minor;")
    String getClassName(String uuid, String minor);
}
