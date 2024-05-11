package com.example.androidproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidproject.dao.DAOBook;
import com.example.androidproject.entity.Book;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AdminNewMgaActivity extends AppCompatActivity {
    private static ImageButton btnAddImage;
    private ImageButton buttonBack;
    private EditText edtTitle;
    private EditText edtPrice;
    private EditText edtAuthor;
    private EditText edtCategory;
    private static File currentCover;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_new_mga);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminHomeActivity.class);
            startActivity(intent);
        });
        btnAddImage = findViewById(R.id.btnAddImage);
        btnAddImage.setOnClickListener(view -> AddImage());

        Button btnAddBook = findViewById(R.id.btnAddBook);
        btnAddBook.setOnClickListener(v -> {
            edtTitle = findViewById(R.id.edtTitle);
            edtPrice = findViewById(R.id.edtPrice);
            edtAuthor = findViewById(R.id.edtAuthor);
            edtCategory = findViewById(R.id.edtCategory);
            Book bookData = new Book();
            bookData.setName(edtTitle.getText().toString());
            bookData.setPrice(Double.parseDouble(edtPrice.getText().toString()));
            bookData.setAuthorID(edtAuthor.getText().toString());
            bookData.setUploadDate(System.currentTimeMillis());
            InsertNewBook(bookData);
        });

    }
    public void InsertNewBook(Book bookData) {
        try {
//            new DAOBook().addBook(bookData, currentCover)
//                    .addOnSuccessListener(bookKey -> {
//                        new DAOBook().addChapter(bookKey, "", new ArrayList<>());
//                    });
            new DAOBook().addBook(bookData, currentCover);
            Toast.makeText(this, "Created new Book successfully!", Toast.LENGTH_SHORT).show();
            //Add back to home page or redirect to add chapters in manga activity
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handleActivityResult(AdminNewMgaActivity.this, requestCode, resultCode, data);
    }




    //Add cover image locally and display it to ImageButton
    public void AddImage(){
        pickPhoto(AdminNewMgaActivity.this);
    }

    public static void takePhoto(Activity activity) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public static void pickPhoto(Activity activity) {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
    }

    public static void handleActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            currentCover = saveImageLocally(activity, imageBitmap);
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImageUri);
                currentCover = saveImageLocally(activity, imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(activity, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static File saveImageLocally(Activity activity, Bitmap imageBitmap) {
        String imageName = "image_" + System.currentTimeMillis() + ".jpg";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, imageName);

        try (OutputStream fos = new FileOutputStream(imageFile)) {
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Toast.makeText(activity, "Image saved locally", Toast.LENGTH_SHORT).show();
            displaySavedImage(activity, imageFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Error saving image", Toast.LENGTH_SHORT).show();
        }
        return imageFile;
    }
    private static void displaySavedImage(Activity activity, String imagePath) {
        // Load the saved image from file path
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        // Find the ImageView in your activity layout
        btnAddImage = activity.findViewById(R.id.btnAddImage);

        // Display the image in the ImageView
        if (bitmap != null) {
            btnAddImage.setImageBitmap(bitmap);
        } else {
            Toast.makeText(activity, "Error loading image", Toast.LENGTH_SHORT).show();
        }
    }
    //End add cover image locally and display it to ImageButton



}