package com.example.notepad.controller;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.support.v7.widget.ShareActionProvider;
import android.widget.Toast;

import com.example.notepad.managers.App;
import com.example.notepad.utils.Loader;
import com.example.notepad.R;

public class NoteActivity extends AppCompatActivity {

    public static final String KEY_NOTE_ID = "key_id";
    EditText etTitle;
    EditText etContent;
    Loader loader;
    private ShareActionProvider myShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        etTitle = (EditText) findViewById(R.id.et_title);
        etContent = (EditText) findViewById(R.id.et_content);
        loader = App.getLoader();
        if (isIntentHasExtra()) {
            int id = getNoteId();
            loader.readData(id, etTitle, etContent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        MenuItem deleteItem = menu.findItem(R.id.delete);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        if (!isIntentHasExtra()) {
            deleteItem.setVisible(false);
            shareItem.setVisible(false);
        }
        myShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        setIntent(loader.getContentText(etContent));
        return true;
    }

    private void setIntent(String shareContent) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareContent);
        myShareActionProvider.setShareIntent(intent);
    }

    public int getNoteId() {
        Intent intent = getIntent();
        int id = intent.getIntExtra(KEY_NOTE_ID, 0);
        return id;
    }

    public boolean isIntentHasExtra() {
        Intent intent = getIntent();
        if (intent.hasExtra(KEY_NOTE_ID)) {
            return true;
        } else return false;
    }

    public void saveNote() {
        if(loader.isFieldsEmpty(etTitle, etContent)){
            return;
        }
        int id = getNoteId();
        if (isIntentHasExtra()) {
            if(loader.isNoteEdit(id, etTitle, etContent)){
                return;
            } else {
                loader.saveEditNote(id, etTitle, etContent);
                Toast.makeText(this, "Заметка отредактирована", Toast.LENGTH_SHORT).show();
            }
        } else {
            loader.saveNewNote(etTitle, etContent);
            Toast.makeText(this, "Заметка успешно сохранена", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                finish();
                break;
            case R.id.clear:
                loader.clearNote(etTitle, etContent);
                Toast.makeText(this, "Поля очищены", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                int id = getNoteId();
                loader.deleteNote(id);
                loader.clearNote(etTitle, etContent);
                Toast.makeText(this, "Заметка успешно удалена", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveNote();
    }
}
