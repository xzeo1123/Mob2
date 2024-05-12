package com.example.androidproject.entity;

import java.util.List;

public class Volume {
    private List<Chapter> ChapterList;
    private String Name;

    public Volume() {
    }

    public Volume(List<Chapter> chapterList, String name) {
        ChapterList = chapterList;
        Name = name;
    }

    public List<Chapter> getChapterList() {
        return ChapterList;
    }

    public void setChapterList(List<Chapter> chapterList) {
        ChapterList = chapterList;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
