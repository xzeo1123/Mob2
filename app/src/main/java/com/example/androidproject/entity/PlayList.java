package com.example.androidproject.entity;

public class PlayList {
    private int listID;
    private String name;
    private int accountID;
    public PlayList() {

    }

    public PlayList(int ListID, String Name, int AccountID) {
        this.listID = ListID;
        this.name = Name;
        this.accountID = AccountID;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int ListID) {
        this.listID = ListID;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int AccountID) {
        this.accountID = AccountID;
    }
}
