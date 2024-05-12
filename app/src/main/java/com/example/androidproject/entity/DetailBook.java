package com.example.androidproject.entity;

public class DetailBook {
    private String BookId;
    private String AuthorId;
    private String Name;
    private String CoverURL;
    private double price;

    public DetailBook() {
    }

    public DetailBook(String bookId, String authorId, String name, String coverURL, double price) {
        BookId = bookId;
        AuthorId = authorId;
        Name = name;
        CoverURL = coverURL;
        this.price = price;
    }

    public String getBookId() {
        return BookId;
    }

    public void setBookId(String bookId) {
        BookId = bookId;
    }

    public String getAuthorId() {
        return AuthorId;
    }

    public void setAuthorId(String authorId) {
        AuthorId = authorId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCoverURL() {
        return CoverURL;
    }

    public void setCoverURL(String coverURL) {
        CoverURL = coverURL;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
