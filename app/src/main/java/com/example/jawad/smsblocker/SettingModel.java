package com.example.jawad.smsblocker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jawad on 7/5/2016.
 */
public class SettingModel {

    DataBaseHandler dbHelper;
    Context ct;


    public SettingModel(Context ct){
        this.ct = ct;
        dbHelper = new DataBaseHandler(ct);
    }



    public int[] getValues(){
        int[] arr = new int[2];

        String selectQuery = "SELECT * FROM " + SettingTable.SettingEntry.TABLE_NAME;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try{
            if (cursor != null && !cursor.isClosed() && cursor.moveToNext()){

                arr[0] = cursor.getInt(1);
                arr[1] = cursor.getInt(2);
            }
            else {

                SQLiteDatabase db1 = dbHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(SettingTable.SettingEntry.BLOCK_UNKOWN,0);
                cv.put(SettingTable.SettingEntry.NO_DAYS,0);

                db1.insert(SettingTable.SettingEntry.TABLE_NAME,null,cv);
                db1.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {

            db.close();
            try {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                cursor = null;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return arr;
    }

    public void updateUnknown(int value){
        SQLiteDatabase db1 = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SettingTable.SettingEntry.BLOCK_UNKOWN, value);
        // updating row
        db1.update(SettingTable.SettingEntry.TABLE_NAME, values, null,null);

        db1.close();

    }

    public void updateDays(int value){
        SQLiteDatabase db1 = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SettingTable.SettingEntry.NO_DAYS, value);
        // updating row
        db1.update(SettingTable.SettingEntry.TABLE_NAME, values, null,null);

        db1.close();

    }

}
