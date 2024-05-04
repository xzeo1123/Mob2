package com.example.androidproject.entity;

public class List {
    private int listID;
    private String name;
    private int accountID;
    public List() {

    }

    public List(int listID, String name, int accountID) {
        this.listID = listID;
        this.name = name;
        this.accountID = accountID;
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

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }
}
