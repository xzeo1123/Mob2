package com.example.androidproject.dao;

import androidx.annotation.NonNull;

import com.example.androidproject.entity.Card;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DAOPayment {
    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

    public void Pay(int accountID, float money) {
        String sAccountID = String.valueOf(accountID);
        DatabaseReference accountRef = mData.child("Account").child(sAccountID).child("Money");

        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    float currentMoney = dataSnapshot.getValue(Float.class);
                    float updatedMoney = currentMoney + money;
                    accountRef.setValue(updatedMoney);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
