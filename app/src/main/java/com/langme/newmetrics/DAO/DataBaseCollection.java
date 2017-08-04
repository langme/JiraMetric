package com.langme.newmetrics.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by melang on 26/07/2017.
 */

public class DataBaseCollection extends SQLiteOpenHelper {
    public static final String TAG = "DAOCollection";
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Mam.db";

    public DataBaseCollection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(FichierDAO.SQL_CREATE_ENTRIES);
            Log.v(TAG, "TABLE " + FichierDAO.TABLE_NAME + " is created");
            sqLiteDatabase.execSQL(TaskDAO.SQL_CREATE_ENTRIES);
            Log.v(TAG, "TABLE " + TaskDAO.TABLE_NAME + " is created");
        } catch (Exception ex){
            Log.e(TAG, "onCreate: " + ex);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        if (oldVersion < 2) {
            // Create tables again
            //onCreate(db);
        }
    }
}
