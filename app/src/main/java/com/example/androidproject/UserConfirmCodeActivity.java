package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidproject.dao.DAOConfirmCode;

import java.util.ArrayList;
import java.util.List;

public class UserConfirmCodeActivity extends AppCompatActivity {
    private Button btnConfirm;
    private ImageButton btnBack;
    private EditText txtEmail, txtCode;
    private Context mContext;
    private final List<Integer> idList = new ArrayList<>();
    private List<String> emailList = new ArrayList<>();
    private final List<Integer> codeList = new ArrayList<>();
    private final DAOConfirmCode daoConfirmCode = new DAOConfirmCode();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_comfirm_code);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mappingComponent();

        getParseData();

        getAllData();

        btnConfirm.setOnClickListener(v -> checkCode());

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(UserConfirmCodeActivity.this, UserLoginActivity.class);
            startActivity(intent);
        });
    }

    private void mappingComponent() {
        btnConfirm = findViewById(R.id.buttonConfirm);
        btnBack = findViewById(R.id.buttonBack);

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

            Intent intent = new Intent(UserConfirmCodeActivity.this, UserLoginActivity.class);
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