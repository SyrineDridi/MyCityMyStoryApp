package com.esprit.mycitymystory.Handler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Syrine Dridi on 28/11/2016.
 */

public class ParticipationSqlite extends SQLiteOpenHelper {

    public static final String TABLE_PARTICIPATIONS = "participations";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EVENTID = "event_id";
    public static final String COLUMN_USERID = "user_id";
    public static final String COLUMN_DATE = "date_participation" ;
    public static final String COLUMN_DATE_START = "date_event_start" ;






    private static final String DATABASE_NAME = "events.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_PARTICIPATIONS + "( " + COLUMN_ID
            + " integer primary key autoincrement , " + COLUMN_EVENTID
            + " text not null ," + COLUMN_USERID
            + " text not null ,"+COLUMN_DATE
            + " text not null ,"+COLUMN_DATE_START
            + " text not null "+
            ");";

    public ParticipationSqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(EventsSqlite.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPATIONS);
        onCreate(db);
    }
}
