package com.esprit.mycitymystory.Handler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Syrine Dridi on 28/11/2016.
 */

public class UserSqlite extends SQLiteOpenHelper {

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_SEX = "sex";
    public static final String COLUMN_BIRTHDAY = "birthdat";
    public static final String COLUMN_URLIMAGE = "url_image";


    private static final String DATABASE_NAME = "events.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_USERS + "( " + COLUMN_ID
            + " text not null , " + COLUMN_USERNAME
            + " text not null ," + COLUMN_PASSWORD
            + "text not null ," + COLUMN_FIRSTNAME
            + "text not null ," + COLUMN_LASTNAME
            + "text not null ," + COLUMN_CITY
            + "text not null ," + COLUMN_COUNTRY
            + "not null ," + COLUMN_PHONE
            + "not null ," + COLUMN_SEX
            + "test not null ," + COLUMN_BIRTHDAY
            + "text not null ," + COLUMN_URLIMAGE
            + "text not null ," +
            ");";

    public UserSqlite(Context context) {
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}
