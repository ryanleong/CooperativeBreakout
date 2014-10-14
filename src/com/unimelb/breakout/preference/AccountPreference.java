package com.unimelb.breakout.preference;

import android.content.SharedPreferences;

/**
 * Utility class of using preference
 * 
 * @author Siyuan Zhang
 *
 */
public class AccountPreference {
    private static final String ACCOUNT_PREFERENCE = "AccountPreference";

    private static final String PLAYER_KEY = "username";
    private static final String UPLOADED_HIGHEST_SCORE = "score";

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
    
    
    /**
     * Remember the highest score that the player chooses to upload
     * 
     * @param playername
     * @param password
     */
    public static void rememberScore(int score) {
    	PreferenceUtils.edit(ACCOUNT_PREFERENCE)
                .putInt(UPLOADED_HIGHEST_SCORE, score)
                .apply();
    }

    /**
     * Get the highest score that the player uploaded before
     * @return
     */
    public static int getScore() {
        SharedPreferences sharedPreferences = PreferenceUtils.get(ACCOUNT_PREFERENCE);
        return sharedPreferences.getInt(UPLOADED_HIGHEST_SCORE, 0);
    }
    
    /**
     * Check if the play has a name
     * @return
     */
    public static boolean hasScore(){
    	SharedPreferences sharedPreferences = PreferenceUtils.get(ACCOUNT_PREFERENCE);
        return sharedPreferences.contains(UPLOADED_HIGHEST_SCORE);	
    	
    }
}
