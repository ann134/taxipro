package ru.sigmadigital.taxipro.models.my;

import java.util.List;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class Header {

    public static class Request extends JsonParser {

        private int id;
        private String request;

        public Request(int id, String request) {
            this.id = id;
            this.request = request;
        }

        public int getId() {
            return id;
        }
    }


    public static class Response extends JsonParser {

        private int id;
        private int responseId;
        private List<Message> messages;

        public int getId() {
            return id;
        }

        public int getResponseId() {
            return responseId;
        }

        public List<Message> getMessages() {
            return messages;
        }


        public class Message {
            int count;
            String type;

            public int getCount() {
                return count;
            }

            public String getType() {
                return type;
            }
        }
    }

}
