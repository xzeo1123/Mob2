package com.example.androidproject.dao;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.androidproject.entity.Book;
import com.example.androidproject.entity.Chapter;
import com.example.androidproject.entity.Volume;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class DAOBook {
    private final DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    private String bookKeyValue;
    public interface GetBookDataListener{
        void onBooksLoaded(ArrayList<Book> books);
        void onError(String errorMessage);
    }
    //Get a book
    public ArrayList<Book> getBook(GetBookDataListener listener){
        ArrayList<Book> books = new ArrayList<>();
        DatabaseReference booksRef = FirebaseDatabase.getInstance().getReference("Book");
        booksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot bookSnapshot : snapshot.getChildren()){
                    Book book = new Book();
                    book.setBookID(bookSnapshot.getKey());
                    book.setPrice(bookSnapshot.child("price").getValue(Double.class));
                    book.setAuthorID(bookSnapshot.child("authorID").getValue(String.class));
                    book.setName(bookSnapshot.child("name").getValue(String.class));
                    book.setUploadDate(bookSnapshot.child("uploadDate").getValue(Long.class));
                    book.setCoverURL(bookSnapshot.child("coverURL").getValue(String.class));
                    books.add(book);
                }
                listener.onBooksLoaded(books);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.getMessage());
            }
        });
        return books;
    }
    //Add a book
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


    //Add new chapter
    public void addChapter(String bookKey, String chapterName, List<String> imageURLs) {
        // Get reference to the "Chapters" node of the specified volume
        DatabaseReference chaptersRef = databaseRef.child("Book").child(bookKey)
                .child("Chapter");

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

    public void uploadImagesAndSaveToDatabase(List<Uri> imageUris, String bookName, String bookId, Chapter chapter) {
        List<String> imageUrls = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(0);

        for (Uri imageUri : imageUris) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                    .child(bookName + "/" + chapter.getName() + "/" + UUID.randomUUID().toString());

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully, get the download URL
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            imageUrls.add(uri.toString());
                            count.incrementAndGet();
                            if (count.get() == imageUris.size()) {
                                // All images uploaded, save chapter to database
                                addChapter(bookId, chapter.getName(), imageUrls);
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle any errors
                        Log.e(TAG, "Error uploading image to Firebase Storage: " + e.getMessage());
                    });
        }
    }



}
