package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.dao.AccountDAO;

public class HomePage extends AppCompatActivity {
    AccountDAO accountDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        accountDAO = new AccountDAO(this);

        Button btn1 = findViewById(R.id.buttonLogout);
        btn1.setOnClickListener(v -> {
            boolean storeState = accountDAO.getStoreState();
            if(!storeState) {
                accountDAO.delAccount();
            }
            Intent intent = new Intent(HomePage.this, LoginActivity.class);
            startActivity(intent);
        });

        Button btn2 = findViewById(R.id.buttonUpdateInfo);
        btn2.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, UpdatePasswordActivity.class);
            startActivity(intent);
        });
    }
}