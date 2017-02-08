package com.example.jawad.smsblocker;

import android.provider.BaseColumns;

/**
 * Created by jawad on 06/05/2016.
 */
public final class BlockMessageTable {

    public BlockMessageTable(){}

    public static abstract class BlockMessageEntry implements BaseColumns {
        public static final String TABLE_NAME = "block_messages";
        public static final String BLOCK_LIST_ID = "block_list_id";
        public static final String NUMBER = "number";
        public static final String MESSAGE = "message";
        public static final String IS_NEW = "is_new";
        public static final String CREATED_AT = "created_at";

    }

    public static final String CREATE_STATEMENT =
            "CREATE TABLE "+ BlockMessageTable.BlockMessageEntry.TABLE_NAME+"(" +
                    BlockMessageEntry._ID+" INTEGER PRIMARY KEY,"+
                    BlockMessageEntry.BLOCK_LIST_ID+" INTEGER,"+
                    BlockMessageEntry.NUMBER+" INT,"+
                    BlockMessageEntry.MESSAGE+" TEXT,"+
                    BlockMessageEntry.IS_NEW+" INT,"+
                    BlockMessageEntry.CREATED_AT+" DATETIME"+
                    ")";

    public static final String DELETE_STATEMENT =
            "DROP TABLE IF EXISTS "+ BlockMessageEntry.TABLE_NAME;

}
