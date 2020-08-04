package in.sanitization.sanitization.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 08,July,2020
 */
public class NotificationHandler extends SQLiteOpenHelper {
    private static String DB_NAME = "a2zntt_db";
    private static int DB_VERSION = 3;
    private SQLiteDatabase db;

    public static final String TABLE_NAME = "notifications";
    public static final String COLUMN_ID = "notification_id";
    public static final String COLUMN_CID = "c_id";
    public static final String COLUMN_TITLE = "title";

    public NotificationHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;
        String exe = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + "(" + COLUMN_CID + " integer primary key , "
                + COLUMN_ID + " TEXT NOT NULL,"
                + COLUMN_TITLE + " TEXT NOT NULL "
                + ")";

        db.execSQL(exe);

    }

    public boolean setNotification(HashMap<String, String> map) {
        db = getWritableDatabase();
        if (isInNotification(map.get(COLUMN_ID))) {
            return false;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CID, map.get(COLUMN_CID));
            values.put(COLUMN_ID, map.get(COLUMN_ID));
            values.put(COLUMN_TITLE, map.get(COLUMN_TITLE));
            db.insert(TABLE_NAME, null, values);
            return true;
        }
    }

    public boolean isInNotification(String id) {
        db = getReadableDatabase();
        String qry = "Select *  from " + TABLE_NAME + " where " + COLUMN_ID + " = '" + id +"'";
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) return true;
        return false;
    }


    public int getNotificationCount() {
        db = getReadableDatabase();
        String qry = "Select *  from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(qry, null);
        return cursor.getCount();
    }




    public ArrayList<HashMap<String, String>> getAllNotification() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select *  from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(COLUMN_CID, cursor.getString(cursor.getColumnIndex(COLUMN_CID)));
            map.put(COLUMN_ID, cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
            map.put(COLUMN_TITLE, cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            list.add(map);
            cursor.moveToNext();
        }
        return list;
    }

    public ArrayList<HashMap<String, String>> getSingleNotification(int id) {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select *  from " + TABLE_NAME+ " where " + COLUMN_ID + " = '" + id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(COLUMN_CID, cursor.getString(cursor.getColumnIndex(COLUMN_CID)));
            map.put(COLUMN_ID, cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
            map.put(COLUMN_TITLE, cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            list.add(map);
            cursor.moveToNext();
        }
        return list;
    }


    public void clearNotifications() {
        db = getReadableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }

    public void removeItemFromNotification(String id) {
        db = getReadableDatabase();
        db.execSQL("delete from " + TABLE_NAME + " where " + COLUMN_ID + " = " + id);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}