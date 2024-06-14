package com.nas.naisak.constants;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManagerJava {
    public static String SHARED_PREF_NAS = "NAISAK";
    public static String getlanguagejava(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        return prefs.getString("language", "");
    }
    public static void setlanguagejava(Context context, String result) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF_NAS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("language", result);
        editor.commit();
    }
}
