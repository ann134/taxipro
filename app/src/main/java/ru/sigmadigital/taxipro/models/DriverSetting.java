package ru.sigmadigital.taxipro.models;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class DriverSetting {

    public static class DriverSettingsValue extends JsonParser {

        GeoAddress homeAddressGeo;
        boolean onlyCash;
        int navigation;
        int nightMode;


        public GeoAddress getHomeAddressGeo() {
            return homeAddressGeo;
        }

        public boolean isOnlyCash() {
            return onlyCash;
        }

        public int getNavigation() {
            return navigation;
        }

        public int getNightMode() {
            return nightMode;
        }

    }


    public static class Setup extends JsonParser{
        DriverSetting.DriverSettingsValue settings;

        public Setup(DriverSettingsValue settings) {
            this.settings = settings;
        }
    }


}
