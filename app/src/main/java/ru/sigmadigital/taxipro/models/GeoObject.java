package ru.sigmadigital.taxipro.models;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class GeoObject {

    public static class GeoObjectType {
        public static final int Region = 0;
        public static final int Locality = 1;
        public static final int Street = 2;
        public static final int Address = 3;
        public static final int Branch = 4;
        public static final int Crossroad = 5;
        public static final int Station = 6;
        public static final int SubArea = 7;
        public static final int Exit = 8;
        public static final int Intercity = 9;
    }


    public static class Item extends JsonParser {

        String id;
        String title;
        String highlightedTitle;
        String note;
        String highlightedNote;
        Geo geo;
        String region;
        int type;
        String trueRegion;
        String locality;
        String district;
        String thoroughfare;
        String number;

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getHighlightedTitle() {
            return highlightedTitle;
        }

        public String getNote() {
            return note;
        }

        public String getHighlightedNote() {
            return highlightedNote;
        }

        public Geo getGeo() {
            return geo;
        }

        public String getRegion() {
            return region;
        }

        public int getType() {
            return type;
        }

        public String getTrueRegion() {
            return trueRegion;
        }

        public String getLocality() {
            return locality;
        }

        public String getDistrict() {
            return district;
        }

        public String getThoroughfare() {
            return thoroughfare;
        }

        public String getNumber() {
            return number;
        }
    }



    public static class ItemFilter extends JsonParser {

        String term;
       /* int[] types;*/
        /*String[] localities;
        int[] sortTypes;
        int cityId;*/
        Geo location;
       /* RegionFilter regionFilter;
        int skip;
        int limit;*/

        public ItemFilter(String term, Geo location) {
            this.term = term;
            this.location = location;
        }
    }


}
