package ru.sigmadigital.taxipro.models;

import java.io.Serializable;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class DriverNews {

    public static class HaveBeenAppearedAsync extends JsonParser {

        Driver.FeedNews news;
        int commandId;

    }

    public static class ListItem extends JsonParser implements Serializable {

        int id;
        String date;
        String title;
        String text;

        public int getId() {
            return id;
        }

        public String getDate() {
            return date;
        }

        public String getTitle() {
            return title;
        }

        public String getText() {
            return text;
        }
    }

    public static class ListItemFilter extends JsonParser {
        int skip;
        int limit;

        public ListItemFilter(int skip, int limit) {
            this.skip = skip;
            this.limit = limit;
        }
    }

}
