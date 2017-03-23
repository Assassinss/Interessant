package me.zsj.interessant.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author zsj
 */

public class PreferenceManager {

    private static final String PREFS_NAME = "interesant";

    private static SharedPreferences preferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor edit(Context context) {
        return preferences(context).edit();
    }

    public static void putBoolean(Context context, String key, boolean value) {
        edit(context).putBoolean(key, value).apply();
    }

    public static boolean getBooleanValue(Context context, String key) {
        return getBooleanValue(context, key, false);
    }

    public static boolean getBooleanValue(Context context, String key, boolean defaultValue) {
        return preferences(context).getBoolean(key, defaultValue);
    }

}
