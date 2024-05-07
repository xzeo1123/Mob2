package com.example.androidproject.entity;

import java.util.List;

public class Book {
    private String AuthorID;
    private String BookID;
    private String Name;
    private double  price;
    private long UploadDate;
    private String CoverURL;
    private List<Volume> Volume;

    public Book() {
    }

    public Book(String authorID, String bookID, String name, double price, long uploadDate, String CoverURL,List<Volume> volume) {
        AuthorID = authorID;
        BookID = bookID;
        Name = name;
        this.price = price;
        UploadDate = uploadDate;
        this.CoverURL = CoverURL;
        Volume = volume;
    }

    public String getAuthorID() {
        return AuthorID;
    }

    public void setAuthorID(String authorID) {
        AuthorID = authorID;
    }

    public String getBookID() {
        return BookID;
    }

    public void setBookID(String bookID) {
        BookID = bookID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getUploadDate() {
        return UploadDate;
    }

    public void setUploadDate(long uploadDate) {
        UploadDate = uploadDate;
    }
    public String getCoverURL() {
        return CoverURL;
    }

    public void setCoverURL(String coverURL) {
        CoverURL = coverURL;
    }

    public List<Volume> getVolume() {
        return Volume;
    }

    public void setVolume(List<Volume> volume) {
        Volume = volume;
    }
}
