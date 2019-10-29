package ru.sigmadigital.taxipro.models.http;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class ImageResponse extends JsonParser {

    int[] ids;

    public int[] getIds() {
        return ids;
    }
}
