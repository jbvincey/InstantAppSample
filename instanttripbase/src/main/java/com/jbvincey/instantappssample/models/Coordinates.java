package com.jbvincey.instantappssample.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jean-baptistevincey on 24/06/2017.
 */

public class Coordinates implements Parcelable {

    public static final Creator<Coordinates> CREATOR = new Creator<Coordinates>() {
        @Override
        public Coordinates createFromParcel(Parcel in) {
            return new Coordinates(in);
        }

        @Override
        public Coordinates[] newArray(int size) {
            return new Coordinates[size];
        }
    };
    private float latitude;
    private float longitude;

    protected Coordinates(Parcel in) {
        latitude = in.readLong();
        longitude = in.readLong();
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(latitude);
        parcel.writeFloat(longitude);
    }
}
