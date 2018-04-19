package com.example.notepad;

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
}
