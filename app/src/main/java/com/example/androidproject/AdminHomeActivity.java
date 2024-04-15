package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //      Button to Admins
        Button btnToAdmin = findViewById(R.id.btnToAdmin);
        btnToAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, AdminAccAdminActivity.class);
            startActivity(intent);
        });
        //      Button to Users
        Button btnToUser = findViewById(R.id.btnToUser);
        btnToUser.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, AdminAccUserActivity.class);
            startActivity(intent);
        });
        //      Button to Add new manga
        Button btnNewManga = findViewById(R.id.btnNewManga);
        btnNewManga.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, AdminNewMgaActivity.class);
            startActivity(intent);
        });
        //      Button to Posted Manga
        Button btnPostedManga = findViewById(R.id.btnPostedManga);
        btnPostedManga.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, AdminPostedActivity.class);
            startActivity(intent);
        });
        //      Button to Change Password
        Button btnChangePassword = findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, AdminAccountInfo.class);
            startActivity(intent);
        });
        //      Logout Button
        Button btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(v -> {

        });
    }
}