package com.example.androidproject.dao;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.example.androidproject.entity.BookList;
import com.example.androidproject.entity.PlayList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAOBookList {
    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
    
    public interface BookIdFetchCallback {
        void onBookIdsFetched(List<String> bookIdList);
    }

    public void getbookIdList(int listID, final BookIdFetchCallback callback) {
        new Thread(() -> {
            final List<String> bookIDList = new ArrayList<>();
            String slistID = String.valueOf(listID);
            mData.child("Book List").child(slistID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot booklistSnapshot : dataSnapshot.getChildren()) {
                        String storedBookID = booklistSnapshot.child("bookID").getValue(String.class);
                        if (storedBookID != null) {
                            bookIDList.add(storedBookID);
                        }
                    }
                    new Handler(Looper.getMainLooper()).post(() -> callback.onBookIdsFetched(bookIDList));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }).start();
    }
}
