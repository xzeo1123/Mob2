package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.dao.DAOConfirmCode;

import java.util.ArrayList;
import java.util.List;

public class ConfirmCodeActivity extends AppCompatActivity {
    private Button btnConfirm;
    private EditText txtEmail, txtCode;
    private Context mContext;
    private final List<Integer> idList = new ArrayList<>();
    private List<String> emailList = new ArrayList<>();
    private final List<Integer> codeList = new ArrayList<>();
    private final DAOConfirmCode daoConfirmCode = new DAOConfirmCode();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_code);

        mappingComponent();

        getParseData();

        getAllData();

        btnConfirm.setOnClickListener(v -> checkCode());
    }

    private void mappingComponent() {
        btnConfirm = findViewById(R.id.buttonConfirm);

        txtEmail = findViewById(R.id.editTextEmail);
        txtCode = findViewById(R.id.editTextCode);

        mContext = this;
    }

    private void getParseData() {
        Intent intent = getIntent();
        if (intent != null) {
            String receivedString;
            try {
                receivedString = intent.getStringExtra("passEmail");
            } catch (NullPointerException e) {
                receivedString = "";
            }
            txtEmail.setText(receivedString);
        }
    }

    private void checkCode() {
        String getEmail = String.valueOf(txtEmail.getText());
        int getCode = Integer.parseInt(String.valueOf(txtCode.getText()));

        boolean flagCheckEmptyEmail = checkEmptyEmail(getEmail);
        if (flagCheckEmptyEmail) {
            Toast.makeText(mContext, "Trường email không được rỗng!", Toast.LENGTH_SHORT).show();
            txtEmail.requestFocus();
            return;
        }

        getAllData();

        int position = emailList.indexOf(getEmail);
        int code = codeList.get(position);

        if(code == getCode) {
            String sid = String.valueOf(idList.get(position));
            daoConfirmCode.updateCode(sid);
            Toast.makeText(mContext, "Xác thực thành công, đang chuyển hướng trang!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ConfirmCodeActivity.this, LoginActivity.class);
            intent.putExtra("passEmail", getEmail);
            startActivity(intent);
        } else if (code == 999999) {
            Toast.makeText(mContext, "Tài khoản này đã kích hoạt rồi!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Mã xác nhận không hợp lệ, hãy kiểm tra lại email!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllData() {
        daoConfirmCode.getIDList(idList -> {
            this.idList.clear();
            for (String id : idList) {
                try {
                    Integer intValue = Integer.parseInt(id);
                    this.idList.add(intValue);
                } catch (NumberFormatException ignored) {
                }
            }
        });

        daoConfirmCode.getEmailList(emailList -> {
            this.emailList.clear();
            this.emailList = emailList;
        });

        daoConfirmCode.getCodeList(codeList -> {
            this.codeList.clear();
            for (String code : codeList) {
                try {
                    Integer intValue = Integer.parseInt(code);
                    this.codeList.add(intValue);
                } catch (NumberFormatException ignored) {
                }
            }
        });
    }

    private boolean checkEmptyEmail(String email) {
        return (email == null || email.equals(""));
    }
}