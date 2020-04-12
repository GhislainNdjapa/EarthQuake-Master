package com.ndjapa_s1803449.earthquake_master.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Earthquake implements Parcelable {
    private String title;
    private String location;
    private String dateString;
    private double longitude;
    private double latitude;
    private double magnitude;
    private int depth;
    private String link;
    public Earthquake(String title, String location, String dateString, double longitude, double latitude, double magnitude, int depth,String link) {
        this.title = title;
        this.location = location;
        this.dateString = dateString;
        this.longitude = longitude;
        this.latitude = latitude;
        this.magnitude = magnitude;
        this.depth = depth;
        this.link = link;
    }


    protected Earthquake(Parcel in) {
        title = in.readString();
        location = in.readString();
        dateString = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        magnitude = in.readDouble();
        depth = in.readInt();
        link = in.readString();
    }

    public static final Creator<Earthquake> CREATOR = new Creator<Earthquake>() {
        @Override
        public Earthquake createFromParcel(Parcel in) {
            return new Earthquake(in);
        }

        @Override
        public Earthquake[] newArray(int size) {
            return new Earthquake[size];
        }
    };

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    public Earthquake() {

    }


    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDateString() {
        return dateString;
    }

    public String getLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return "Earthquake{" +
                "title='" + title + '\'' +
                ", location='" + location + '\'' +
                ", dateString='" + dateString + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", magnitude=" + magnitude +
                ", depth=" + depth +
                ", link='" + link + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(location);
        dest.writeString(dateString);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeDouble(magnitude);
        dest.writeInt(depth);
        dest.writeString(link);
    }
}
