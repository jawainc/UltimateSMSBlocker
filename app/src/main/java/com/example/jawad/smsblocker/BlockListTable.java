package com.example.jawad.smsblocker;

import android.provider.BaseColumns;

/**
 * Created by jawad on 06/05/2016.
 */
public final class BlockListTable {

    public BlockListTable(){}

    public static abstract class BlockListEntry implements BaseColumns {
        public static final String TABLE_NAME = "block_lists";

        public static final String TITLE_NUMBER = "title_number";
        public static final String TITLE_RANGE_NUMBER = "title_range_number";
        public static final String NUMBER = "number";
        public static final String RANGE_NUMBER = "range_number";
        public static final String IS_RANGE = "is_range";
        public static final String CREATED_AT = "created_at";

    }

    public static final String CREATE_STATEMENT =
            "CREATE TABLE "+BlockListEntry.TABLE_NAME+"(" +
                    BlockListEntry._ID+" INTEGER PRIMARY KEY,"+
                    BlockListEntry.TITLE_NUMBER+" VARCHAR(20),"+
                    BlockListEntry.TITLE_RANGE_NUMBER+" VARCHAR(20),"+
                    BlockListEntry.NUMBER+" INT,"+
                    BlockListEntry.RANGE_NUMBER+" INT,"+
                    BlockListEntry.IS_RANGE+" BOOLEAN,"+
                    BlockListEntry.CREATED_AT+" DATETIME"+
                    ")";

    public static final String DELETE_STATEMENT =
            "DROP TABLE IF EXISTS "+ BlockListEntry.TABLE_NAME;

}
