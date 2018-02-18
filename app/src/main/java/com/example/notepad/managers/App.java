package com.example.notepad.managers;

import android.app.Application;

import com.example.notepad.model.DBHelper;
import com.example.notepad.utils.Loader;
import com.example.notepad.utils.NoteDbHelper;

public class App extends Application {
    private static App instance;
    private static Loader loader;

    public synchronized static Loader getLoader() {
        if (loader == null) {
            DBHelper dbHelper = new DBHelper(instance);
            NoteDbHelper noteDbHelper = new NoteDbHelper(dbHelper);
            loader = new Loader(noteDbHelper);
        }
        return loader;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
