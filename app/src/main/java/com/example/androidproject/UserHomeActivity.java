package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserHomeActivity extends AppCompatActivity {

    ImageButton btnUser;
    ImageButton btnPlayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mappingComponents();

        btnUser.setOnClickListener(v -> goToUserInfo());
        btnPlayList.setOnClickListener(v -> goToPlayList());
    }

    private void mappingComponents() {
        btnUser = findViewById(R.id.imageUser);
        btnPlayList = findViewById(R.id.imagePlaylist);
    }

    private void goToUserInfo() {
        Intent intent = new Intent(UserHomeActivity.this, UserAfter.class);
        startActivity(intent);
    }
    private void goToPlayList() {
        Intent intent = new Intent(UserHomeActivity.this, BCListActivity.class);
        startActivity(intent);
    }
}