package com.example.androidproject.entity;

public class List {
    private int listID;
    private String name;
    public List() {

    }

    public List(int listID, String name) {
        this.listID = listID;
        this.name = name;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
