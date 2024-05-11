package com.example.androidproject.dao;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.example.androidproject.entity.Account;
import com.example.androidproject.entity.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAOWriteCmt {
    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

    public interface idFetchCallback {
        void onIDsFetched(List<String> idList);
    }

    public void getIDList(final idFetchCallback callback) {
        new Thread(() -> {
            final List<String> idlList = new ArrayList<>();
            mData.child("Rating").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot accountSnapshot : dataSnapshot.getChildren()) {
                        Integer storedID = accountSnapshot.child("RatingID").getValue(Integer.class);
                        String sID = String.valueOf(storedID);
                        idlList.add(sID);
                    }
                    new Handler(Looper.getMainLooper()).post(() -> callback.onIDsFetched(idlList));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }).start();
    }

    public void writeComment(int id, Rating rating) {
        mData.child("Rating").child(String.valueOf(id)).setValue(null);
        mData.child("Rating").child(String.valueOf(id)).setValue(rating);
    }
}
