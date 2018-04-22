package com.example.notepad.model;

import java.util.Objects;

public class ImageRecord implements Record {

    private String mPhotoUrl;

    public ImageRecord() {
    }

    public ImageRecord(String mPhotoUrl) {

        this.mPhotoUrl = mPhotoUrl;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageRecord that = (ImageRecord) o;
        return Objects.equals(mPhotoUrl, that.mPhotoUrl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(mPhotoUrl);
    }
}
