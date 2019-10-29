package ru.sigmadigital.taxipro.models;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class Address {

    public static class GetOrAdd extends JsonParser {
        String id;

        public GetOrAdd(String id) {
            this.id = id;
        }
    }


    public static class HasBeenRespond extends JsonParser {
        int addressId;

        public int getAddressId() {
            return addressId;
        }
    }
}
