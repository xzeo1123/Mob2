package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserAfter extends AppCompatActivity {
    TextView txtEditProfile, txtChangePass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_after);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mappingComponents();

        txtEditProfile.setOnClickListener(v -> goToUpdateInfo());

        txtChangePass.setOnClickListener(v -> goToChangePass());
    }

    private void mappingComponents() {
        txtEditProfile = findViewById(R.id.textViewEditProfile);
        txtChangePass = findViewById(R.id.textViewChangePassword);
    }

    private void goToUpdateInfo() {
        Intent intent = new Intent(UserAfter.this, UserUpdateInfoActivity.class);
        startActivity(intent);
    }

    private void goToChangePass() {
        Intent intent = new Intent(UserAfter.this, UserChangePassActivity.class);
        startActivity(intent);
    }
}