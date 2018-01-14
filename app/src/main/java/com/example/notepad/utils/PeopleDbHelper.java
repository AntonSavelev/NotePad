package com.example.notepad.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.notepad.model.DBHelper;
import com.example.notepad.model.Note;

import java.util.ArrayList;
import java.util.List;

public class PeopleDbHelper {
    private DBHelper helper;

    public PeopleDbHelper(DBHelper dbHelper) {
        this.helper = dbHelper;
    }

    public List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase readableDatabase = helper.getReadableDatabase();
        String orderBy = DBHelper.KEY_ID + " DESC";
        Cursor cursor = readableDatabase.query(DBHelper.TABLE_NOTES, null, null, null, null, null, orderBy);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.TITLE);
            int contentIndex = cursor.getColumnIndex(DBHelper.CONTENT);
            int dateIndex = cursor.getColumnIndex(DBHelper.DATE);
            do {
                int id = cursor.getInt(idIndex);
                String title = cursor.getString(nameIndex);
                String content = cursor.getString(contentIndex);
                String date = cursor.getString(dateIndex);
                Note note = new Note(id, title, content, date);
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    public void editNote(int id, String title, String content, String date, String path_uri_1, String path_uri_2, String path_uri_3, String path_uri_4, String path_uri_5) {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        writableDatabase.beginTransaction();
        try {
            cv.put(DBHelper.TITLE, title);
            cv.put(DBHelper.CONTENT, content);
            cv.put(DBHelper.DATE, date);
            cv.put(DBHelper.PATH_URI_1, path_uri_1);
            cv.put(DBHelper.PATH_URI_2, path_uri_2);
            cv.put(DBHelper.PATH_URI_3, path_uri_3);
            cv.put(DBHelper.PATH_URI_4, path_uri_4);
            cv.put(DBHelper.PATH_URI_5, path_uri_5);
            writableDatabase.update(DBHelper.TABLE_NOTES, cv, DBHelper.KEY_ID + " = " + id, null);
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public void writeNote(Note note) {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        writableDatabase.beginTransaction();
        try {
            String title = note.getTitle();
            String content = note.getContent();
            String date = note.getDate();
            String uri_1 = note.getUri_1();
            String uri_2 = note.getUri_2();
            String uri_3 = note.getUri_3();
            String uri_4 = note.getUri_4();
            String uri_5 = note.getUri_5();
            cv.put(DBHelper.TITLE, title);
            cv.put(DBHelper.CONTENT, content);
            cv.put(DBHelper.DATE, date);
            cv.put(DBHelper.PATH_URI_1, uri_1);
            cv.put(DBHelper.PATH_URI_2, uri_2);
            cv.put(DBHelper.PATH_URI_3, uri_3);
            cv.put(DBHelper.PATH_URI_4, uri_4);
            cv.put(DBHelper.PATH_URI_5, uri_5);
            writableDatabase.insert(DBHelper.TABLE_NOTES, null, cv);
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public void deleteNote(int noteId) {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        String selection = DBHelper.KEY_ID + " = " + noteId;
        writableDatabase.beginTransaction();
        try {
            writableDatabase.delete(DBHelper.TABLE_NOTES, selection, null);
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public Note getNote(int noteId) {
        SQLiteDatabase readableDatabase = helper.getReadableDatabase();
        String selection = DBHelper.KEY_ID + " = " + noteId;
        Cursor cursor = readableDatabase.query(DBHelper.TABLE_NOTES, null, selection, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        int titleIndex = cursor.getColumnIndex(DBHelper.TITLE);
        int contentIndex = cursor.getColumnIndex(DBHelper.CONTENT);
        int dateIndex = cursor.getColumnIndex(DBHelper.DATE);
        int uriIndex_1 = cursor.getColumnIndex(DBHelper.PATH_URI_1);
        int uriIndex_2 = cursor.getColumnIndex(DBHelper.PATH_URI_2);
        int uriIndex_3 = cursor.getColumnIndex(DBHelper.PATH_URI_3);
        int uriIndex_4 = cursor.getColumnIndex(DBHelper.PATH_URI_4);
        int uriIndex_5 = cursor.getColumnIndex(DBHelper.PATH_URI_5);
        String titleNote = cursor.getString(titleIndex);
        String contentNote = cursor.getString(contentIndex);
        String dateNote = cursor.getString(dateIndex);
        String uri_1 = cursor.getString(uriIndex_1);
        String uri_2 = cursor.getString(uriIndex_2);
        String uri_3 = cursor.getString(uriIndex_3);
        String uri_4 = cursor.getString(uriIndex_4);
        String uri_5 = cursor.getString(uriIndex_5);
        Note note = new Note(noteId, titleNote, contentNote, dateNote, uri_1, uri_2, uri_3, uri_4, uri_5);
        return note;
    }
}
