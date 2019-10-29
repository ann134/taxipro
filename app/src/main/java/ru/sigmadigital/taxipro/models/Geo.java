package ru.sigmadigital.taxipro.models;

import android.location.Location;

import java.io.Serializable;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class Geo extends JsonParser implements Serializable {

    double lat;
    double lon;

    public Geo(Location location) {
        this.lat = location.getLatitude();
        this.lon = location.getLongitude();
    }

    public Geo(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
