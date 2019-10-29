package ru.sigmadigital.taxipro.models;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class AuthSession {

    public static class ContinueSession extends JsonParser {

        private int language;
        private String tokenId;

        public ContinueSession(int language, String tokenId) {
            this.language = language;
            this.tokenId = tokenId;
        }
    }


    public static class HasBeenContinued extends JsonParser {

        private int sessionId;
        private int userId;

        public int getSessionId() {
            return sessionId;
        }

        public int getUserId() {
            return userId;
        }
    }

}
