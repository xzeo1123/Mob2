package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidproject.entity.AdminAccount;

public class AdminAAccDetail extends AppCompatActivity {
    private TextView txtAccountId;
    private TextView txtDisplayName;
    private TextView txtEmail;
    private TextView txtDOB;
    private TextView txtRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_aacc_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        AdminAccount adminAccount = (AdminAccount) intent.getSerializableExtra("adminAccount");
        txtAccountId = findViewById(R.id.txtaccountid);
        txtDisplayName = findViewById(R.id.txtdisplayname);
        txtEmail = findViewById(R.id.txtemail);
        txtDOB = findViewById(R.id.txtdob);
        txtRole = findViewById(R.id.txtrole);

        assert adminAccount != null;
        txtAccountId.setText(adminAccount.getAccountID());
        txtDisplayName.setText(adminAccount.getDisplayName());
        txtEmail.setText(adminAccount.getEmail());
        txtDOB.setText(adminAccount.getDoB());
        txtRole.setText(adminAccount.getRole());




    }
}