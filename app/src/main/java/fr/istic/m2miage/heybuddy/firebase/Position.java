package fr.istic.m2miage.heybuddy.firebase;

/**
 * Created by Florian on 27/11/2016.
 */

public class Position {

    private float latitude;
    private float longitude;

    public Position(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
}
