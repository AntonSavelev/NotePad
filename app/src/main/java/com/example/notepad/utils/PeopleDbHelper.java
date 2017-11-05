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

    public void editNote(int id, String title, String content, String date) {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        writableDatabase.beginTransaction();
        try {
            cv.put(DBHelper.TITLE, title);
            cv.put(DBHelper.CONTENT, content);
            cv.put(DBHelper.DATE, date);
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
            cv.put(DBHelper.TITLE, title);
            cv.put(DBHelper.CONTENT, content);
            cv.put(DBHelper.DATE, date);
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
        String titleNote = cursor.getString(titleIndex);
        String contentNote = cursor.getString(contentIndex);
        String dateNote = cursor.getString(dateIndex);
        Note note = new Note(noteId, titleNote, contentNote, dateNote);
        return note;
    }
}
