package ru.sigmadigital.taxipro.models;

import java.io.Serializable;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class TitledGeoAddress extends JsonParser implements Serializable {

    private Geo geo;
    private int addressId;
    private String title;
    private String district;

    public Geo getGeo() {
        return geo;
    }

    public int getAddressId() {
        return addressId;
    }

    public String getTitle() {
        return title;
    }

    public String getDistrict() {
        return district;
    }
}
