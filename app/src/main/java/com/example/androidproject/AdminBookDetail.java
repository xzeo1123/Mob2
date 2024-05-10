package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidproject.entity.Book;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminBookDetail extends AppCompatActivity {
    private Book book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_book_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra("singleBook");

        TextView txtBookTitle, txtBookAuthor, txtBookPostedDate, txtBookPrice, txtLastUpdated;
        ImageView imgBookCover;
        imgBookCover = findViewById(R.id.imgBookCover);
        txtBookTitle = findViewById(R.id.txtBookTitle);
        txtBookAuthor = findViewById(R.id.txtBookAuthor);
        txtBookPostedDate = findViewById(R.id.txtBookPostedDate);
        txtBookPrice = findViewById(R.id.txtBookPrice);
        txtLastUpdated = findViewById(R.id.txtLastUpdated);

        assert book != null;
        Picasso.get().load(book.getCoverURL()).into(imgBookCover);
        txtBookTitle.setText(book.getName());
        txtBookAuthor.setText(book.getAuthorID());
        txtBookPostedDate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                .format(new Date(book.getUploadDate())));
        txtBookPrice.setText(String.valueOf(book.getPrice()));
        txtLastUpdated.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                .format(new Date(System.currentTimeMillis())));


        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent backToPosted = new Intent(this, AdminPostedActivity.class);
            startActivity(backToPosted);
        });
        Button btnAddChapter = findViewById(R.id.btnAddChapter);
        btnAddChapter.setOnClickListener(view -> {
            Intent newMangaIntent = new Intent(this, AdminWriteMgaActivity.class);
            newMangaIntent.putExtra("BookID", book.getBookID());
            startActivity(newMangaIntent);
        });
    }
}