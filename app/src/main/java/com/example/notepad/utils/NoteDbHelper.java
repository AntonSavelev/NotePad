package com.example.notepad.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.notepad.model.DBHelper;
import com.example.notepad.model.Note;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class NoteDbHelper {
    private DBHelper helper;

    public NoteDbHelper(DBHelper dbHelper) {
        this.helper = dbHelper;
    }

    public List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase readableDatabase = helper.getReadableDatabase();
        String orderBy = DBHelper.KEY_ID + " DESC";
        Cursor cursor = readableDatabase.query(DBHelper.TABLE_NOTES, null, null, null, null, null, orderBy);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int noteIndex = cursor.getColumnIndex(DBHelper.NOTE);
            do {
                int id = cursor.getInt(idIndex);
                String jsonNote = cursor.getString(noteIndex);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                Note note = gson.fromJson(jsonNote, Note.class);
                note.setId(id);
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    public void editNote(int id, Note note) {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        writableDatabase.beginTransaction();
        try {
            Gson gson = new Gson();
            String jsonNote = gson.toJson(note);
            cv.put(DBHelper.NOTE, jsonNote);
            writableDatabase.update(DBHelper.TABLE_NOTES, cv, DBHelper.KEY_ID + " = " + id, null);
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    public void saveNote(Note note) {
        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        writableDatabase.beginTransaction();
        try {
            Gson gson = new Gson();
            String jsonNote = gson.toJson(note);
            cv.put(DBHelper.NOTE, jsonNote);
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
        int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
        int noteIndex = cursor.getColumnIndex(DBHelper.NOTE);
        int id = cursor.getInt(idIndex);
        String jsonNote = cursor.getString(noteIndex);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Note note = gson.fromJson(jsonNote, Note.class);
        return note;
    }
}
