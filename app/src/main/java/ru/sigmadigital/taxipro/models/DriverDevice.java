package ru.sigmadigital.taxipro.models;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class DriverDevice {

    public static class DeviceDataValue extends JsonParser {
        String appVersion;
        String deviceType;
        String deviceVersion;
        String osVersion;
    }
}
