package com.example.androidproject.entity;

public class Rating {
    private int ratingId;
    private String comment;
    private String ratingDate;
    private String bookId;
    private int userId;

    public Rating() {

    }

    public Rating(int ratingId, String comment, String ratingDate, String bookId, int userId) {
        this.ratingId = ratingId;
        this.comment = comment;
        this.ratingDate = ratingDate;
        this.bookId = bookId;
        this.userId = userId;
    }

    public String getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(String ratingDate) {
        this.ratingDate = ratingDate;
    }

    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
