package com.example.notepad.utils;

import android.widget.EditText;

import com.example.notepad.model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Loader {

    private PeopleDbHelper peopleDbHelper;
    private Set<LoadListener> listeners = new HashSet<>();

    public Loader(PeopleDbHelper peopleDbHelper) {
        this.peopleDbHelper = peopleDbHelper;
    }

    public List<Note> getNotesFromDB() {
        List<Note> notes = peopleDbHelper.getNotes();
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

    public String getContentText(EditText etContent){
        String content = etContent.getText().toString();
        return content;
    }

    public void readData(int id, EditText etTitle, EditText etContent) {
        Note note = peopleDbHelper.getNote(id);
        String title = note.getTitle();
        String content = note.getContent();
        etTitle.setText(title);
        etContent.setText(content);
    }

    public void saveEditNote(int id, EditText etTitle, EditText etContent) {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        peopleDbHelper.editNote(id, title, content, currentDateandTime);
        notifyDataLoaded();
    }

    public void saveNewNote(EditText etTitle, EditText etContent) {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setDate(currentDateandTime);
        peopleDbHelper.writeNote(note);
        notifyDataLoaded();
    }

    public boolean isFieldsEmpty(EditText etTitle, EditText etContent){
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        if(title.equals("") && content.equals("")){
            return true;
        } return false;
    }

    public boolean isNoteEdit(int id, EditText etTitle, EditText etContent){
        Note note = peopleDbHelper.getNote(id);
        String titleNoteDB = note.getTitle();
        String contentNoteDB = note.getContent();
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        if(title.equals(titleNoteDB) && content.equals(contentNoteDB)){
            return true;
        } return false;
    }

    public void deleteNote(int id) {
        peopleDbHelper.deleteNote(id);
        notifyDataLoaded();
    }

    public void clearNote(EditText etTitle, EditText etContent) {
        etTitle.setText("");
        etContent.setText("");
    }
}
