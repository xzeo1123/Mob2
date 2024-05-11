package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidproject.dao.AccountDAO;
import com.example.androidproject.dao.DAOLogin;
import com.example.androidproject.dao.DAOLoginWithGoogle;
import com.example.androidproject.entity.Account;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserLoginActivity extends AppCompatActivity {
    private EditText etxtEmail, etxtPassword;
    private Button btnLogin;
    private ImageButton btnGoogleLogin;
    private TextView txtSignup, txtForgotPass, txtConfirm;
    private CheckBox cbStorePassword;
    private Context mContext;
    private final List<Integer> idList = new ArrayList<>();
    private List<String> emailList = new ArrayList<>();
    private List<String> passwordList = new ArrayList<>();
    private final List<Integer> codeList = new ArrayList<>();
    private final DAOLogin daoLogin = new DAOLogin();
    private AccountDAO accountDAO;
    private FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;
    private final DAOLoginWithGoogle daoLoginWithGoogle = new DAOLoginWithGoogle();
    private final int RC_SIGN_IN = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mappingComponent();

        setupComponent();

        getParseData();

        getSQLiteData();

        getAllData();

        setupLoginGoogle();

        btnLogin.setOnClickListener(v -> checkLogin());

        txtSignup.setOnClickListener(v -> goToSignup());

        txtConfirm.setOnClickListener(v -> goToConfirmCode());

        txtForgotPass.setOnClickListener(v -> goToRestorePass());

        btnGoogleLogin.setOnClickListener(v -> {
            signIn();
            getAllData();
        });
    }

    private void mappingComponent() {
        etxtEmail = findViewById(R.id.editTextEmail);
        etxtPassword = findViewById(R.id.editTextPassword);

        btnLogin = findViewById(R.id.buttonLogin);
        btnGoogleLogin = findViewById(R.id.buttonGoogleLogin);

        txtSignup = findViewById(R.id.textViewSignup);
        txtConfirm = findViewById(R.id.textViewConfirmCode);
        txtForgotPass = findViewById(R.id.textViewForgotPass);

        cbStorePassword = findViewById(R.id.checkBoxStorePassword);

        mContext = this;

        auth = FirebaseAuth.getInstance();
    }

    private void setupLoginGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("153641149690-h6pri5gqsipshbopkivt9egerisk0ocp.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            } catch (ApiException ignored) {
            }
        }
    }

    private void firebaseAuth(String idToken) {
        new Handler().postDelayed(() -> daoLoginWithGoogle.firebaseAuth(idToken, auth, idList, emailList, mContext, new DAOLoginWithGoogle.FirebaseAuthCallback() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(UserLoginActivity.this, UserHomeActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure() {
                Toast.makeText(UserLoginActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        }), 2000);
    }

    private void setupComponent() {
        cbStorePassword.setChecked(true);
    }

    private void getParseData() {
        Intent intent = getIntent();
        if (intent != null) {
            String receivedString = intent.getStringExtra("passEmail");
            etxtEmail.setText(receivedString);
        }
    }

    private void getSQLiteData() {
        accountDAO = new AccountDAO(mContext);
        boolean check = accountDAO.checkExistAccount();
        if (check) {
            Account account = accountDAO.getAccount();
            etxtEmail.setText(account.getEmail());
            etxtPassword.setText(account.getPassword());
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
        String getEmail = String.valueOf(etxtEmail.getText());
        String getPassword = String.valueOf(etxtPassword.getText());

        boolean flagCheckEmptyEmail = checkEmptyEmail(getEmail);
        if (flagCheckEmptyEmail) {
            Toast.makeText(mContext, "Trường email không được rỗng!", Toast.LENGTH_SHORT).show();
            etxtEmail.requestFocus();
            return;
        }

        boolean flagCheckEmptyPass = checkEmptyPass(getPassword);
        if (flagCheckEmptyPass) {
            Toast.makeText(mContext, "Trường mật khẩu không được rỗng!", Toast.LENGTH_SHORT).show();
            etxtPassword.requestFocus();
            return;
        }

        boolean flagIsValidEmail = isValidEmail(getEmail);
        if (!flagIsValidEmail) {
            Toast.makeText(mContext, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
            etxtEmail.requestFocus();
            return;
        }

        getAllData();

        boolean flagCheckEmailAvailable = emailList.contains(getEmail);
        if (!flagCheckEmailAvailable) {
            Toast.makeText(mContext, "Email này chưa được đăng ký!", Toast.LENGTH_SHORT).show();
            etxtEmail.requestFocus();
            return;
        }

        int position = emailList.indexOf(getEmail);
        String storedPassword = String.valueOf((passwordList.get(position)));
        String md5Pass = MD5.md5(getPassword);
        if (!storedPassword.equals(md5Pass)) {
            Toast.makeText(mContext, "Sai tên đăng nhập hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
            txtForgotPass.requestFocus();
            return;
        }

        int storedCode = codeList.get(position);
        if (storedCode != 999999) {
            Toast.makeText(mContext, "Tài khoản này chưa kích hoạt mã xác thực!", Toast.LENGTH_SHORT).show();
            txtConfirm.requestFocus();
            return;
        }

        StorePassword(position, getPassword);

        Toast.makeText(mContext, "Đăng nhập thành công. Đang chuyển đến trang chủ!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(UserLoginActivity.this, UserHomeActivity.class);
            startActivity(intent);

            finish();
        }, 2000);
    }

    private void StorePassword(int position, String unMD5Pass) {
        int id = idList.get(position);
        daoLogin.getAccountById(id, account -> {
            if (account != null) {
                accountDAO = new AccountDAO(mContext);
                account.setPassword(unMD5Pass);
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

    private void goToSignup() {
        Intent intent = new Intent(UserLoginActivity.this, UserRegisterActivity.class);
        startActivity(intent);
    }

    private void goToConfirmCode() {
        String email;
        try {
            email = String.valueOf(etxtEmail.getText());
        } catch (NullPointerException e) {
            email = "";
        }
        Intent intent = new Intent(UserLoginActivity.this, UserConfirmCodeActivity.class);
        intent.putExtra("passEmail", email);
        startActivity(intent);
    }

    private void goToRestorePass() {
        String email;
        try {
            email = String.valueOf(etxtEmail.getText());
        } catch (NullPointerException e) {
            email = "";
        }
        Intent intent = new Intent(UserLoginActivity.this, UserRestorePassActivity.class);
        intent.putExtra("passEmail", email);
        startActivity(intent);
    }
}