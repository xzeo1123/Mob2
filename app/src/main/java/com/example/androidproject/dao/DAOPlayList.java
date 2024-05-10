package com.example.androidproject.dao;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.androidproject.entity.Account;
import com.example.androidproject.entity.PlayList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAOPlayList {
    private static final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

    public interface idFetchCallback {
        void onIDsFetched(List<String> idList);
    }

    public void getIDList(final DAOSignup.idFetchCallback callback) {
        new Thread(() -> {
            final List<String> idlList = new ArrayList<>();
            mData.child("List").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot listSnapshot : dataSnapshot.getChildren()) {
                        Integer storedID = listSnapshot.child("listID").getValue(Integer.class);
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

    public interface nameFetchCallback {
        void onNamesFetched(List<String> nameList);
    }

    public void getNameList(int accountID, final nameFetchCallback callback) {
        new Thread(() -> {
            final List<String> nameList = new ArrayList<>();
            String sAccountID = String.valueOf(accountID);
            mData.child("List").child(sAccountID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot listSnapshot : dataSnapshot.getChildren()) {
                        String storedName = listSnapshot.child("Name").getValue(String.class);
                        if (storedName != null) {
                            nameList.add(storedName);
                        }
                    }
                    new Handler(Looper.getMainLooper()).post(() -> callback.onNamesFetched(nameList));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }).start();
    }
    public interface ListFetchCallback {
        void onListsFetched(PlayList list);
    }

    public void getListById(int playlistId, final ListFetchCallback callback) {
        String sListID = String.valueOf(playlistId);
        mData.child("List").child(sListID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PlayList playlist = dataSnapshot.getValue(PlayList.class);
                callback.onListsFetched(playlist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public interface PlayListFetchCallback {
        void onPlayListsFetched(List<PlayList> playlistList);
    }
    public void getListByAccountId(int accountID, final PlayListFetchCallback callback) {
        new Thread(() -> {
            final List<PlayList> playlistList = new ArrayList<>();
            mData.child("List").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot listSnapshot : dataSnapshot.getChildren()) {
                        // Here, use listSnapshot to retrieve each playlist
                        PlayList playlist = listSnapshot.getValue(PlayList.class);
                        if (playlist != null && playlist.getAccountID() == accountID) {
                            playlistList.add(playlist);
                            // Log the retrieved playlist
                        }
                    }
                    // Log the total number of playlists retrieved
                    Log.d("Playlist", "Total playlists retrieved: " + playlistList.size());

                    new Handler(Looper.getMainLooper()).post(() -> callback.onPlayListsFetched(playlistList));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled event if needed
                    Log.e("Playlist", "Error retrieving playlists: " + databaseError.getMessage());
                }
            });
        }).start();
    }
    public static void addPlayList(int id, PlayList playList) {
        mData.child("List").child(String.valueOf(id)).setValue(null);
        mData.child("List").child(String.valueOf(id)).setValue(playList);
    }
    public void deletePlaylistById(int playlistId, DeleteCallback callback) {
        mData.child("List").child(String.valueOf(playlistId)).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Deletion successful
                    callback.onDeleteSuccess();
                })
                .addOnFailureListener(e -> {
                    // Failed to delete playlist
                    callback.onDeleteFailure(e.getMessage());
                });
    }

    public interface DeleteCallback {
        void onDeleteSuccess();
        void onDeleteFailure(String error);
    }
}
