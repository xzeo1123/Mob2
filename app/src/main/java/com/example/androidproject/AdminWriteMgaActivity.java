package com.example.androidproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Toast;

import com.example.androidproject.dao.DAOBook;
import com.example.androidproject.entity.Book;
import com.example.androidproject.entity.Chapter;

import java.util.ArrayList;
import java.util.List;

public class AdminWriteMgaActivity extends AppCompatActivity {

    private static final int PICK_IMAGES_REQUEST_CODE = 100;
    private List<Uri> imageUris = new ArrayList<>();
    private List<String> imageNames = new ArrayList<>();
    private Chapter chapter = new Chapter();;
    private EditText txtChapterName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_write_mga);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        Intent intent = getIntent();
        Book book = (Book) intent.getSerializableExtra("BookInfo");
        assert book != null;
        txtChapterName = findViewById(R.id.txtChapterName);
        Button btnPostChapter = findViewById(R.id.btnPostChapter);
        btnPostChapter.setOnClickListener(v -> {

            chapter.setName(txtChapterName.getText().toString());
            chapter.setUploadDate(System.currentTimeMillis());
            uploadNewChapter(imageUris, book.getName(), book.getBookID(), chapter);
        });
        Button btnPickChapters = findViewById(R.id.btnPickChapters);
        btnPickChapters.setOnClickListener(view -> selectImage(view));
        TextView txtDisplayNames = findViewById(R.id.txtDisplayNames);

        txtDisplayNames.setText("");

        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            Intent intentBack = new Intent(this, AdminBookDetail.class);
            startActivity(intentBack);
        });


    }
    public void uploadNewChapter(List<Uri> imageUris, String bookName, String bookId, Chapter chapter){
        new DAOBook().uploadImagesAndSaveToDatabase(imageUris, bookName, bookId, chapter);
        Toast.makeText(AdminWriteMgaActivity.this, "Upload new chapter successfully!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, AdminPostedActivity.class);
        startActivity(intent);
    }
    private void updateImageNamesTextView() {
        TextView txtDisplayNames = findViewById(R.id.txtDisplayNames);
        StringBuilder stringBuilder = new StringBuilder();
        for (String name : imageNames) {
            stringBuilder.append(name).append("\n"); // Add each image name followed by a new line
        }
        txtDisplayNames.setText(stringBuilder.toString());
    }
    public void selectImage(View view) {
        // Create an intent to pick images from the device
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Enable multiple image selection
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGES_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            if (data.getClipData() != null) {
                // Multiple images are selected
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imageUris.add(imageUri);
                    // Get the file name from the URI
                    String imageName = getFileNameFromUri(imageUri) ;
                    imageNames.add(imageName);
                }
            } else if (data.getData() != null) {
                // Single image is selected
                Uri imageUri = data.getData();
                imageUris.add(imageUri);
                // Get the file name from the URI
                String imageName = getFileNameFromUri(imageUri);
                imageNames.add(imageName);
            }

            updateImageNamesTextView();
        }
    }

    // Method to get file name from URI
    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (displayNameIndex != -1) {
                fileName = cursor.getString(displayNameIndex);
            }
            cursor.close();
        }
        return fileName;
    }
}