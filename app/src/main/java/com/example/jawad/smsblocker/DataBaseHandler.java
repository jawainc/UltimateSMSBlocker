package com.example.jawad.smsblocker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jawad on 06/05/2016.
 */
public class DataBaseHandler extends SQLiteOpenHelper {




    // Database Version
    private static final int DATABASE_VERSION = 7;

    // Database Name
    private static final String DATABASE_NAME = "smsblocker.db";

    public DataBaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(BlockListTable.CREATE_STATEMENT);
        db.execSQL(BlockMessageTable.CREATE_STATEMENT);
        db.execSQL(SettingTable.CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BlockListTable.DELETE_STATEMENT);
        db.execSQL(BlockMessageTable.DELETE_STATEMENT);
        db.execSQL(SettingTable.DELETE_STATEMENT);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
