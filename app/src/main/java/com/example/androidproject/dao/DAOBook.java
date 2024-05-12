package com.example.androidproject.dao;

import android.net.Uri;
import android.util.Log;

import com.example.androidproject.entity.Book;
import com.example.androidproject.entity.Chapter;
import com.example.androidproject.entity.Volume;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DAOBook {
    private final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    private String bookKeyValue;

//    public String addBook(Book bookData, File imageFile) {
//        // Get a reference to the Firebase Storage root
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//
//        // Create a reference to the folder with the book's name
//        StorageReference folderRef = storageRef.child(bookData.getName());
//
//        // Create a reference to the image file inside the folder
//        Uri fileUri = Uri.fromFile(imageFile);
//        StorageReference imageRef = folderRef.child(fileUri.getLastPathSegment());
//        imageRef.putFile(fileUri)
//                .addOnSuccessListener(taskSnapshot -> {
//                    // Get the download URL of the uploaded image
//                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                        // Image download URL retrieved successfully
//                        String imageUrl = uri.toString();
//                        // Store this URL in the Firebase Realtime Database
//                        bookData.setCoverURL(imageUrl);
//                        // Generate a unique key for the new book
//                        String bookKey = databaseRef.child("Book").push().getKey();
//                        assert bookKey != null;
//                        databaseRef.child("Book").child(bookKey).setValue(bookData);
//                        bookKeyValue = bookKey;
//                    }).addOnFailureListener(e -> {
//                        // Failed to retrieve download URL
//                        Log.i("ERROR", e.getMessage());
//                    });
//                })
//                .addOnFailureListener(e -> {
//                    // Image upload failed
//                    Log.i("ERROR", e.getMessage());
//                });
//        return bookKeyValue;
//    }
public Task<String> addBook(Book bookData, File imageFile) {
    // Get a reference to the Firebase Storage root
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    // Create a reference to the folder with the book's name
    StorageReference folderRef = storageRef.child(bookData.getName());

    // Create a reference to the image file inside the folder
    Uri fileUri = Uri.fromFile(imageFile);
    StorageReference imageRef = folderRef.child(fileUri.getLastPathSegment());

    // Upload the image to Firebase Storage
    UploadTask uploadTask = imageRef.putFile(fileUri);

    // Return a task that completes when the image upload and book data insertion are both finished
    return uploadTask.continueWithTask(task -> {
        if (!task.isSuccessful()) {
            throw task.getException();
        }

        // Image upload successful, get the download URL
        return imageRef.getDownloadUrl();
    }).continueWithTask(task -> {
        if (task.isSuccessful()) {
            // Image upload and URL retrieval successful
            String imageUrl = task.getResult().toString();
            // Set the image URL in the book data
            bookData.setCoverURL(imageUrl);

            // Generate a unique key for the new book
            String bookKey = databaseRef.child("Book").push().getKey();

            // Set the book under the "Book" node with the generated key
            databaseRef.child("Book").child(bookKey).setValue(bookData);

            return Tasks.forResult(bookKey);
        } else {
            // Image upload or URL retrieval failed
            Log.e("DAOBook", "Error uploading image or retrieving URL", task.getException());
            return null;
        }
    });
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
