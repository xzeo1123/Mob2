package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginActivity extends AppCompatActivity {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("AdmAccount");
    private EditText _email, _password;
    private Button _btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        _btnLogin = findViewById(R.id.buttonLogin);
        _btnLogin.setOnClickListener(v -> {
            _email = findViewById(R.id.email);
            _password = findViewById(R.id.password);
            LoginValidation(_email.getText().toString(), _password.getText().toString());
        });
    }

    private void LoginValidation(String providedEmail, String providedPassword){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean userExists = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String email = snapshot.child("Email").getValue(String.class);
                    String password = snapshot.child("Password").getValue(String.class);
                    String displayName = snapshot.child("DisplayName").getValue(String.class) != null ?
                            snapshot.child("DisplayName").getValue(String.class) :
                            "User";
                    Log.i("Data", email + " " + password + " " + providedEmail);
                    // Check if the email exists
                    if (email != null && email.equals(providedEmail)) {
                        userExists = true;
                        // Check if the password matches
                        if (password != null && password.equals(providedPassword)) {
                            Intent intent = new Intent(AdminLoginActivity.this, AdminHomeActivity.class);
                            intent.putExtra("userName", displayName);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AdminLoginActivity.this, "Account or Password is incorrect!", Toast.LENGTH_SHORT).show();
                        }
                        break; // No need to continue looping
                    }
                }

                if (!userExists) {
                    Toast.makeText(AdminLoginActivity.this, "Account or Password is incorrect!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

    }
}