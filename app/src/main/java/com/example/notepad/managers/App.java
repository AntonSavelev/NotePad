package com.example.notepad.managers;

import android.app.Application;

import com.example.notepad.model.DBHelper;
import com.example.notepad.utils.Loader;
import com.example.notepad.utils.PeopleDbHelper;

public class App extends Application {
    private static App instance;
    private static Loader loader;

    public synchronized static Loader getLoader() {
        if (loader == null) {
            DBHelper dbHelper = new DBHelper(instance);
            PeopleDbHelper peopleDbHelper = new PeopleDbHelper(dbHelper);
            loader = new Loader(peopleDbHelper);
        }
        return loader;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
