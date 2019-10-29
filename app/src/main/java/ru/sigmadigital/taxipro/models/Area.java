package ru.sigmadigital.taxipro.models;

import java.io.Serializable;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class Area {


    public static class AreaPanelFilter extends JsonParser {
        int skip;
        int limit;

        public AreaPanelFilter(int skip, int limit) {
            this.skip = skip;
            this.limit = limit;
        }
    }



    public static class AreaPanel extends JsonParser implements Serializable {
        int[] ids;
        String name;

        public int[] getIds() {
            return ids;
        }

        public String getName() {
            return name;
        }
    }






}
