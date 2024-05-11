package com.example.androidproject.dao;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.entity.Account;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DAOLoginWithGoogle extends AppCompatActivity {
    private final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();

    public interface FirebaseAuthCallback {
        void onSuccess();

        void onFailure();
    }

    public void firebaseAuth(String idToken, FirebaseAuth auth, List<Integer> idList, List<String> emailList, Context mContext, FirebaseAuthCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        assert user != null;
                        String email = user.getEmail();
                        if (!emailList.contains(email)) {
                            int marking = createMarkId(idList);

                            LocalDate currentDate = LocalDate.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
                            String formattedDate = currentDate.format(formatter);

                            Account account = new Account(marking, email, user.getUid(), 0, 999999, user.getDisplayName(), formattedDate, false);
                            mData.child("Account").child(String.valueOf(marking)).setValue(null);
                            mData.child("Account").child(String.valueOf(marking)).setValue(account);

                            StorePassword(account, mContext);
                        }
                        callback.onSuccess();
                    } else {
                        callback.onFailure();
                    }
                });
    }

    private int createMarkId(List<Integer> idList) {
        int i = 1, marking;
        while (true) {
            if (!idList.contains(i)) {
                marking = i;
                break;
            }
            i += 1;
        }

        return marking;
    }

    private void StorePassword(Account account, Context mContext) {
        AccountDAO accountDAO = new AccountDAO(mContext);

        accountDAO.addAccount(account, false);
    }
}

