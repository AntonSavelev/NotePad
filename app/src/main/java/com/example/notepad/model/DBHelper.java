package com.example.notepad.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "noteDb";
    public static final String TABLE_NOTES = "notes";

    public static final String KEY_ID = "_id";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String DATE = "date";
    public static final String PATH_URI_1 = "uri_1";
    public static final String PATH_URI_2 = "uri_2";
    public static final String PATH_URI_3 = "uri_3";
    public static final String PATH_URI_4 = "uri_4";
    public static final String PATH_URI_5 = "uri_5";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NOTES + "(" + KEY_ID
                + " integer primary key autoincrement," + TITLE + " text," + CONTENT + " text," + DATE + " text," + PATH_URI_1 + " text," + PATH_URI_2 + " text,"
                + PATH_URI_3 + " text," + PATH_URI_4 + " text," + PATH_URI_5 + " text" +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NOTES);
        onCreate(sqLiteDatabase);
    }
}
