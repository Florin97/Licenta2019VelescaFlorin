package com.florinvelesca.beaconapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ClassRoomCoordonates implements Parcelable {
    String name;
    float x;
    float y;

    protected ClassRoomCoordonates(Parcel in) {
        name = in.readString();
        x = in.readFloat();
        y = in.readFloat();
    }

    public static final Creator<ClassRoomCoordonates> CREATOR = new Creator<ClassRoomCoordonates>() {
        @Override
        public ClassRoomCoordonates createFromParcel(Parcel in) {
            return new ClassRoomCoordonates(in);
        }

        @Override
        public ClassRoomCoordonates[] newArray(int size) {
            return new ClassRoomCoordonates[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeFloat(x);
        parcel.writeFloat(y);
    }
}
