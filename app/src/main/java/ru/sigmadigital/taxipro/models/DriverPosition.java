package ru.sigmadigital.taxipro.models;

import android.location.Location;

import java.util.List;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class DriverPosition {

    public static class AreaPanelHasBeenModifiedAsync extends JsonParser {
        List <Area.AreaPanel> panels;

        public List<Area.AreaPanel> getPanels() {
            return panels;
        }
    }


    public static class Position extends JsonParser {
        Geo geo;
        double speed;
        double accuracy;
        double course;
        double altitude;
        int timestamp;

        public Position(Location location) {
            this.geo = new Geo(location.getLatitude(), location.getLongitude());
            this.speed = location.getSpeed();
            this.accuracy = location.getAccuracy();
            this.course = location.getBearing();
            this.altitude = location.getAltitude();
            this.timestamp = (int)location.getTime();
        }
    }

}
