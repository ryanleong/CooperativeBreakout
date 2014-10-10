package com.unimelb.breakout.view;

public class BreakoutGame extends android.app.Application{
    private static BreakoutGame instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static BreakoutGame getInstance() {
        return instance;
	}
}
