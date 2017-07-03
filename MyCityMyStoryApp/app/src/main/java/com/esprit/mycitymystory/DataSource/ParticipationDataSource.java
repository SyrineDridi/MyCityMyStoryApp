package com.esprit.mycitymystory.DataSource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.esprit.mycitymystory.Entities.Participation;
import com.esprit.mycitymystory.Handler.ParticipationSqlite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Syrine Dridi on 28/11/2016.
 */

public class ParticipationDataSource  {
    private SQLiteDatabase database;
        private ParticipationSqlite participationSqlite;
        private String[] allColumns = {participationSqlite.COLUMN_ID,
                participationSqlite.COLUMN_EVENTID,participationSqlite.COLUMN_USERID,participationSqlite.COLUMN_DATE,participationSqlite.COLUMN_DATE_START};
    private String[] allColumnss = {participationSqlite.COLUMN_ID,
            participationSqlite.COLUMN_EVENTID};

        public ParticipationDataSource(Context context) {
            participationSqlite = new ParticipationSqlite(context);
        }

        public void open() throws SQLException {
            database = participationSqlite.getWritableDatabase();
        }

        public void close() {
            participationSqlite.close();
        }

        private Participation cursorToEvent(Cursor cursor) {
            Participation event = new Participation();
            event.setId(cursor.getString(0));
            event.setEvent_id(cursor.getString(1));
            event.setUser_id(cursor.getString(2));
            event.setDate_participation(cursor.getString(3));
            event.setDate_event_start(cursor.getString(4));
            return event;
        }

        public Participation showParticipation(String id) {
            SQLiteDatabase db = participationSqlite.getReadableDatabase();
            Cursor cursor = db.query(participationSqlite.TABLE_PARTICIPATIONS, new String[]{participationSqlite.COLUMN_ID,
                            participationSqlite.COLUMN_EVENTID,participationSqlite.COLUMN_USERID,participationSqlite.COLUMN_DATE,participationSqlite.COLUMN_DATE_START}, participationSqlite.COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();


            Participation newParticipation = cursorToEvent(cursor);
            cursor.close();
            return newParticipation;
        }

        public Participation createParticipation (String user_id ,String event_id, String date,String date_start) {
            ContentValues values = new ContentValues();

            values.put(participationSqlite.COLUMN_EVENTID, event_id);
            values.put(participationSqlite.COLUMN_USERID, user_id);
            values.put(participationSqlite.COLUMN_DATE, date);
            values.put(participationSqlite.COLUMN_DATE_START, date_start);

            long insertId = database.insert(participationSqlite.TABLE_PARTICIPATIONS, null,
                    values);
            Cursor cursor = database.query(participationSqlite.TABLE_PARTICIPATIONS, allColumns, participationSqlite.COLUMN_ID + " = " + insertId,
                    null, null, null, null);

            cursor.moveToFirst();
            Participation newParticipaton = cursorToEvent(cursor);
            cursor.close();
            return newParticipaton;
        }

        public void deleteEvent(Participation event) {
            String id = event.getId();
            System.out.println("Comment deleted with id: " + id);
            database.delete(participationSqlite.TABLE_PARTICIPATIONS, participationSqlite.COLUMN_ID
                    + " = " + id, null);
        }

        public List<Participation> getAllEvents() {
            List<Participation> participations = new ArrayList<>();

            Cursor cursor = database.query(participationSqlite.TABLE_PARTICIPATIONS,
                    allColumns, null, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Participation event = cursorToEvent(cursor);
                participations.add(event);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
            return participations;
        }

        public List<Participation> FindByUSERIF(String user_id) {
            List<Participation> events = new ArrayList<>();
            SQLiteDatabase db = participationSqlite.getReadableDatabase();
            Cursor cursor = db.query(participationSqlite.TABLE_PARTICIPATIONS, new String[] { participationSqlite.COLUMN_USERID,
                            participationSqlite.COLUMN_USERID  }, participationSqlite.COLUMN_USERID + "=?",
                    new String[] { String.valueOf(user_id) }, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Participation event = cursorToEvent(cursor);
                events.add(event);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
            return events ;
        }


}
