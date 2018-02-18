package com.example.notepad.model;

import java.util.List;

public class Note {
    private int id;
    private String title;
    private List<String> contents;
    private String time;

    public Note(String title, List<String> contents, String time) {
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

    public List<String> getContents() {
        return contents;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
