package com.example.androidproject.entity;

import java.io.Serializable;
import java.util.List;

public class Chapter implements Serializable {
    private List<String> Images;
    private String Name;
    private long UploadDate;
    public Chapter() {
    }

    public Chapter(List<String> Images, String name, long UploadDate) {
        this.Images = Images;
        Name = name;
        this.UploadDate = UploadDate;
    }

    public List<String> getImages() {
        return Images;
    }

    public void setImages(List<String> Images) {
        this.Images = Images;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getUploadDate() {
        return UploadDate;
    }
    public void setUploadDate(long UploadDate) {
        this.UploadDate = UploadDate;
    }
}
