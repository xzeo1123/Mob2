package com.example.androidproject.dao;

import androidx.annotation.NonNull;

import com.example.androidproject.entity.Account;
import com.example.androidproject.entity.Author;
import com.example.androidproject.entity.DetailBook;
import com.example.androidproject.entity.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DAODetailManga {
    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

    public interface DetailBookFetchCallback {
        void onDetailBookFetched(DetailBook detailBook);
    }
    public void getBookById(String bookId, final DetailBookFetchCallback callback) {
        mData.child("Book").child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DetailBook detailBook = new DetailBook();
                detailBook.setName((String) dataSnapshot.child("name").getValue());
                detailBook.setAuthorId((String) dataSnapshot.child("authorID").getValue());
                detailBook.setPrice(Double.parseDouble(String.valueOf(dataSnapshot.child("price").getValue())));
                detailBook.setBookId(bookId);
                detailBook.setCoverURL((String) dataSnapshot.child("coverURL").getValue());
                callback.onDetailBookFetched(detailBook);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public interface AuthorFetchCallback {
        void onAuthorFetched(Author author);
    }

    public void getAuthorByID(String authorID, final AuthorFetchCallback callback) {
        mData.child("Author").child(authorID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Author author = new Author();
                author.setName(String.valueOf(dataSnapshot.child("Name").getValue()));
                callback.onAuthorFetched(author);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public interface RatingFetchCallback {
        void onAuthorFetched(Rating rating);
    }

    public void getLatestRating(String bookId, final RatingFetchCallback callback) {
        mData.child("Rating").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Rating nearestRating = null;
                String nearestDate = "";

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Rating rating = new Rating();
                    int ratingID = Integer.parseInt(String.valueOf(snapshot.child("RatingId").getValue()));
                    rating.setRatingId(ratingID);
                    rating.setComment(String.valueOf(snapshot.child("Comment").getValue()));
                    rating.setBookId(String.valueOf(snapshot.child("BookID").getValue()));
                    rating.setRatingDate(String.valueOf(snapshot.child("DateRating").getValue()));
                    rating.setUserId(Integer.parseInt(String.valueOf(snapshot.child("AccountID").getValue())));
                    if (rating.getBookId().equals(bookId)) {

                        String ratingDate = rating.getRatingDate();
                        if (nearestRating == null || ratingDate.compareTo(nearestDate) > 0) {
                            nearestRating = rating;
                            nearestDate = ratingDate;
                        }
                    }
                }

                if (nearestRating != null) {
                    callback.onAuthorFetched(nearestRating);
                } else {
                    callback.onAuthorFetched(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public interface UserNameFetchCallback {
        void onUserNameFetched(Account account);
    }

    public void getUserNameByID(String userID, final UserNameFetchCallback callback) {
        mData.child("Account").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Account account = dataSnapshot.getValue(Account.class);
                callback.onUserNameFetched(account);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
