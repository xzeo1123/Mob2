package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.androidproject.dao.AccountDAO;
import com.example.androidproject.entity.Account;

public class HomePage extends AppCompatActivity {
    AccountDAO accountDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        accountDAO = new AccountDAO(this);

        Account ac;
        ac = accountDAO.getAccount();
        ac = accountDAO.getAccount();
        try {
            Log.i("Getted Account", ac.getEmail());
        } catch (NullPointerException e) {
            Log.i("Getted Account", "Account get bi null");
        }


        Button btn = findViewById(R.id.buttonLogout);
        btn.setOnClickListener(v -> {
            boolean storeState = accountDAO.getStoreState();
            if(!storeState) {
                accountDAO.delAccount();
            }
            Intent intent = new Intent(HomePage.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}