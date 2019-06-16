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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertBeacon(BeaconTable beaconTable);

    @Query("SELECT classRoomName FROM BeaconTable WHERE uuid like :uuid AND minor like :minor;")
    String getClassName(String uuid, String minor);

    @Query("SELECT classRoomName FROM BeaconTable WHERE id like :id;")
    String getClassNameById(int id);

    @Query("SELECT id FROM BeaconTable WHERE classRoomName like :className;")
    int getClassIdByName(String className);

    @Query("SELECT type FROM BeaconTable WHERE classRoomName like :className;")
    int getType(String className);

}
