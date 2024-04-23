package com.example.androidproject.dao;

import com.example.androidproject.entity.Account;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOUpdateInfo {
    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

    public void UpdateInfo(int id, Account account) {
        mData.child("Account").child(String.valueOf(id)).setValue(account);
    }
}
