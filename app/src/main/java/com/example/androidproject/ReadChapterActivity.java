package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.androidproject.entity.Chapter;

public class ReadChapterActivity extends AppCompatActivity {
    private String BookId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_read_chapter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        LinearLayout container = findViewById(R.id.container);
        Chapter chapter = (Chapter) intent.getSerializableExtra("chapter");
        BookId = intent.getStringExtra("BookId");
        ImageButton btnBackToChapters = findViewById(R.id.btnBackToChapters);
        btnBackToChapters.setOnClickListener(v -> {
            backToChapters();
        });

        // Add ImageViews dynamically to the LinearLayout
        for (String url : chapter.getImages()) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(layoutParams);

            // Load image from URL using Glide or any other image loading library
            Glide.with(this)
                    .load(url)
                    .into(imageView);

            container.addView(imageView);
        }
    }
    public void backToChapters(){
        Intent intent = new Intent(ReadChapterActivity.this, ChapterActivity.class);
        intent.putExtra("BookId", BookId);
        startActivity(intent);
    }
}