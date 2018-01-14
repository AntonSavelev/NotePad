package com.example.notepad.utils;

import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

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

    public String getContentText(EditText etContent) {
        String content = etContent.getText().toString();
        return content;
    }

    public void readData(int id, EditText etTitle, EditText etContent, ImageView imageView_1, ImageView imageView_2, ImageView imageView_3, ImageView imageView_4, ImageView imageView_5) {
        Note note = peopleDbHelper.getNote(id);
        String title = note.getTitle();
        String content = note.getContent();
        String uri_1 = note.getUri_1();
        String uri_2 = note.getUri_2();
        String uri_3 = note.getUri_3();
        String uri_4 = note.getUri_4();
        String uri_5 = note.getUri_5();
        Log.d("Log_PHOTO_READ", uri_1 + "/" + uri_2 + "/" + uri_3 + "/" + uri_4 + "/" + uri_5);

        etTitle.setText(title);
        etContent.setText(content);
        if (uri_1 != null) {
            imageView_1.setImageURI(Uri.parse(uri_1));
            imageView_1.setVisibility(ImageView.VISIBLE);
        }
        if (uri_2 != null) {
            imageView_2.setImageURI(Uri.parse(uri_2));
            imageView_2.setVisibility(ImageView.VISIBLE);
        }
        if (uri_3 != null) {
            imageView_3.setImageURI(Uri.parse(uri_3));
            imageView_3.setVisibility(ImageView.VISIBLE);
        }
        if (uri_4 != null) {
            imageView_4.setImageURI(Uri.parse(uri_4));
            imageView_4.setVisibility(ImageView.VISIBLE);
        }
        if (uri_5 != null) {
            imageView_5.setImageURI(Uri.parse(uri_5));
            imageView_5.setVisibility(ImageView.VISIBLE);
        }
    }

    public void saveEditNote(int id, EditText etTitle, EditText etContent, String mCurrentPhotoPath_1, String mCurrentPhotoPath_2, String mCurrentPhotoPath_3, String mCurrentPhotoPath_4, String mCurrentPhotoPath_5) {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());
        Note note = peopleDbHelper.getNote(id);
        String uri_1 = note.getUri_1();
        String uri_2 = note.getUri_2();
        String uri_3 = note.getUri_3();
        String uri_4 = note.getUri_4();
        String uri_5 = note.getUri_5();

        if (mCurrentPhotoPath_1 != null) {
            if (uri_1 == null) {
                uri_1 = mCurrentPhotoPath_1;
            } else if (uri_2 == null) {
                uri_2 = mCurrentPhotoPath_1;
            } else if (uri_3 == null) {
                uri_3 = mCurrentPhotoPath_1;
            } else if (uri_4 == null) {
                uri_4 = mCurrentPhotoPath_1;
            } else if (uri_5 == null) {
                uri_5 = mCurrentPhotoPath_1;
            }
        }

        if (mCurrentPhotoPath_2 != null) {
            if (uri_2 == null) {
                uri_2 = mCurrentPhotoPath_2;
            } else if (uri_3 == null) {
                uri_3 = mCurrentPhotoPath_2;
            } else if (uri_4 == null) {
                uri_4 = mCurrentPhotoPath_2;
            } else if (uri_5 == null) {
                uri_5 = mCurrentPhotoPath_2;
            }
        }

        if (mCurrentPhotoPath_3 != null) {
            if (uri_3 == null) {
                uri_3 = mCurrentPhotoPath_3;
            } else if (uri_4 == null) {
                uri_4 = mCurrentPhotoPath_3;
            } else if (uri_5 == null) {
                uri_5 = mCurrentPhotoPath_3;
            }
        }

        if (mCurrentPhotoPath_4 != null) {
            if (uri_4 == null) {
                uri_4 = mCurrentPhotoPath_4;
            } else if (uri_5 == null) {
                uri_5 = mCurrentPhotoPath_4;
            }
        }

        if (mCurrentPhotoPath_5 != null) {
            if (uri_5 == null) {
                uri_5 = mCurrentPhotoPath_5;
            }
        }

        peopleDbHelper.editNote(id, title, content, currentDateAndTime, uri_1, uri_2, uri_3, uri_4, uri_5);
//        peopleDbHelper.editNote(id, title, content, currentDateAndTime, mCurrentPhotoPath_1, mCurrentPhotoPath_2, mCurrentPhotoPath_3, mCurrentPhotoPath_4, mCurrentPhotoPath_5);
        notifyDataLoaded();
    }

    public void saveNewNote(EditText etTitle, EditText etContent, String mCurrentPhotoPath_1, String mCurrentPhotoPath_2, String mCurrentPhotoPath_3, String mCurrentPhotoPath_4, String mCurrentPhotoPath_5) {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        String currentDateAndTime = sdf.format(new Date());
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setDate(currentDateAndTime);
        note.setUri_1(mCurrentPhotoPath_1);
        note.setUri_2(mCurrentPhotoPath_2);
        note.setUri_3(mCurrentPhotoPath_3);
        note.setUri_4(mCurrentPhotoPath_4);
        note.setUri_5(mCurrentPhotoPath_5);
        peopleDbHelper.writeNote(note);
        notifyDataLoaded();
    }

    public boolean isFieldsEmpty(EditText etTitle, EditText etContent) {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        if (title.equals("") && content.equals("")) {
            return true;
        }
        return false;
    }

    public boolean isNoteEdit(int id, EditText etTitle, EditText etContent, String mCurrentPhotoPath_1) {
        Note note = peopleDbHelper.getNote(id);
        String titleNoteDB = note.getTitle();
        String contentNoteDB = note.getContent();
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        if (title.equals(titleNoteDB) && content.equals(contentNoteDB) && mCurrentPhotoPath_1 == null) {
            return true;
        }
        return false;
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
