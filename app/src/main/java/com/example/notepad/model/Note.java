package com.example.notepad.model;

public class Note {
    int id;
    private String title;
    private String content;
    private String date;
    private String uri_1;
    private String uri_2;
    private String uri_3;
    private String uri_4;
    private String uri_5;

    public Note(int id, String title, String content, String date, String uri_1, String uri_2, String uri_3, String uri_4, String uri_5) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.uri_1 = uri_1;
        this.uri_2 = uri_2;
        this.uri_3 = uri_3;
        this.uri_4 = uri_4;
        this.uri_5 = uri_5;
    }

    public Note(int id, String title, String content, String date, String uri_1, String uri_2, String uri_3, String uri_4) {

        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.uri_1 = uri_1;
        this.uri_2 = uri_2;
        this.uri_3 = uri_3;
        this.uri_4 = uri_4;
    }

    public Note(int id, String title, String content, String date, String uri_1, String uri_2, String uri_3) {

        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.uri_1 = uri_1;
        this.uri_2 = uri_2;
        this.uri_3 = uri_3;
    }

    public Note(int id, String title, String content, String date, String uri_1, String uri_2) {

        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.uri_1 = uri_1;
        this.uri_2 = uri_2;
    }

    public Note() {
    }

    public Note(int id, String title, String content, String date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public Note(int id, String title, String content, String date, String uri_1) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.uri_1 = uri_1;
    }

    public String getUri_2() {
        return uri_2;
    }

    public void setUri_2(String uri_2) {
        this.uri_2 = uri_2;
    }

    public String getUri_3() {
        return uri_3;
    }

    public void setUri_3(String uri_3) {
        this.uri_3 = uri_3;
    }

    public String getUri_4() {
        return uri_4;
    }

    public void setUri_4(String uri_4) {
        this.uri_4 = uri_4;
    }

    public String getUri_5() {
        return uri_5;
    }

    public void setUri_5(String uri_5) {
        this.uri_5 = uri_5;
    }

    public String getUri_1() {
        return uri_1;
    }

    public void setUri_1(String uri_1) {
        this.uri_1 = uri_1;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
