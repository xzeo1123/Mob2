package com.example.androidproject.dao;

import android.util.Log;

import com.example.androidproject.entity.Book;
import com.example.androidproject.entity.Chapter;
import com.example.androidproject.entity.Volume;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DAOBook {
    private final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();;

    public String addBook(Book bookData) {
        // Generate a unique key for the new book
        String bookKey = databaseRef.child("Book").push().getKey();

//        bookData.setAuthorID(bookData.getAuthorID());
//        bookData.setName(bookData);
//        bookData.setPrice(price);
//        bookData.setUploadDate(uploadDate);
//        bookData.setUploadDate(System.currentTimeMillis());

        // Set the book under the "books" node with the generated key
        assert bookKey != null;
        databaseRef.child("Book").child(bookKey).setValue(bookData);
        return bookKey;
    }

    public String addVolume(String bookKey, String name) {
        String volumeKey = null;
        try {
            // Generate a unique key for the new volume
            volumeKey = databaseRef.child("Book").child(bookKey).child("Volume").push().getKey();

            // Create a new Volume object
            Volume volume = new Volume();
            volume.setName(name);
            // Set the volume under the "Volume" node of the specified book with the generated key
            databaseRef.child("Book").child(bookKey).child("Volume").child(volumeKey).setValue(volume);
        } catch (Exception e) {
            Log.i("ERROR", e.getMessage());
        }
        return volumeKey;
    }

    public void addChapter(String bookKey, String volumeKey, String chapterName, List<String> imageURLs) {
        // Get reference to the "Chapters" node of the specified volume
        DatabaseReference chaptersRef = databaseRef.child("Book").child(bookKey)
                .child("Volume").child(volumeKey);

        // Generate a unique key for the new chapter
        String chapterKey = chaptersRef.push().getKey();

        // Create a new Chapter object
        Chapter chapter = new Chapter();
        chapter.setName(chapterName);
        chapter.setImages(imageURLs);
        chapter.setUploadDate(System.currentTimeMillis());
        // Set the chapter under the "Chapters" node with the generated key
        chaptersRef.child(chapterKey).setValue(chapter);
    }

    // Other methods for retrieving data, updating data, etc.
}
