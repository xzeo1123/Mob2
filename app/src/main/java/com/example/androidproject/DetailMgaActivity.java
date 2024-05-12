package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.androidproject.dao.DAODetailManga;

public class DetailMgaActivity extends AppCompatActivity {
    private TextView txtName, txtAuthor, txtCategory, txtPrice, txtDescription;
    private Button btnRead, btnAdd, btnComment, btnChapter;
    private ImageView imgView;
    private Context mContext;
    private DAODetailManga daoDetailManga = new DAODetailManga();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_mga);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mappingComponent();

        RenderData();

        btnRead.setOnClickListener(v -> goToChapter());

        btnAdd.setOnClickListener(v -> addFavor());

        btnComment.setOnClickListener(v -> goToComment());

        btnChapter.setOnClickListener(v -> goToChapter());
    }

    private void mappingComponent() {
        txtName = findViewById(R.id.txtName);
        txtAuthor = findViewById(R.id.txtAuthor);
        txtCategory = findViewById(R.id.txtCategory);
        txtPrice = findViewById(R.id.txtPrice);
        txtDescription = findViewById(R.id.txtDescription);

        btnRead = findViewById(R.id.buttonRead);
        btnAdd = findViewById(R.id.buttonAddToFavor);
        btnComment = findViewById(R.id.buttonComment);
        btnChapter = findViewById(R.id.buttonChapter);

        imgView = findViewById(R.id.imgViewCover);

        mContext = this;
    }

    private void RenderData() {
        String bookId = getParseData();

        daoDetailManga.getBookById(bookId, detailBook -> {
            if (detailBook != null) {
                txtName.setText(detailBook.getName());

                txtAuthor.setText(detailBook.getName());

                txtCategory.setText(detailBook.getName());

                double getPrice = detailBook.getPrice();
                txtPrice.setText(String.valueOf(getPrice));

                txtDescription.setText(detailBook.getName());

                String imageUrl = detailBook.getCoverURL();
                Glide.with(this)
                        .load(imageUrl)
                        .apply(new RequestOptions())
                        .into(imgView);
            }
        });
    }

    private String getParseData() {
//        try {
//            Intent intent = getIntent();
//            if (intent != null) {
//                return intent.getStringExtra("BookId");
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
        return "-NxSlK3ynwldt7Wp3ihX";
    }

    private void goToChapter() {
        Intent intent = new Intent(DetailMgaActivity.this, ChapterActivity.class);
        intent.putExtra("BookId", getParseData());
        startActivity(intent);
    }

    private void addFavor() {

    }

    private void goToComment() {
        Intent intent = new Intent(DetailMgaActivity.this, WriteCmtActivity.class);
        intent.putExtra("BookId", getParseData());
        startActivity(intent);
    }
}