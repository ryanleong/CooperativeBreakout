package com.unimelb.breakout.preference;

import com.unimelb.breakout.view.BreakoutGame;

import android.content.ContextWrapper;
import android.content.SharedPreferences;

/**
 * Preference Operations
 * 
 * @author Siyuan Zhang
 *
 */
public class PreferenceUtils {
    public static SharedPreferences get(String preference) {
        return BreakoutGame.getInstance()
                .getSharedPreferences(preference, ContextWrapper.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor edit(String preference) {
        return get(preference).edit();
    }
}
