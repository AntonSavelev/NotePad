package com.example.notepad.controller;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.notepad.model.AppDatabase;
import com.example.notepad.model.NoteDao;
import com.example.notepad.model.Note;
import com.example.notepad.R;
import com.example.notepad.adapter.MyAdapter;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    MyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.r_view);
        final GridLayoutManager layoutManager = new GridLayoutManager(NoteListActivity.this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter();
        adapter.setListener(new MyAdapter.Listener() {
            @Override
            public void onClick(int noteId) {
                editNote(noteId);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newNote();
            }
        });
    }

    public void editNote(int noteId) {
        Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
        intent.putExtra(NoteActivity.KEY_NOTE_ID, noteId);
        startActivity(intent);
    }

    public void newNote() {
        Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
        startActivity(intent);
    }

    public void setDataInAdapter() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database").allowMainThreadQueries().build();
        NoteDao noteDao = db.noteDao();
        LiveData<List<Note>> notesLiveData = noteDao.getAll();
        notesLiveData.observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                adapter.setData(notes);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setDataInAdapter();
    }
}
