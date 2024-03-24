package com.example.androidproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.dao.AccountDAO;
import com.example.androidproject.dao.DAOUpdateInfo;
import com.example.androidproject.entity.Account;

public class UpdatePasswordActivity extends AppCompatActivity {
    private View mainLayout;
    private EditText txtOldPass, txtNewPass, txtConfirmPass;
    private TextView txtEmail;
    private Button btnUpdate;
    private Context mContext;
    private final DAOUpdateInfo daoUpdateInfo = new DAOUpdateInfo();
    private AccountDAO accountDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        mappingComponent();

        setMainLayout();

        getSQLiteData();

        btnUpdate.setOnClickListener(v -> updateInfo());
    }

    private void mappingComponent() {
        mainLayout = findViewById(R.id.mainLayout);

        txtOldPass = findViewById(R.id.editTextOldPassword);
        txtNewPass = findViewById(R.id.editTextNewPassword);
        txtConfirmPass = findViewById(R.id.editTextConfirmPassword);

        txtEmail = findViewById(R.id.textViewEmail);

        btnUpdate = findViewById(R.id.buttonUpdatePassword);

        mContext = this;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setMainLayout() {
        mainLayout.setOnTouchListener((v, event) -> {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                getCurrentFocus().clearFocus();
            }
            return false;
        });
    }

    private void getSQLiteData() {
        accountDAO = new AccountDAO(mContext);
        boolean check = accountDAO.checkExistAccount();
        if (check) {
            Account account = accountDAO.getAccount();
            txtEmail.setText(account.getEmail());
        }
    }

    private void updateInfo() {
        String oldPass = String.valueOf(txtOldPass.getText());
        String newPass = String.valueOf(txtNewPass.getText());
        String confirmPass = String.valueOf(txtConfirmPass.getText());

        accountDAO = new AccountDAO(mContext);
        Account account = accountDAO.getAccount();

        boolean flagCheckOldPass = checkOldPass(oldPass, account);
        if(!flagCheckOldPass) {
            Toast.makeText(mContext, "Mật khẩu cũ không chính xác!", Toast.LENGTH_SHORT).show();
            txtOldPass.requestFocus();
            return;
        }

        boolean flagCheckNewPass = checkNewPass(newPass, confirmPass);
        if(!flagCheckNewPass) {
            Toast.makeText(mContext, "Mật khẩu mới không trùng khớp!", Toast.LENGTH_SHORT).show();
            txtNewPass.requestFocus();
            return;
        }

        boolean flagCheckEmptyNewPass = checkEmptyNewPass(newPass);
        if(flagCheckEmptyNewPass) {
            Toast.makeText(mContext, "Mật khẩu mới không được rỗng!", Toast.LENGTH_SHORT).show();
            txtNewPass.requestFocus();
            return;
        }

        account.setPassword(newPass);

        daoUpdateInfo.UpdateInfo(account.getAccountID(), account);
        accountDAO.addAccount(account, true);

        Toast.makeText(mContext, "Cập nhật mật khẩu mới thành công!", Toast.LENGTH_SHORT).show();
        txtOldPass.setText("");
        txtNewPass.setText("");
        txtConfirmPass.setText("");
    }

    private boolean checkOldPass(String oldPass, Account account) {
        return oldPass.equalsIgnoreCase(account.getPassword());
    }

    private boolean checkNewPass(String newPass1, String newPass2) {
        return newPass1.equalsIgnoreCase(newPass2);
    }

    private boolean checkEmptyNewPass(String pass) {
        return (pass == null || pass.equals(""));
    }

}