package com.example.androidproject.dao;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAOCard {
    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

    public interface codeFetchCallback {
        void onCodesFetched(List<String> emailList);
    }

    public void getCodeList(final codeFetchCallback callback) {
        new Thread(() -> {
            final List<String> codeList = new ArrayList<>();
            mData.child("Card").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot accountSnapshot : dataSnapshot.getChildren()) {
                        String storedCode = accountSnapshot.child("code").getValue(String.class);
                        if (storedCode != null) {
                            codeList.add(storedCode);
                        }
                    }
                    new Handler(Looper.getMainLooper()).post(() -> callback.onCodesFetched(codeList));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }).start();
    }

    public interface valueFetchCallback {
        void onValuesFetched(List<String> valueList);
    }

    public void getValueList(final valueFetchCallback callback) {
        new Thread(() -> {
            final List<String> valueList = new ArrayList<>();
            mData.child("Card").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot accountSnapshot : dataSnapshot.getChildren()) {
                        String storedValue = accountSnapshot.child("value").getValue(String.class);
                        if (storedValue != null) {
                            valueList.add(storedValue);
                        }
                    }
                    new Handler(Looper.getMainLooper()).post(() -> callback.onValuesFetched(valueList));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }).start();
    }
}
