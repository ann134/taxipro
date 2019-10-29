package ru.sigmadigital.taxipro.util;

import android.content.Context;

import ru.sigmadigital.taxipro.App;
import ru.sigmadigital.taxipro.models.Token;


public class SettingsHelper {

    private static String NAME = "settings";
    private static String FIELD_TOKEN = "token";
    private static String FIELD_TEXT_SIZE = "textSize";


    public static Token.DriverAppSignedIn getToken() {
        if (App.getAppContext() != null) {
            String json = App.getAppContext().getSharedPreferences(NAME, Context.MODE_PRIVATE)
                    .getString(FIELD_TOKEN, null);
            return Token.DriverAppSignedIn.fromJson(json, Token.DriverAppSignedIn.class);
        } else {
            return null;
        }
    }

    public static void setToken(Token.DriverAppSignedIn token) {
        if (App.getAppContext() != null) {
            App.getAppContext().getSharedPreferences(NAME, Context.MODE_PRIVATE)
                    .edit()
                    .putString(FIELD_TOKEN, token != null ? token.toJson() : null)
                    .apply();
        }
    }

    public static void clearToken(){
        if (App.getAppContext() != null) {
            App.getAppContext().getSharedPreferences(NAME, Context.MODE_PRIVATE).edit()
                    .clear()
                    .apply();
        }
    }

    public static int getTextSize() {
        if (App.getAppContext() != null) {
            return App.getAppContext().getSharedPreferences(NAME, Context.MODE_PRIVATE).getInt(FIELD_TEXT_SIZE, 1);
        } else {
            return 1;
        }
    }


    public static void setNewTextSise(int newTextSise) {
        if (App.getAppContext() != null) {
            App.getAppContext().getSharedPreferences(NAME, Context.MODE_PRIVATE)
                    .edit()
                    .putInt(FIELD_TEXT_SIZE, newTextSise)
                    .apply();
        }
    }


}