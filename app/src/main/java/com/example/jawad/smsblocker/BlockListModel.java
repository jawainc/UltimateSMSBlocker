package com.example.jawad.smsblocker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.PhoneNumberUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by jawad on 06/05/2016.
 */
public class BlockListModel {



    DataBaseHandler dbHelper;
    Context ct;


    public BlockListModel(Context ct){
        this.ct = ct;
        dbHelper = new DataBaseHandler(ct);
    }

    public void dropTable(){

    }



    /**
     * Insert single number
     *
     * @param data
     * @param showError
     * @return Boolean
     */
    public long insertNumber(BlockList data, Boolean showError){

        long dbSuccessful = -1;
        if(!hasRecord(data.get_number())){

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Date date = new Date();

            ContentValues cv = new ContentValues();

            cv.put(BlockListTable.BlockListEntry.TITLE_NUMBER, data.get_title_number());
            cv.put(BlockListTable.BlockListEntry.NUMBER, data.get_number());
            cv.put(BlockListTable.BlockListEntry.IS_RANGE, data.is_ranged_number());
            cv.put(BlockListTable.BlockListEntry.CREATED_AT, dateFormat.format(date));


            try {
                    dbSuccessful = db.insert(
                            BlockListTable.BlockListEntry.TABLE_NAME,
                            BlockListTable.BlockListEntry.RANGE_NUMBER,
                            cv);


            }
            catch (Exception e){
                System.out.println(e.toString());
            }
            finally {
                db.close();
            }

        }
        else if(showError){
            Toast.makeText(this.ct,"ERROR : Number already in block list",Toast.LENGTH_LONG).show();
        }

        return dbSuccessful;
    }


    /**
     * Insert range number
     *
     * @param data
     * @param showError
     * @return Boolean
     */
    public Boolean insertRangeNumber(BlockList data, Boolean showError){

        boolean dbSuccessful = false;
        if(!hasRangeRecord(data.get_number(),data.get_range_number())){


            SQLiteDatabase db = dbHelper.getWritableDatabase();


            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                Date date = new Date();

                ContentValues cv = new ContentValues();



                cv.put(BlockListTable.BlockListEntry.TITLE_NUMBER, data.get_title_number());
                cv.put(BlockListTable.BlockListEntry.TITLE_RANGE_NUMBER, data.get_title_range_number());
                cv.put(BlockListTable.BlockListEntry.NUMBER, data.get_number());
                cv.put(BlockListTable.BlockListEntry.RANGE_NUMBER, data.get_range_number());
                cv.put(BlockListTable.BlockListEntry.IS_RANGE, data.is_ranged_number());
                cv.put(BlockListTable.BlockListEntry.CREATED_AT, dateFormat.format(date));

                dbSuccessful = db.insert(
                   BlockListTable.BlockListEntry.TABLE_NAME,null,cv) > 0;

            }
            catch (Exception e){
                System.out.println(e.toString());
            }
            finally {
                db.close();
            }


        }
        else if(showError){
            Toast.makeText(this.ct,"ERROR : Numbers already in block list",Toast.LENGTH_LONG).show();
        }





        return dbSuccessful;
    }

    /**
     * Get All Numbers
     * @return List<BlockList>
     */
    public ArrayList<BlockList> getAllNumbers(String order){
        ArrayList<BlockList> blockList = new ArrayList<BlockList>();

        String orderQuery = "";

        switch (order){
            case "date_desc":
                orderQuery = BlockListTable.BlockListEntry.CREATED_AT +" DESC";
            break;
            case "date_asc":
                orderQuery = BlockListTable.BlockListEntry.CREATED_AT +" ASC";
                break;
            case "num_desc":
                orderQuery = BlockListTable.BlockListEntry.NUMBER +" DESC";
                break;
            case "num_asc":
                orderQuery = BlockListTable.BlockListEntry.NUMBER +" ASC";
                break;
            default:
                orderQuery = BlockListTable.BlockListEntry.CREATED_AT +" DESC";
        }



        String selectQuery = "SELECT  " +
                BlockListTable.BlockListEntry._ID +","+
                BlockListTable.BlockListEntry.TITLE_NUMBER +","+
                BlockListTable.BlockListEntry.TITLE_RANGE_NUMBER +","+
                BlockListTable.BlockListEntry.IS_RANGE +
                " FROM " + BlockListTable.BlockListEntry.TABLE_NAME + " Order By "+ orderQuery;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);



        try{
            if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()){
                do {
                    blockList.add(new BlockList(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3)));
                }while (cursor.moveToNext());
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
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


        return blockList;
    }

    /**
     * delete item
     * @return Boolean
     */
    public Boolean removeItem(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Boolean flag = false;

        try{
            flag = db.delete(BlockListTable.BlockListEntry.TABLE_NAME, BlockListTable.BlockListEntry._ID+"=? ",new String[]{Integer.toString(id)}) > 0;
        }catch (Exception e){
            System.out.println(e);
        }
        finally {
            db.close();
        }


        return flag;
    }

    /**
     * Check if a record exists
     * @param record
     * @return Boolean
     */
    private boolean hasRecord(String record){

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selectString = "SELECT * FROM " + BlockListTable.BlockListEntry.TABLE_NAME + " WHERE CAST(" + BlockListTable.BlockListEntry.NUMBER + " AS INT) =?";


        Cursor cursor = db.rawQuery(selectString, new String[] {record});

        boolean hasRecord = false;
        if(cursor != null && !cursor.isClosed() && cursor.moveToFirst()){
            hasRecord = true;
        }

        db.close();

        try {
            if (!cursor.isClosed()) {
                cursor.close();
            }
            cursor = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasRecord;
    }

    private boolean hasRangeRecord(String record, String record2){

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectString = "SELECT * FROM " + BlockListTable.BlockListEntry.TABLE_NAME + " WHERE CAST(" + BlockListTable.BlockListEntry.NUMBER + " AS INT) =? AND CAST("+ BlockListTable.BlockListEntry.RANGE_NUMBER + " AS  INT) =?";


        Cursor cursor = db.rawQuery(selectString, new String[] {record,record2});

        boolean hasRecord = false;
        if(cursor != null && !cursor.isClosed() && cursor.moveToFirst()){
            hasRecord = true;
        }


        db.close();
        try {
            if (!cursor.isClosed()) {
                cursor.close();
            }
            cursor = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hasRecord;
    }

    public int findNumber(String num){
        int hasRecord = 0;

        boolean numberFound = false;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //String selectString = "SELECT * FROM " + BlockListTable.BlockListEntry.TABLE_NAME + " WHERE CAST( ? AS INT) = " + BlockListTable.BlockListEntry.NUMBER;

        String selectString = "SELECT * FROM " + BlockListTable.BlockListEntry.TABLE_NAME +
                " WHERE " + BlockListTable.BlockListEntry.IS_RANGE + " != 1";

        Cursor cursor = db.rawQuery(selectString, null);

        if(cursor != null && !cursor.isClosed() && cursor.moveToFirst()){
            do{

                if(PhoneNumberUtils.compare(cursor.getString(1),num)) {
                    numberFound = true;
                    hasRecord = cursor.getInt(0);
                    break;
                }

            }while (cursor.moveToNext());


        }

        try {
            if (!cursor.isClosed()) {
                cursor.close();
            }
            cursor = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(hasRecord == 0){
            String selectString2 = "SELECT * FROM " + BlockListTable.BlockListEntry.TABLE_NAME +
                    " WHERE " + BlockListTable.BlockListEntry.IS_RANGE + " =1";


            Cursor cursor2 = db.rawQuery(selectString2, null);

            if(cursor2.moveToFirst()){
                do{

                    int n1 = cursor2.getInt(3);
                    int n2 = cursor2.getInt(4);

                    for(;n1<=n2;n1++){
                        if(PhoneNumberUtils.compare(Integer.toString(n1),num)) {
                            numberFound = true;
                            hasRecord = cursor2.getInt(0);
                            break;
                        }
                    }

                    if(numberFound) {
                       break;
                    }

                }while (cursor.moveToNext());


            }


            try {
                if (!cursor2.isClosed()) {
                    cursor2.close();
                }
                cursor2 = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        db.close();

        return hasRecord;
    }

    public String getNumber(int block_list_id){
        String number = "";
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectString = "SELECT * FROM " + BlockListTable.BlockListEntry.TABLE_NAME +
                " WHERE " + BlockListTable.BlockListEntry._ID + " = " + block_list_id;

        Cursor cursor = db.rawQuery(selectString, null);

        if(cursor != null && !cursor.isClosed() && cursor.moveToFirst()){

        }
        try {
            if (!cursor.isClosed()) {
                cursor.close();
            }
            cursor = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return number;
    }

    public Boolean upDateNumber(String id, String number){

        Boolean num_changed = false;

        SQLiteDatabase db1 = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BlockListTable.BlockListEntry.TITLE_NUMBER, number);
        values.put(BlockListTable.BlockListEntry.NUMBER, Helper.formatToOnlyNumber(number));
        // updating row
        num_changed = db1.update(BlockListTable.BlockListEntry.TABLE_NAME, values, BlockListTable.BlockListEntry._ID + " = ?",
                new String[] { id }) > 0;

        db1.close();

        return num_changed;
    }


}
