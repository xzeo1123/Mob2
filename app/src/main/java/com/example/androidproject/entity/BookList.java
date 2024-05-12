package com.example.androidproject.entity;

public class BookList {
    private int playlistID;
    private int bookID;
    public BookList() {

    }

    public BookList(int playlistID, int bookID) {
        this.playlistID = playlistID;
        this.bookID = bookID;
    }

    public int getListID() {
        return playlistID;
    }

    public void setListID(int listID) {
        this.playlistID = listID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }
}
