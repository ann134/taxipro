package ru.sigmadigital.taxipro.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {

    public static Gson getGson() {
        return new GsonBuilder().disableHtmlEscaping().create();
    }
}
