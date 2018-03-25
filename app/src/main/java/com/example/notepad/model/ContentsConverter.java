package com.example.notepad.model;

import android.arch.persistence.room.TypeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ContentsConverter {

    @TypeConverter
    public String fromContents(List<String> contents) {
        return contents.stream().collect(Collectors.joining(","));
    }

    @TypeConverter
    public List<String> toContents(String data) {
        return Arrays.asList(data.split(","));
    }
}
