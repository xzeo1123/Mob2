package com.example.androidproject;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidproject.dao.AccountDAO;
import com.example.androidproject.dao.DAOUpdateInfo;
import com.example.androidproject.entity.Account;

public class UserChangePassActivity extends AppCompatActivity {
    private EditText txtOldPass, txtNewPass, txtConfirmPass;
    private Button btnUpdate;
    private Context mContext;
    private final DAOUpdateInfo daoUpdateInfo = new DAOUpdateInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_change_pass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mappingComponent();

        btnUpdate.setOnClickListener(v -> updateInfo());
    }

    private void mappingComponent() {
        txtOldPass = findViewById(R.id.editTextOldPassword);
        txtNewPass = findViewById(R.id.editTextNewPassword);
        txtConfirmPass = findViewById(R.id.editTextConfirmPassword);

        btnUpdate = findViewById(R.id.buttonUpdatePassword);

        mContext = this;
    }

    private void updateInfo() {
        String oldPass = String.valueOf(txtOldPass.getText());
        String newPass = String.valueOf(txtNewPass.getText());
        String confirmPass = String.valueOf(txtConfirmPass.getText());

        AccountDAO accountDAO = new AccountDAO(mContext);
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