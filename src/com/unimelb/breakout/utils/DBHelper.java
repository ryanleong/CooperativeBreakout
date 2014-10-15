package com.unimelb.breakout.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Define operations of local database
 * @author Siyuan Zhang
 *
 */
public class DBHelper extends SQLiteOpenHelper{

    //DATABASE NAME
    public static final String dbName="breakout_database";

    //Score TABLE
    public static final String TABLENAME="SCORETABLE";
    public static final String ID="_id";
    public static final String LEVEL ="LEVEL";
    public static final String SCORE ="SCORE";
    public static final String TIME ="TIME";

    /**
     * Constructor of DBHelper
     * @param context
     */
    public DBHelper(Context context) {
        //(context, dbname, cursorfactory, version)
        super(context, dbName, null,1);

    }

    /**
     * Called when DBHelper is created.
     */
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        //create document table
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + TABLENAME +" ("
                    + ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + LEVEL + " TEXT, "
                    + SCORE + " INTEGER, "
                    + TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP)" );
        
        Log.i("DATABASE", "Score table created.");
    }

    /**
     * Called when database needs to be upgraded.
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.d("DATABASE UPGRADE", String.format("SQLiteDatabase.onUpgrade(%d -> %d)",
                oldVersion, newVersion));

        //TODO: do something

        onCreate(db);
    }

    /**
     * Insert a score record into the database.
     * 
     * @param level
     * @param score
     */
    public void insertScoreRecord(String level, int score){
    	
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(LEVEL, level);
        cv.put(SCORE, score);
        
        db.insert(TABLENAME, null, cv);

        db.close();
        Log.d("INSERTION SUCCEED", "The record is successfully inserted." + "CV: " + cv.toString());      
    }

    public void dropScoreTable(){
        SQLiteDatabase db=this.getWritableDatabase();
    	db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
    	Log.d("DATABASE", "Score table is dropped");
    }
    
    /**
     * Fetch records from the database.
     * @return
     */
    public Cursor fetchRecords() {
        SQLiteDatabase db=this.getWritableDatabase();

    	Cursor mCursor = db.query(TABLENAME, new String[] {
    	    ID, LEVEL, SCORE, TIME}, 
    	    null, null, null, null, SCORE+" DESC");
    	 
    	if (mCursor != null) {
    		mCursor.moveToFirst();
    	}
    	
    	return mCursor;
    }
    
}
