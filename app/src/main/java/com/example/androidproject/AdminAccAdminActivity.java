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

import com.example.androidproject.IRecycleView.IRecycleViewAdminAcc;
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

public class AdminAccAdminActivity extends AppCompatActivity implements IRecycleViewAdminAcc {
    public ArrayList<AdminAccount> adminAccountArrayList = new ArrayList<>();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("AdmAccount");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_acc_admin);

        GetAdminAccounts();

        RecyclerView adminAccRecyclerView = findViewById(R.id.adminAccRecyclerView);


        AAdmin_RecyclerViewAdapter adapter = new AAdmin_RecyclerViewAdapter(this, adminAccountArrayList, this);
        adminAccRecyclerView.setAdapter(adapter);
        adminAccRecyclerView.setLayoutManager(new LinearLayoutManager((this)));
    }

    private void GetAdminAccounts(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AdminAccount adminAccount = new AdminAccount();
                    adminAccount.AccountID = snapshot.child("AccountID").getValue(Integer.class);;
                    adminAccount.Email = snapshot.child("Email").getValue(String.class);;
                    adminAccount.Password = snapshot.child("Password").getValue(String.class);;
                    adminAccount.DisplayName = snapshot.child("DisplayName").getValue(String.class);;
                    adminAccount.DoB = snapshot.child("DoB").getValue(String.class);;
                    adminAccount.Role = snapshot.child("Role").getValue(String.class);;


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
        AAdmin_RecyclerViewAdapter adapter = new AAdmin_RecyclerViewAdapter(this, adminAccountArrayList, this);
        adminAccRecyclerView.setAdapter(adapter);
        adminAccRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        @Override
    public void onClickResetPassword(int position) {
        AdminAccount adminAccount = adminAccountArrayList.get(position);
        //Check this part is null or not
        Intent intent = new Intent(AdminAccAdminActivity.this, AdminAAccDetail.class);
        intent.putExtra("adminAccount", adminAccount);
        startActivity(intent);
        Log.i("Dataaaaaaaaa", adminAccount.DisplayName + " " + adminAccount.Password + " " + adminAccount.Email + " " + adminAccount.DoB + " " + adminAccount.Role + " " + adminAccount.AccountID);
    }
}