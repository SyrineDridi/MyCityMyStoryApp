package com.esprit.mycitymystory.Handler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Syrine on 22/10/2016.
 */

public class EventsSqlite  extends SQLiteOpenHelper{

    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EVENT = "title";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_CATEGORIE = "categorie";
    public static final String COLUMN_PLACE = "place";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_USERID = "user_id";
    public static final String COLUMN_URLIMAGE = "urlImage";
    public static final String COLUMN_NBPLACE = "nbPlace";
    public static final String COLUMN_NBPLACEDISPO = "nbPlaceDispo" ;
    public static final String COLUMN_STARTDATE  = "startDate" ;
    public static final String COLUMN_ENDDATE  = "endDate";
    public static final String COLUMN_STARTTIME = "startTime"   ;
    public static final String COLUMN_ENDTIME ="endTime" ;





    private static final String DATABASE_NAME = "events.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_EVENTS + "( " + COLUMN_ID
            + " text not null , " + COLUMN_EVENT
            + " text not null ," + COLUMN_DESC
            + "text not null ,"+COLUMN_CATEGORIE
            + "text not null ,"+COLUMN_PLACE
            + "text not null ,"+COLUMN_USERID
            + "text not null ,"+COLUMN_URLIMAGE
            + "not null ,"+COLUMN_NBPLACE
            + "not null ,"+COLUMN_NBPLACEDISPO
            + "test not null ,"+COLUMN_STARTDATE
            + "text not null ,"+COLUMN_ENDDATE
            + "text not null ,"+COLUMN_ENDTIME
            + "text not null ,"+COLUMN_STARTTIME
            + "not null ,"+COLUMN_LONGITUDE
            + "not null ,"+COLUMN_LATITUDE
            + "text not null ,"+
            ");";

    public EventsSqlite(Context context) {
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }
}
