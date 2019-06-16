package com.florinvelesca.beaconapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;


@Entity(indices = {@Index(value = {"uuid","minor"}, unique = true)})
public class BeaconTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "uuid")
    private String uuid;

    @ColumnInfo(name = "minor")
    private String minor;

    @ColumnInfo(name = "classRoomName")
    private String classRoomName;



    private int roomId;

    private int floor;

    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }
    public String getClassRoomName() {
        return classRoomName;
    }

    public void setClassRoomName(String classRoomName) {
        this.classRoomName = classRoomName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeaconTable that = (BeaconTable) o;
        return id == that.id &&
                roomId == that.roomId &&
                floor == that.floor &&
                uuid.equals(that.uuid) &&
                minor.equals(that.minor) &&
                classRoomName.equals(that.classRoomName) &&
                type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, minor, classRoomName, roomId, floor, type);
    }

    @Override
    public String toString() {
        return "BeaconTable{" +
                "id=" + id +
                ", roomId=" + roomId +
                ", floor=" + floor +
                ", type='" + type + '\'' +
                '}';
    }
}
