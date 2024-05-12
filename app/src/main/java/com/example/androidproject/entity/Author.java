package com.example.androidproject.entity;

public class Author {
    private int authorID;
    private String information;
    private String name;
    private String picture;

    public Author() {

    }

    public Author(int authorID, String information, String name, String picture) {
        this.authorID = authorID;
        this.information = information;
        this.name = name;
        this.picture = picture;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
