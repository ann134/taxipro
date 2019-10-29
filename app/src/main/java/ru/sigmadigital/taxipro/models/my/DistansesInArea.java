package ru.sigmadigital.taxipro.models.my;

import android.location.Location;

import java.util.List;

import ru.sigmadigital.taxipro.models.Area;
import ru.sigmadigital.taxipro.models.Order;

public class DistansesInArea {

    private Area.AreaPanel area;
    private boolean isPreorder;
    private Location location;
    private long time;
    private List<Order.OrderDriverDistance> distances;

    public DistansesInArea(Area.AreaPanel area) {
        this.area = area;
    }

    public void update(DistansesInArea newDist) {
        this.location = newDist.getLocation();
        this.time = newDist.getTime();
        this.distances = newDist.getDistances();
        //this.isPreorder = newDist.isPreorder();
    }



    public void setLocation(Location location) {
        this.location = location;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isPreorder() {
        return isPreorder;
    }

    public void setPreorder(boolean preorder) {
        isPreorder = preorder;
    }

    public Area.AreaPanel getArea() {
        return area;
    }

    public Location getLocation() {
        return location;
    }

    public long getTime() {
        return time;
    }

    public List<Order.OrderDriverDistance> getDistances() {
        return distances;
    }

    public void setDistances(List<Order.OrderDriverDistance> distances) {
        this.distances = distances;
    }




}
