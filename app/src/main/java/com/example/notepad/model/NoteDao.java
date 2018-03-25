package com.example.notepad.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.notepad.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note")
    LiveData<List<Note>> getAll();

    @Query("SELECT * FROM note WHERE id = :id")
    Note getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);
}
