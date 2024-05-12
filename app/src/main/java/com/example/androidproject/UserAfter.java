package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidproject.dao.AccountDAO;

public class UserAfter extends AppCompatActivity {
    private TextView txtEditProfile, txtChangePass, txtUserName;
    private Context mContext;

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

        renderData();

        txtEditProfile.setOnClickListener(v -> goToUpdateInfo());

        txtChangePass.setOnClickListener(v -> goToChangePass());

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            backToHome();
        });
    }
    public void backToHome(){
        Intent intent = new Intent(UserAfter.this, UserHomeActivity.class);
        startActivity(intent);
    }
    private void mappingComponents() {
        txtEditProfile = findViewById(R.id.textViewEditProfile);
        txtChangePass = findViewById(R.id.textViewChangePassword);
        txtUserName = findViewById(R.id.textViewUserName);
        mContext = this;
    }

    private void goToUpdateInfo() {
        Intent intent = new Intent(UserAfter.this, UserUpdateInfoActivity.class);
        startActivity(intent);
    }

    private void goToChangePass() {
        Intent intent = new Intent(UserAfter.this, UserChangePassActivity.class);
        startActivity(intent);
    }

    private void renderData() {
        AccountDAO accountDAO = new AccountDAO(mContext);
        boolean check = accountDAO.checkExistAccount();
        if (check) {
            txtUserName.setText(accountDAO.getFullName());
        }
    }
}