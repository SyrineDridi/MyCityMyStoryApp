package com.example.android.pim.pimandroid.Handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.pim.pimandroid.entities.User;


/**
 * Created by Syrine on 28/03/2017.
 */
public class UserHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "User";

    // Contacts table name
    private static final String TABLE_USER = "users";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FIRSTNAME = "first_name";
    private static final String KEY_LASTNAME = "last_name";
    private static final String KEY_URLIMAGE = "url_image";
    private static final String KEY_TEL = "tel";

    public UserHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER ," + KEY_EMAIL + " TEXT,"
                + KEY_FIRSTNAME + " TEXT," + KEY_LASTNAME+ " TEXT ," +KEY_URLIMAGE+ " TEXT ,"
                +KEY_TEL+ " INTEGER "+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
   public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, 1);
        values.put(KEY_EMAIL, user.getEmail()); // user Email
        values.put(KEY_FIRSTNAME, user.getFirst_name()); // user First_Name
        values.put(KEY_LASTNAME, user.getLast_name()); // user Last_Name
        values.put(KEY_URLIMAGE, user.getUrlImage()); // user url_image
        values.put(KEY_TEL, user.getTel()); // user Phone

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER, new String[] { KEY_ID,
                        KEY_EMAIL, KEY_FIRSTNAME,KEY_LASTNAME , KEY_URLIMAGE,KEY_TEL }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2) ,cursor.getString(3),cursor.getString(4),Integer.parseInt(cursor.getString(5)));
        // return contact
        return user;
    }



    // Updating single contact
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, user.getEmail()); // user Email
        values.put(KEY_FIRSTNAME, user.getFirst_name()); // user First_Name
        values.put(KEY_LASTNAME, user.getLast_name()); // user Last_Name
        values.put(KEY_URLIMAGE, user.getUrlImage()); // user url_image
        values.put(KEY_TEL, user.getTel()); // user Phone

        // updating row
        return db.update(TABLE_USER, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
    }




}