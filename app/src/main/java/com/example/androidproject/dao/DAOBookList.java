package com.example.androidproject.dao;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.androidproject.entity.Book;
import com.example.androidproject.entity.BookList;
import com.example.androidproject.entity.PlayList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DAOBookList {
    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();



    public interface BookFetchCallback {
        void onBooksFetched(List<Book> books);
    }

    public void getBookListByListID(int listID, final BookFetchCallback callback) {
        new Thread(() -> {
            final List<String> bookIDList = new ArrayList<>();
            mData.child("Book List").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot listSnapshot : dataSnapshot.getChildren()) {
                        Integer snapshotListID = listSnapshot.child("listID").getValue(Integer.class); // Retrieve the list ID from the snapshot
                        if (snapshotListID != null && snapshotListID == listID) { // Check if the retrieved list ID matches the specified list ID
                            String bookID = listSnapshot.child("bookID").getValue(String.class);
                            if (bookID != null) {
                                bookIDList.add(bookID);
                            }
                        }
                    }
                    Log.d("Book List", "Total book IDs retrieved: " + bookIDList.size());
                    // Retrieve books from bookIDList
                    List<Book> books = new ArrayList<>();
                    mData.child("Book").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot bookSnapshot : dataSnapshot.getChildren()) {
                                Book book = new Book();
                                if (bookIDList.contains(bookSnapshot.getKey())) {
                                    book.setBookID(bookSnapshot.getKey());
                                    book.setPrice(bookSnapshot.child("price").getValue(Double.class));
                                    book.setAuthorID(bookSnapshot.child("authorID").getValue(String.class));
                                    book.setName(bookSnapshot.child("name").getValue(String.class));
                                    book.setUploadDate(bookSnapshot.child("uploadDate").getValue(Long.class));
                                    book.setCoverURL(bookSnapshot.child("coverURL").getValue(String.class));
                                    books.add(book);
                                }
                                Log.d("Book List", "book retrieved: " + bookSnapshot.getKey());
                            }
                            // Log the total number of books retrieved
                            new Handler(Looper.getMainLooper()).post(() -> callback.onBooksFetched(books));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle onCancelled event if needed
                            Log.e("Book List", "Error retrieving books: " + databaseError.getMessage());
                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled event if needed
                    Log.e("Book List", "Error retrieving book IDs: " + databaseError.getMessage());
                }
            });
        }).start();
    }

    public void deleteBooklistById(String bookID, int listID, DAOBookList.DeleteCallback callback) {
        new Thread(() -> {
            mData.child("Book List").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot listSnapshot : dataSnapshot.getChildren()) {
                        Integer snapshotListID = listSnapshot.child("listID").getValue(Integer.class); // Retrieve the list ID from the snapshot
                        String snapshotBookID = listSnapshot.child("bookID").getValue(String.class); // Retrieve the list ID from the snapshot
                        if (snapshotListID != null && snapshotListID == listID && Objects.equals(snapshotBookID, bookID)) { // Check if the retrieved list ID matches the specified list ID
                            listSnapshot.getRef().removeValue()
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Book List", "Book list entry deleted: " + bookID);
                                        callback.onDeleteSuccess();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Book List", "Error deleting book list entry: " + e.getMessage());
                                        callback.onDeleteFailure(e.getMessage());
                                    });
                        }
                    }
                    Log.d("Book List", "book IDs delete: " + bookID + " at " + listID);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled event if needed
                    Log.e("Book List", "Error delete book: " + databaseError.getMessage());
                }
            });
        }).start();
    }
    public void addBookList(BookList bookList) {
        mData.child("Book List").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean bookListExists = false;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    BookList snapshot = childSnapshot.getValue(BookList.class);
                    Log.d(snapshot.getBookID(), "BL "+ snapshot.getListID());
                    if (Objects.equals(snapshot.getBookID(), bookList.getBookID()) && snapshot.getListID() == bookList.getListID()) {
                        bookListExists = true;
                        break;
                    }
                }
                // If no matching BookList was found, add the new BookList
                if (!bookListExists) {
                    // Use push() to generate a unique key for the new book list
                    DatabaseReference newBookListRef = mData.child("Book List").push();
                    // Set the value of the new book list at the generated key
                    newBookListRef.setValue(bookList)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Book list added successfully
                                    Log.d(bookList.getBookID(), "New book list added!"+ bookList.getListID());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle cancellation
                Log.e("cancel","Error retrieving book lists: " + databaseError.getMessage());
            }
        });
    }
    public interface DeleteCallback {
        void onDeleteSuccess();
        void onDeleteFailure(String error);
    }
}
