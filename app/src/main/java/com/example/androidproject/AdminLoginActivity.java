package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        Button btn = findViewById(R.id.buttonLogin);
        btn.setOnClickListener(v -> {
            EditText email = findViewById(R.id.email);
            EditText password = findViewById(R.id.password);

            Intent intent = new Intent(AdminLoginActivity.this, AdminHomeActivity.class);
            intent.putExtra("userName", email.getText().toString());
            startActivity(intent);
        });
    }
}