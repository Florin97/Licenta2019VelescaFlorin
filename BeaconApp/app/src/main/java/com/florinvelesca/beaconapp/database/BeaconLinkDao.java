package com.florinvelesca.beaconapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface BeaconLinkDao {

    @Query("SELECT * FROM beaconlink")
    List<BeaconLink> getAllBeaconLinks();



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLink(BeaconLink link);
}
