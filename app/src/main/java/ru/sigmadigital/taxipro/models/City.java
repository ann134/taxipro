package ru.sigmadigital.taxipro.models;


import ru.sigmadigital.taxipro.models.my.JsonParser;

public class City {

    public static class Item extends JsonParser {
        int id;
        String name;
        boolean isSelected;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public boolean isSelected() {
            return isSelected;
        }
    }

    public static class ItemFilter extends JsonParser {
        Geo position; //Позиция для поиска ближайшего города
        int skip;
        int limit;

        public ItemFilter(Geo position, int skip, int limit) {
            this.position = position;
            this.skip = skip;
            this.limit = limit;
        }
    }



}
