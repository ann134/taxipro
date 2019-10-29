package ru.sigmadigital.taxipro.models;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class Token {

    public static class SignIn extends JsonParser {
        private int countryId;
        private String phone;
        private int language;

        public SignIn(int countryId, String phone, int language) {
            this.countryId = countryId;
            this.phone = phone;
            this.language = language;
        }
    }

    public static class DriverAppSignedIn extends JsonParser {
        private String entityId;

        public String getEntityId() {
            return entityId;
        }

       /* int appEntityId;
        "entityId":"ifBc6zs5L0mv71rdNsQ6tiRuPKkqt4aoRETMB0ry3Zk=","appEntityId":3428998*/
    }

    public static class ConfirmSignIn extends JsonParser {
        private String tokenId;
        private String confirmationCode;
        private int language;

        public ConfirmSignIn(String tokenId, String confirmationCode, int language) {
            this.tokenId = tokenId;
            this.confirmationCode = confirmationCode;
            this.language = language;
        }
    }

    public static class HasBeenConfirmed extends JsonParser {

    }

    public static class SignOut extends JsonParser {


    }


}
