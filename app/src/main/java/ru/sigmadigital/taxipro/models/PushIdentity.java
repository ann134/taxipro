package ru.sigmadigital.taxipro.models;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class PushIdentity {

    public static class Register extends JsonParser {
        String token;
        String application;

        public Register(String token, String application) {
            this.token = token;
            this.application = application;
        }
    }

}
