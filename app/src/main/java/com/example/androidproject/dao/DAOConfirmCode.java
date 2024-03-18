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

public class DAOConfirmCode {
    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

    public interface idFetchCallback {
        void onIDsFetched(List<String> codeList);
    }

    public void getIDList(final idFetchCallback callback) {
        new Thread(() -> {
            final List<String> idlList = new ArrayList<>();
            mData.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot accountSnapshot : dataSnapshot.getChildren()) {
                        Integer storedID = accountSnapshot.child("accountID").getValue(Integer.class);
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

    public interface emailFetchCallback {
        void onEmailsFetched(List<String> emailList);
    }

    public void getEmailList(final emailFetchCallback callback) {
        new Thread(() -> {
            final List<String> emailList = new ArrayList<>();
            mData.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot accountSnapshot : dataSnapshot.getChildren()) {
                        String storedEmail = accountSnapshot.child("email").getValue(String.class);
                        if (storedEmail != null) {
                            emailList.add(storedEmail);
                        }
                    }
                    new Handler(Looper.getMainLooper()).post(() -> callback.onEmailsFetched(emailList));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }).start();
    }

    public interface codeFetchCallback {
        void onCodesFetched(List<String> codeList);
    }

    public void getCodeList(final codeFetchCallback callback) {
        new Thread(() -> {
            final List<String> codelList = new ArrayList<>();
            mData.child("Account").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot accountSnapshot : dataSnapshot.getChildren()) {
                        Integer storedCode = accountSnapshot.child("confirmedCode").getValue(Integer.class);
                        String sCode = String.valueOf(storedCode);
                        codelList.add(sCode);
                    }
                    new Handler(Looper.getMainLooper()).post(() -> callback.onCodesFetched(codelList));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }).start();
    }

    public void updateCode(String userId) {
        mData.child("Account").child(userId).child("confirmedCode").setValue(999999);
    }
}
