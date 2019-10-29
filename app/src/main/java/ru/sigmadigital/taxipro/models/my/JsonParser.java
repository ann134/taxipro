package ru.sigmadigital.taxipro.models.my;

import ru.sigmadigital.taxipro.util.GsonHelper;

public abstract class JsonParser {

    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return GsonHelper.getGson().fromJson(json, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toJson() {
        return GsonHelper.getGson().toJson(this);
    }

}
