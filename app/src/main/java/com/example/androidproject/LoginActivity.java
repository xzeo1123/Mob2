package com.example.androidproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.dao.AccountDAO;
import com.example.androidproject.dao.DAOLogin;
import com.example.androidproject.entity.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private View mainLayout;
    private EditText txtEmail, txtPassword;
    private Button btnLogin, btnSignup, btnConfirm, btnForget;
    private CheckBox cbStorePassword;
    private Context mContext;
    private final List<Integer> idList = new ArrayList<>();
    private List<String> emailList = new ArrayList<>();
    private List<String> passwordList = new ArrayList<>();
    private final List<Integer> codeList = new ArrayList<>();
    private final DAOLogin daoLogin = new DAOLogin();
    private AccountDAO accountDAO;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mappingComponent();

        setupComponent();

        setMainLayout();

        getParseData();

        getSQLiteData();

        getAllData();

        btnLogin.setOnClickListener(v -> checkLogin());

        btnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        btnConfirm.setOnClickListener(v -> {
            String email;
            try {
                email = String.valueOf(txtEmail.getText());
            } catch (NullPointerException e) {
                email = "";
            }
            Intent intent = new Intent(LoginActivity.this, ConfirmCodeActivity.class);
            intent.putExtra("passEmail", email);
            startActivity(intent);
        });

        btnForget.setOnClickListener(v -> {
            String email;
            try {
                email = String.valueOf(txtEmail.getText());
            } catch (NullPointerException e) {
                email = "";
            }
            Intent intent = new Intent(LoginActivity.this, RestorePasswordActivity.class);
            intent.putExtra("passEmail", email);
            startActivity(intent);
        });
    }

    private void mappingComponent() {
        mainLayout = findViewById(R.id.mainLayout);

        txtEmail = findViewById(R.id.editTextEmail);
        txtPassword = findViewById(R.id.editTextPassword);

        btnLogin = findViewById(R.id.buttonLogin);
        btnSignup = findViewById(R.id.buttonSignup);
        btnConfirm = findViewById(R.id.buttonConfirm);
        btnForget = findViewById(R.id.buttonForget);

        cbStorePassword = findViewById(R.id.checkBoxStorePassword);

        progressBar = findViewById(R.id.progressBar);

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

    private void setupComponent() {
        cbStorePassword.setChecked(true);
    }

    private void getParseData() {
        Intent intent = getIntent();
        if (intent != null) {
            String receivedString = intent.getStringExtra("passEmail");
            txtEmail.setText(receivedString);
        }
    }

    private void getSQLiteData() {
        accountDAO = new AccountDAO(mContext);
        boolean check = accountDAO.checkExistAccount();
        if (check) {
            Account account = accountDAO.getAccount();
            txtEmail.setText(account.getEmail());
            txtPassword.setText(account.getPassword());
        }
    }

    private void getAllData() {
        daoLogin.getIDList(idList -> {
            this.idList.clear();
            for (String id : idList) {
                try {
                    Integer intValue = Integer.parseInt(id);
                    this.idList.add(intValue);
                } catch (NumberFormatException ignored) {
                }
            }
        });

        daoLogin.getEmailList(emailList -> {
            this.emailList.clear();
            this.emailList = emailList;
        });

        daoLogin.getPasswordList(passwordList -> {
            this.passwordList.clear();
            this.passwordList = passwordList;
        });

        daoLogin.getCodeList(codeList -> {
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

    private void checkLogin() {
        String getEmail = String.valueOf(txtEmail.getText());
        String getPassword = String.valueOf(txtPassword.getText());

        boolean flagCheckEmptyEmail = checkEmptyEmail(getEmail);
        if (flagCheckEmptyEmail) {
            Toast.makeText(mContext, "Trường email không được rỗng!", Toast.LENGTH_SHORT).show();
            txtEmail.requestFocus();
            return;
        }

        boolean flagCheckEmptyPass = checkEmptyPass(getPassword);
        if (flagCheckEmptyPass) {
            Toast.makeText(mContext, "Trường mật khẩu không được rỗng!", Toast.LENGTH_SHORT).show();
            txtPassword.requestFocus();
            return;
        }

        boolean flagIsValidEmail = isValidEmail(getEmail);
        if (!flagIsValidEmail) {
            Toast.makeText(mContext, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
            txtEmail.requestFocus();
            return;
        }

        getAllData();

        boolean flagCheckEmailAvailable = emailList.contains(getEmail);
        if (!flagCheckEmailAvailable) {
            Toast.makeText(mContext, "Email này chưa được đăng ký!", Toast.LENGTH_SHORT).show();
            txtEmail.requestFocus();
            return;
        }

        int position = emailList.indexOf(getEmail);
        String storedPassword = String.valueOf((passwordList.get(position)));
        if (!getPassword.equals(storedPassword)) {
            Toast.makeText(mContext, "Sai tên đăng nhập hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
            btnForget.requestFocus();
            return;
        }

        int storedCode = codeList.get(position);
        if (storedCode != 999999) {
            Toast.makeText(mContext, "Tài khoản này chưa kích hoạt mã xác thực!", Toast.LENGTH_SHORT).show();
            btnConfirm.requestFocus();
            return;
        }

        StorePassword(position);

        Toast.makeText(mContext, "Đăng nhập thành công. Đang chuyển đến trang chủ!", Toast.LENGTH_SHORT).show();

        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.GONE);

            Intent intent = new Intent(LoginActivity.this, HomePage.class);
            startActivity(intent);

            finish();
        }, 2000);
    }

    private void StorePassword(int position) {
        int id = idList.get(position);
        daoLogin.getAccountById(id, account -> {
            if (account != null) {
                accountDAO = new AccountDAO(mContext);
                accountDAO.addAccount(account, cbStorePassword.isChecked());
            }
        });
    }

    private boolean checkEmptyEmail(String email) {
        return (email == null || email.equals(""));
    }

    private boolean checkEmptyPass(String pass) {
        return (pass == null || pass.equals(""));
    }

    public static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
