package data;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    public static final String KEY_SHARED_PREFS = "my_shared_prefs";
    public static final String KEY_FINISHED = "KEY_FINISHED";
    public static final String KEY_LOCKED = "KEY_LOCKED";
    public static final String KEY_SP_APP_BUILDS = "KEY_SP_APP_BUILDS";
    public static final String KEY_SP_IS_PAID_OLD_ADS = "KEY_SP_IS_USER_PRO";
    public static final String KEY_SP_IS_PAID_ADS = "KEY_SP_IS_PAID_ADS";
    public static final String KEY_SP_IS_PAID_UNLOCK_LVLS = "KEY_SP_IS_PAID_UNLOCK_LVLS";
    public static final String KEY_SP_IS_PAID_FULL = "KEY_SP_IS_PAID_FULL";
    public static final String KEY_SP_SELECTED_THEME = "KEY_SP_SELECTED_THEME";
    public static String SUPPORT_EMAIL = "admindb@suprememanufacture.com";


    public static void setSharedPreferencesString(String key, String string) {
        SharedPreferences.Editor editor = App.getAppCtx().getSharedPreferences(KEY_SHARED_PREFS, Context.MODE_PRIVATE).edit();
        editor.putString(key, string);
        editor.apply();
    }

    public static String getSharedPreferencesString(String key, String defaultValue) {
        SharedPreferences preferences = App.getAppCtx().getSharedPreferences(KEY_SHARED_PREFS, Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }

    //-----------------------
    public static void setSharedPreferencesInt(String key, int number) {
        SharedPreferences.Editor editor = App.getAppCtx().getSharedPreferences(KEY_SHARED_PREFS, Context.MODE_PRIVATE).edit();
        editor.putInt(key, number);
        editor.apply();
    }

    public static int getSharedPreferencesInt(String key, int defaultValue) {
        SharedPreferences preferences = App.getAppCtx().getSharedPreferences(KEY_SHARED_PREFS, Context.MODE_PRIVATE);
        return preferences.getInt(key, defaultValue);
    }

    //-----------------------
    public static void setSharedPreferencesLong(String key, long number) {
        SharedPreferences.Editor editor = App.getAppCtx().getSharedPreferences(KEY_SHARED_PREFS, Context.MODE_PRIVATE).edit();
        editor.putLong(key, number);
        editor.apply();
    }

    public static long getSharedPreferencesLong(String key, long defaultValue) {
        SharedPreferences preferences = App.getAppCtx().getSharedPreferences(KEY_SHARED_PREFS, Context.MODE_PRIVATE);
        return preferences.getLong(key, defaultValue);
    }

    //-----------------------
    public static void setSharedPreferencesBool(String key, boolean value) {
        SharedPreferences.Editor editor = App.getAppCtx().getSharedPreferences(KEY_SHARED_PREFS, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getSharedPreferencesBool(String key, boolean defaultValue) {
        SharedPreferences preferences = App.getAppCtx().getSharedPreferences(KEY_SHARED_PREFS, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, defaultValue);
    }

    //-----------------------
    public static void deleteSharedPrederences(String key) {
        SharedPreferences preferences = App.getAppCtx().getSharedPreferences(KEY_SHARED_PREFS, Context.MODE_PRIVATE);
        preferences.edit().remove(key).apply();
    }

}