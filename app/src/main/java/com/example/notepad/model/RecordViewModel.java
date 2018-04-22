package com.example.notepad.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.notepad.managers.App;
import com.example.notepad.model.AppDatabase;
import com.example.notepad.model.Note;
import com.example.notepad.model.NoteDao;

public class RecordViewModel extends ViewModel {

    LiveData<Note> data;

    public RecordViewModel() {
    }

    public LiveData<Note> getData(int id) {
        if (data == null) {
            AppDatabase db = App.getInstance().getDatabase();
            NoteDao noteDao = db.noteDao();
            data = noteDao.getById(id);
        }
        return data;
    }
}
