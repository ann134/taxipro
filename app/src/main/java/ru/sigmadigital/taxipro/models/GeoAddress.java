package ru.sigmadigital.taxipro.models;

import java.io.Serializable;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class GeoAddress extends JsonParser implements Serializable {

    Geo geo;
    int addressId;

    public GeoAddress(Geo geo, int addressId) {
        this.geo = geo;
        this.addressId = addressId;
    }

    public Geo getGeo() {
        return geo;
    }

    public int getAddressId() {
        return addressId;
    }
}
