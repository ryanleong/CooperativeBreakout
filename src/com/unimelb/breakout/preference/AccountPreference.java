package com.unimelb.breakout.preference;

import android.content.SharedPreferences;

public class AccountPreference {
    private static final String ACCOUNT_PREFERENCE = "AccountPreference";

    private static final String PLAYER_KEY = "username";

    /**
     * Set a player name
     * 
     * @param playername
     * @param password
     */
    public static void rememberPlayerName(String playername) {
    	PreferenceUtils.edit(ACCOUNT_PREFERENCE)
                .putString(PLAYER_KEY, playername)
                .apply();
    }

    /**
     * Get the play name
     * @return
     */
    public static String getPlayerName() {
        SharedPreferences sharedPreferences = PreferenceUtils.get(ACCOUNT_PREFERENCE);
        return sharedPreferences.getString(PLAYER_KEY, null);
    }
    
    /**
     * Check if the play has a name
     * @return
     */
    public static boolean hasPlayerName(){
    	SharedPreferences sharedPreferences = PreferenceUtils.get(ACCOUNT_PREFERENCE);
        return sharedPreferences.contains(PLAYER_KEY);	
    	
    }
    
}
