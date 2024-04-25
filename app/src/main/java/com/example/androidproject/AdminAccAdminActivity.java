package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidproject.entity.AdminAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminAccAdminActivity extends AppCompatActivity {
    public ArrayList<AdminAccount> adminAccountArrayList = new ArrayList<>();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("AdmAccount");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_acc_admin);

        GetAdminAccounts();

        RecyclerView adminAccRecyclerView = findViewById(R.id.adminAccRecyclerView);


        AAdmin_RecyclerViewAdapter adapter = new AAdmin_RecyclerViewAdapter(this, adminAccountArrayList);
        adminAccRecyclerView.setAdapter(adapter);
        adminAccRecyclerView.setLayoutManager(new LinearLayoutManager((this)));
    }

    private void GetAdminAccounts(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Integer accountID = snapshot.child("AccountID").getValue(Integer.class);
                    String email = snapshot.child("Email").getValue(String.class);
                    String password = snapshot.child("Password").getValue(String.class);
                    String displayName = snapshot.child("DisplayName").getValue(String.class);
                    String doB = snapshot.child("DoB").getValue(String.class);
                    String role = snapshot.child("Role").getValue(String.class);
                    Log.i("TEstttttttttttttt", accountID + " " + email + " " + password + " " + displayName + " " + doB + " " + role);
                    AdminAccount adminAccount = new AdminAccount();
                    adminAccount.AccountID = accountID;
                    adminAccount.Email = email;
                    adminAccount.Password = password;
                    adminAccount.DisplayName = displayName;
                    adminAccount.DoB = doB;
                    adminAccount.Role = role;


                    adminAccountArrayList.add(adminAccount);
                }
                updateRecyclerView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
    private void updateRecyclerView() {
        RecyclerView adminAccRecyclerView = findViewById(R.id.adminAccRecyclerView);
        AAdmin_RecyclerViewAdapter adapter = new AAdmin_RecyclerViewAdapter(this, adminAccountArrayList);
        adminAccRecyclerView.setAdapter(adapter);
        adminAccRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}