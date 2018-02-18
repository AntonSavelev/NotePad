package com.example.notepad.utils;

import com.example.notepad.model.Note;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Loader {

    private NoteDbHelper noteDbHelper;
    private Set<LoadListener> listeners = new HashSet<>();

    public Loader(NoteDbHelper noteDbHelper) {
        this.noteDbHelper = noteDbHelper;
    }

    public List<Note> getNotesFromDB() {
        List<Note> notes = noteDbHelper.getNotes();
        return notes;
    }

    public void notifyDataLoaded() {
        Iterator<LoadListener> iterator = listeners.iterator();
        if (iterator.hasNext()) {
            LoadListener listener = iterator.next();
            listener.onChangesSaved();
        }
    }

    public boolean addLoadListener(LoadListener loadListener) {
        return listeners.add(loadListener);
    }

    public void removeLoadListener(LoadListener loadListener) {
        if (!listeners.remove(loadListener)) {
        }
    }
}
