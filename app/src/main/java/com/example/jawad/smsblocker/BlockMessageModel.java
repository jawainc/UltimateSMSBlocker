package com.example.jawad.smsblocker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jawad on 06/05/2016.
 */
public class BlockMessageModel {



    DataBaseHandler dbHelper;
    Context ct;


    public BlockMessageModel(Context ct){
        this.ct = ct;
        dbHelper = new DataBaseHandler(ct);
    }


    /**
     * Insert message
     *
     * @param data
     * @param showError
     * @return Boolean
     */
    public Boolean insertMessage(BlockMessage data, Boolean showError){

        boolean dbSuccessful = false;





            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            Date date = new Date();

            ContentValues cv = new ContentValues();

            cv.put(BlockMessageTable.BlockMessageEntry.NUMBER, data.get_number());
            cv.put(BlockMessageTable.BlockMessageEntry.MESSAGE, data.get_message());
            cv.put(BlockMessageTable.BlockMessageEntry.BLOCK_LIST_ID, data.get_block_list_id());
            cv.put(BlockMessageTable.BlockMessageEntry.IS_NEW, 1);
            cv.put(BlockMessageTable.BlockMessageEntry.CREATED_AT, dateFormat.format(date));


            try {
                    dbSuccessful = db.insert(
                            BlockMessageTable.BlockMessageEntry.TABLE_NAME,
                            null,
                            cv) > 0;


            }
            catch (Exception e){
                System.out.println(e.toString());
            }
        finally {
                db.close();
            }

        return dbSuccessful;
    }




    /**
     * Get All Messages
     * @return List<BlockMessage>
     */
    public ArrayList<BlockMessage> getAllMessages(){
        ArrayList<BlockMessage> blockMessage = new ArrayList<BlockMessage>();

        String selectQuery = "SELECT " +
                BlockMessageTable.BlockMessageEntry.TABLE_NAME + "."+ BlockMessageTable.BlockMessageEntry.BLOCK_LIST_ID +", " +
                BlockMessageTable.BlockMessageEntry.TABLE_NAME + "."+ BlockMessageTable.BlockMessageEntry.MESSAGE +", " +
                BlockMessageTable.BlockMessageEntry.TABLE_NAME + "."+ BlockMessageTable.BlockMessageEntry.CREATED_AT +", " +
                BlockMessageTable.BlockMessageEntry.TABLE_NAME + "."+ BlockMessageTable.BlockMessageEntry.IS_NEW +", " +
                BlockListTable.BlockListEntry.TABLE_NAME + "."+ BlockListTable.BlockListEntry.TITLE_NUMBER +", " +
                BlockListTable.BlockListEntry.TABLE_NAME + "."+ BlockListTable.BlockListEntry.TITLE_RANGE_NUMBER +", " +
                BlockListTable.BlockListEntry.TABLE_NAME + "."+ BlockListTable.BlockListEntry.IS_RANGE +", "+
                BlockMessageTable.BlockMessageEntry.TABLE_NAME + "."+ BlockMessageTable.BlockMessageEntry.NUMBER +
                " FROM " + BlockMessageTable.BlockMessageEntry.TABLE_NAME +
                " LEFT JOIN " + BlockListTable.BlockListEntry.TABLE_NAME +
                " ON " + BlockListTable.BlockListEntry.TABLE_NAME + "."+ BlockListTable.BlockListEntry._ID +" = "+ BlockMessageTable.BlockMessageEntry.TABLE_NAME + "."+ BlockMessageTable.BlockMessageEntry.BLOCK_LIST_ID +
                " GROUP BY "+BlockMessageTable.BlockMessageEntry.TABLE_NAME+"."+BlockMessageTable.BlockMessageEntry.NUMBER +
                " Order By "+ BlockMessageTable.BlockMessageEntry.TABLE_NAME +"."+ BlockMessageTable.BlockMessageEntry.CREATED_AT +" DESC ";

        System.out.println(selectQuery);


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try{
            if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()){
                do {
                    BlockMessage bm = new BlockMessage();
                    bm.set_block_list_id(cursor.getInt(0));
                    bm.set_message(cursor.getString(1));
                    bm.set_created_at(dateFormat.parse(cursor.getString(2)));
                    bm.set_is_new(cursor.getInt(3));
                    bm.set_title_number(cursor.getString(4));
                    bm.set_title_range_number(cursor.getString(5));
                    bm.set_is_range(cursor.getInt(6));
                    bm.set_number(cursor.getString(7));
                    blockMessage.add(bm);
                }while (cursor.moveToNext());
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
        finally {
            try {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                cursor = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            db.close();
        }


        return blockMessage;
    }

    /**
     * delete message
     * @return Boolean
     */
    public Boolean removeMessage(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Boolean flag = false;

        try{
            flag = db.delete(BlockMessageTable.BlockMessageEntry.TABLE_NAME, BlockMessageTable.BlockMessageEntry._ID+"=? ",new String[]{Integer.toString(id)}) > 0;
        }catch (Exception e){
            System.out.println(e);
        }
        finally {
            db.close();
        }


        return flag;
    }

    public Boolean removeMessages(int block_list_id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Boolean flag = false;

        try{
            flag = db.delete(BlockMessageTable.BlockMessageEntry.TABLE_NAME, BlockMessageTable.BlockMessageEntry.BLOCK_LIST_ID+"=? ",new String[]{Integer.toString(block_list_id)}) > 0;
        }catch (Exception e){
            System.out.println(e);
        }
        finally {
            db.close();
        }


        return flag;
    }

    public Boolean removeMessagesForNumber(String number){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Boolean flag = false;

        try{
            flag = db.delete(BlockMessageTable.BlockMessageEntry.TABLE_NAME, BlockMessageTable.BlockMessageEntry.NUMBER+"=? ",new String[]{number}) > 0;
        }catch (Exception e){
            System.out.println(e);
        }
        finally {
            db.close();
        }


        return flag;
    }

    public Boolean removeAllMessages(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Boolean flag = false;

        try{
            flag = db.delete(BlockMessageTable.BlockMessageEntry.TABLE_NAME, null,null) > 0;
        }catch (Exception e){
            System.out.println(e);
        }
        finally {
            db.close();
        }


        return flag;
    }

    public void RemoveScheduleMessages(int days){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try{
            String where = "(Cast(julianday('now') AS NUMERIC) - CAST(julianday(created_at) AS NUMERIC)) >= ? ";
            db.delete(BlockMessageTable.BlockMessageEntry.TABLE_NAME, where,new String[]{Integer.toString(days)});
        }catch (Exception e){
            System.out.println(e);
        }
        finally {
            db.close();
        }

    }

    public BlockMessage readMessage(String id){
        BlockMessage blockMessage = new BlockMessage();

        String selectQuery = "SELECT  *" +
                " FROM " + BlockMessageTable.BlockMessageEntry.TABLE_NAME + " WHERE "+ BlockMessageTable.BlockMessageEntry._ID +" = ?";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {id});

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try{
            if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()){

                    blockMessage.set_id(cursor.getInt(0));
                    blockMessage.set_block_list_id(cursor.getInt(1));
                    blockMessage.set_number(cursor.getString(2));
                    blockMessage.set_message(cursor.getString(3));
                    blockMessage.set_is_new(cursor.getInt(4));
                    blockMessage.set_created_at(dateFormat.parse(cursor.getString(5)));
            }
            else {
                blockMessage = new BlockMessage();
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
        finally {
            try {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                cursor = null;
            } catch (Exception e) {
                e.printStackTrace();
            }

            db.close();

        }

        return blockMessage;
    }

    public ArrayList<BlockMessage> getReadMessages(String id){
        ArrayList<BlockMessage> blockMessage = new ArrayList<BlockMessage>();

        String selectQuery = "SELECT  *" +
                " FROM " + BlockMessageTable.BlockMessageEntry.TABLE_NAME +
                " WHERE "+ BlockMessageTable.BlockMessageEntry.BLOCK_LIST_ID +" = ?"+
                " ORDER BY "+ BlockMessageTable.BlockMessageEntry.CREATED_AT + " ASC";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {id});

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try{
            if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()){
                do {
                    blockMessage.add(new BlockMessage(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),dateFormat.parse(cursor.getString(5))));
                }while (cursor.moveToNext());
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
        finally {
            try {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                cursor = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            db.close();

            SQLiteDatabase db1 = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(BlockMessageTable.BlockMessageEntry.IS_NEW, 0);
            // updating row
            db1.update(BlockMessageTable.BlockMessageEntry.TABLE_NAME, values, BlockMessageTable.BlockMessageEntry.BLOCK_LIST_ID + " = ?",
                    new String[] { id });

            db1.close();


        }

        return blockMessage;
    }

    public ArrayList<BlockMessage> getReadMessagesForNumber(String number){
        ArrayList<BlockMessage> blockMessage = new ArrayList<BlockMessage>();

        String selectQuery = "SELECT  *" +
                " FROM " + BlockMessageTable.BlockMessageEntry.TABLE_NAME +
                " WHERE "+ BlockMessageTable.BlockMessageEntry.NUMBER +" = ?"+
                " ORDER BY "+ BlockMessageTable.BlockMessageEntry.CREATED_AT + " ASC";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[] {number});

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try{
            if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()){
                do {
                    blockMessage.add(new BlockMessage(cursor.getInt(0),cursor.getInt(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),dateFormat.parse(cursor.getString(5))));
                }while (cursor.moveToNext());
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
        finally {
            try {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                cursor = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            db.close();

            SQLiteDatabase db1 = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(BlockMessageTable.BlockMessageEntry.IS_NEW, 0);
            // updating row
            db1.update(BlockMessageTable.BlockMessageEntry.TABLE_NAME, values, BlockMessageTable.BlockMessageEntry.NUMBER + " = ?",
                    new String[] { number });

            db1.close();


        }

        return blockMessage;
    }


}
