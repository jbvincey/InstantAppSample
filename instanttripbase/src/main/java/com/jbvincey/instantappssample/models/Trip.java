package com.jbvincey.instantappssample.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jean-baptistevincey on 18/06/2017.
 */

public class Trip implements Parcelable {

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
    private String id;
    private String name;
    private String description;
    private String cardImageFile;
    private float grade;
    private String contact;
    private Coordinates coordinates;

    protected Trip(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        cardImageFile = in.readString();
        grade = in.readFloat();
        contact = in.readString();
        coordinates = in.readParcelable(Coordinates.class.getClassLoader());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCardImageFile() {
        return cardImageFile;
    }

    public void setCardImageFile(String cardImageFile) {
        this.cardImageFile = cardImageFile;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public String getGradeAsString() {
        return String.valueOf(grade);
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(cardImageFile);
        parcel.writeFloat(grade);
        parcel.writeString(contact);
        parcel.writeParcelable(coordinates, i);
    }
}
