package com.example.androidproject.entity;

public class BookList {
    private int listID;
    private int bookID;
    public BookList() {

    }

    public BookList(int listID, int bookID) {
        this.listID = listID;
        this.bookID = bookID;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }
}
