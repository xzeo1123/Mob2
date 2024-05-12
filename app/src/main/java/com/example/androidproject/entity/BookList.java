package com.example.androidproject.entity;

public class BookList {
    private int playlistID;
    private String bookID;
    public BookList() {

    }

    public BookList(int playlistID, String bookID) {
        this.playlistID = playlistID;
        this.bookID = bookID;
    }

    public int getListID() {
        return playlistID;
    }

    public void setListID(int listID) {
        this.playlistID = listID;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }
}
