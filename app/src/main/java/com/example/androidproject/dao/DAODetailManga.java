package com.example.androidproject.dao;

import androidx.annotation.NonNull;

import com.example.androidproject.entity.Account;
import com.example.androidproject.entity.DetailBook;
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
//                DetailBook detailBook = dataSnapshot.getValue(DetailBook.class);
                DetailBook detailBook = new DetailBook();
                detailBook.setName((String) dataSnapshot.child("name").getValue());
                detailBook.setAuthorId((String) dataSnapshot.child("authorID").getValue());
                double Price = (double) dataSnapshot.child("price").getValue();
                detailBook.setPrice(Price);
                detailBook.setBookId(bookId);
                detailBook.setCoverURL((String) dataSnapshot.child("coverURL").getValue());
                callback.onDetailBookFetched(detailBook);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
