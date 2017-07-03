package com.esprit.mycitymystory.DataSource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import com.esprit.mycitymystory.Entities.EntityEvent;
import com.esprit.mycitymystory.Handler.EventsSqlite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Syrine on 22/10/2016.
 */

public class EventDataSource {

    private SQLiteDatabase database;
    private EventsSqlite eventsSqlite;
    private String[] allColumns = {eventsSqlite.COLUMN_ID,
            eventsSqlite.COLUMN_EVENT};

    public EventDataSource(Context context) {
        eventsSqlite = new EventsSqlite(context);
    }

    public void open() throws SQLException {
        database = eventsSqlite.getWritableDatabase();
    }

    public void close() {
        eventsSqlite.close();
    }

    private EntityEvent cursorToEvent(Cursor cursor) {
        EntityEvent event = new EntityEvent();
        event.setId(cursor.getString(0));
        event.setTitle(cursor.getString(1));
        return event;
    }

    public EntityEvent showEvent(int id) {
        SQLiteDatabase db = eventsSqlite.getReadableDatabase();
        Cursor cursor = db.query(eventsSqlite.TABLE_EVENTS, new String[]{eventsSqlite.COLUMN_ID,
                        eventsSqlite.COLUMN_EVENT}, eventsSqlite.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();


        EntityEvent newEvent = cursorToEvent(cursor);
        cursor.close();
        return newEvent;
    }

    public EntityEvent createEvent(String event) {
        ContentValues values = new ContentValues();
        values.put(eventsSqlite.COLUMN_EVENT, event);
        long insertId = database.insert(eventsSqlite.TABLE_EVENTS, null,
                values);
        Cursor cursor = database.query(eventsSqlite.TABLE_EVENTS, allColumns, eventsSqlite.COLUMN_ID + " = " + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        EntityEvent newEvent = cursorToEvent(cursor);
        cursor.close();
        return newEvent;
    }

    public void deleteEvent(EntityEvent event) {
        String id = event.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(eventsSqlite.TABLE_EVENTS, eventsSqlite.COLUMN_ID
                + " = " + id, null);
    }

    public List<EntityEvent> getAllEvents() {
        List<EntityEvent> events = new ArrayList<>();

        Cursor cursor = database.query(eventsSqlite.TABLE_EVENTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            EntityEvent event = cursorToEvent(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return events;
    }

    public int getEventCount() {
        String countQuery = "SELECT  * FROM " + eventsSqlite.TABLE_EVENTS;
        SQLiteDatabase db = eventsSqlite.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    public int updateEvent(EntityEvent event) {
        SQLiteDatabase db = eventsSqlite.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(eventsSqlite.TABLE_EVENTS, event.getTitle());


        // updating row
        return db.update(eventsSqlite.TABLE_EVENTS, values, null,
                new String[]{String.valueOf(event.getId())});
    }



    public boolean updatedetails(int rowId,String event)
    {
        SQLiteDatabase db = eventsSqlite.getWritableDatabase();
        ContentValues args = new ContentValues();

        args.put(eventsSqlite.COLUMN_EVENT, event);

        return   db.update(eventsSqlite.TABLE_EVENTS, args, eventsSqlite.COLUMN_ID + "=" + rowId, null) > 0;
    }

    public List<EntityEvent> FindByName(String name) {
        List<EntityEvent> events = new ArrayList<>();
        SQLiteDatabase db = eventsSqlite.getReadableDatabase();
        Cursor cursor = db.query(eventsSqlite.TABLE_EVENTS, new String[] { eventsSqlite.COLUMN_EVENT ,
                        eventsSqlite.COLUMN_EVENT  }, eventsSqlite.COLUMN_EVENT + "=?",
                new String[] { String.valueOf(name) }, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            EntityEvent event = cursorToEvent(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return events ;
    }


}
