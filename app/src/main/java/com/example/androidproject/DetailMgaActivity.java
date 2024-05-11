package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailMgaActivity extends AppCompatActivity {
    private TextView txtName, txtAuthor, txtCategory, txtPrice, txtDescription;
    private Button btnRead, btnAdd, btnComment;
    private Context mContext;
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

        btnRead.setOnClickListener(v -> goToReadBook());

        btnAdd.setOnClickListener(v -> addFavor());

        btnComment.setOnClickListener(v -> goToComment());
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

        mContext = this;
    }

    private void RenderData() {

    }

    private int getParseData() {
        Intent intent = getIntent();
        if (intent != null) {
            return intent.getIntExtra("BookId", 0);
        }
        return 0;
    }

    private void goToReadBook() {
//        Intent intent = new Intent(DetailMgaActivity.this, ???.class);
//        intent.putExtra("BookId", getParseData());
//        startActivity(intent);
    }

    private void addFavor() {

    }

    private void goToComment() {
        Intent intent = new Intent(DetailMgaActivity.this, WriteCmtActivity.class);
        intent.putExtra("BookId", getParseData());
        startActivity(intent);
    }
}