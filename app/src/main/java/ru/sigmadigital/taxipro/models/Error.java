package ru.sigmadigital.taxipro.models;

import ru.sigmadigital.taxipro.models.my.JsonParser;

public class Error extends JsonParser {

    int statusCode;
    /*'Код состояния: 400, 401, 403, 404, 408, 409, 429, 500'*/
    int errorCode;

    String message;

    public int getStatusCode() {
        return statusCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

}
