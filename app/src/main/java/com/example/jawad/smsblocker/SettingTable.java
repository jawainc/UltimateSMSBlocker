package com.example.jawad.smsblocker;

import android.provider.BaseColumns;

/**
 * Created by jawad on 7/5/2016.
 */
public class SettingTable {
    public SettingTable(){}

    public static abstract class SettingEntry implements BaseColumns {
        public static final String TABLE_NAME = "settings";

        public static final String BLOCK_UNKOWN = "block_unkown";
        public static final String NO_DAYS = "no_days";
    }

    public static final String CREATE_STATEMENT =
            "CREATE TABLE "+ SettingTable.SettingEntry.TABLE_NAME+"(" +
                    SettingTable.SettingEntry._ID+" INTEGER PRIMARY KEY,"+
                    SettingTable.SettingEntry.BLOCK_UNKOWN+" BOOLEAN,"+
                    SettingTable.SettingEntry.NO_DAYS+" INT"+
                    "); "+
            "INSERT INTO "+SettingTable.SettingEntry.TABLE_NAME+" VALUES(1, 0, 0)"
            ;

    public static final String DELETE_STATEMENT =
            "DROP TABLE IF EXISTS "+ SettingTable.SettingEntry.TABLE_NAME;
}
