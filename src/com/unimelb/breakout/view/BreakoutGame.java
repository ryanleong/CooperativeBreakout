package com.unimelb.breakout.view;

import com.unimelb.breakout.object.ScoreBoard;

/**
 * This class extends the Application object.
 * Variables in this class have the same lifecycle as the application.
 * They are always available to use.
 * @author Siyuan Zhang
 *
 */
public class BreakoutGame extends android.app.Application{
    private static BreakoutGame instance;
    ScoreBoard scoreboard = null;
    
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static BreakoutGame getInstance() {
        return instance;
	}

	public ScoreBoard getScoreboard() {
		return scoreboard;
	}

	public void setScoreboard(ScoreBoard scoreboard) {
		this.scoreboard = scoreboard;
	}
    
    
}
