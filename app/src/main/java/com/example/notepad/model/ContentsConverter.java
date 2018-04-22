package com.example.notepad.model;

import android.arch.persistence.room.TypeConverter;
import android.util.Base64;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ContentsConverter {

    @TypeConverter
    public String fromContents(List<Record> records) {
        StringBuffer sb = new StringBuffer();
        String content;
        for (Record record : records) {
            Gson gson = new Gson();
            content = gson.toJson(record);
            String encodeToString = Base64.encodeToString(content.getBytes(), Base64.DEFAULT);
            sb.append(encodeToString);
            sb.append(",");
        }
        return sb.toString();
    }

    @TypeConverter
    public List<Record> toContents(String data) {
        List<String> strings = Arrays.asList(data.split(","));
        List<Record> decodedRecords = new ArrayList<>(strings.size());
        for (String string : strings) {
            byte[] decode = Base64.decode(string.getBytes(), Base64.DEFAULT);
            String s = new String(decode);
            Record record;
            if (s.contains(".jpg")) {
                record = new Gson().fromJson(s, ImageRecord.class);
            } else {
                record = new Gson().fromJson(s, TextRecord.class);
            }
            decodedRecords.add(record);
        }
        return decodedRecords;
    }
}
