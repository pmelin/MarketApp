package com.example.marketapp.repository;

import android.content.Context;
import android.content.SharedPreferences;

import jBCrypt.BCrypt;

public class SettingsRepository {

    private static final String PREFERENCES_FILE = "com.example.marketapp.PRIVATE_PREFERENCES";

    private static void saveInPreferences(String key, String value, Context context) {
       SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
       SharedPreferences.Editor editor = preferences.edit();
       editor.putString(key, value);
       editor.apply();
    }

    private static String getFromPreferences(String key, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return preferences.getString(key, null);

    }

    public static void saveRegistration(String uuid, String nickName, String password, String marketPublicKey, Context context) {
        //transform password to bcrypthash
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        //stores uuid, username, marketPublicKey and the password hash on App Shared Preferences - private mode
        saveInPreferences("uuid", uuid, context);
        saveInPreferences("nickName", nickName, context);
        saveInPreferences("passwordHash", passwordHash, context);
        saveInPreferences("marketPublicKey", marketPublicKey, context);
    }

    public static boolean validateCredentials(String nickName, String password, Context context) {
        //1. get username and password hash, compare it and return the result
        String dbNickName = getFromPreferences("nickName", context);
        String dbPasswordHash = getFromPreferences("passwordHash", context);

        return nickName.equals(dbNickName) && BCrypt.checkpw(password, dbPasswordHash);
    }

    public static boolean isUserRegistered(Context context) {
        String uuid = getFromPreferences("uuid", context);
        return uuid != null;
    }

}
