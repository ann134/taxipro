package ru.sigmadigital.taxipro.models;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class Country {

    public static class AuthenticationCountry extends JsonParser {

        private int id;
        private String name;
        private String countryCode;
        private boolean isSelected;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public boolean isSelected() {
            return isSelected;
        }
    }

    public static class AuthenticationCountryFilter extends JsonParser {

        private Geo geo;

        private int skip;
        private int limit;

        public AuthenticationCountryFilter(Geo geo, int skip, int limit) {
            this.geo = geo;
            this.skip = skip;
            this.limit = limit;
        }
    }



}
