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

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NOTES + "(" + KEY_ID
                + " integer primary key autoincrement," + TITLE + " text," + CONTENT + " text," + DATE + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NOTES);
        onCreate(sqLiteDatabase);
    }
}