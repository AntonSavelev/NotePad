package com.example.notepad;

public class TextRecord implements Record {

    private String mTextRec;

    public TextRecord() {
    }

    public TextRecord(String mTextRec) {
        this.mTextRec = mTextRec;
    }

    public String getTextRec() {
        return mTextRec;
    }

    public void setTextRec(String mTextRec) {
        this.mTextRec = mTextRec;
    }
}
