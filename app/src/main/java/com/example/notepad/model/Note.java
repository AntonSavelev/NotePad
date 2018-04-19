package com.example.notepad.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.notepad.Record;

import java.util.List;

@Entity
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    @TypeConverters({ContentsConverter.class})
    private List<Record> contents;
    private String time;

    public Note(String title, List<Record> contents, String time) {
        this.title = title;
        this.contents = contents;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Record> getContents() {
        return contents;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
