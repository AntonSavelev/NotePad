package com.example.notepad.model;

import java.util.Objects;

public class TextRecord implements Record {

    private String mTextRec;

    public TextRecord(String mTextRec) {
        this.mTextRec = mTextRec;
    }

    public String getTextRec() {
        return mTextRec;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextRecord that = (TextRecord) o;
        return Objects.equals(mTextRec, that.mTextRec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mTextRec);
    }
}
