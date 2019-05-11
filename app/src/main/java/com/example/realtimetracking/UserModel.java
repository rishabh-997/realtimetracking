package com.example.realtimetracking;

public class UserModel
{
    String user;
    String latitude;
    String longitude;

    public UserModel(String user, String latitude, String longitude, String altitude) {
        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public String getAltitude() {
        return altitude;
    }

    String altitude;

    public String getUser() {
        return user;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
