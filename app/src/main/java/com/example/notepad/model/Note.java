package com.example.notepad.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.List;
import java.util.Objects;

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

    public void setContents(List<Record> contents) {
        this.contents = contents;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id == note.id &&
                Objects.equals(contents, note.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contents);
    }
}
