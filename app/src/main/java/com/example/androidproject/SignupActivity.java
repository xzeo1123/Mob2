package com.example.androidproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.dao.DAOSignup;
import com.example.androidproject.entity.Account;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SignupActivity extends AppCompatActivity {
    private View mainLayout;
    private EditText txtEmail, txtPassword, txtRePassword;
    private CheckBox cbAgree;
    private Button btnSignup;
    private Context mContext;
    private int marking = 0;
    private List<String> emailList = new ArrayList<>();
    private final List<Integer> idList = new ArrayList<>();
    private final DAOSignup daoSignup = new DAOSignup();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mappingComponent();

        setMainLayout();

        getAllData();

        btnSignup.setOnClickListener(v -> signUp());
    }

    private void mappingComponent() {
        mainLayout = findViewById(R.id.mainLayout);

        txtEmail = findViewById(R.id.editTextEmail);
        txtPassword = findViewById(R.id.editTextPassword);
        txtRePassword = findViewById(R.id.editTextRePassword);

        cbAgree = findViewById(R.id.checkBoxAgree);

        btnSignup = findViewById(R.id.buttonSignup);

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

    private void getAllData() {
        daoSignup.getIDList(idList -> {
            this.idList.clear();
            for (String id : idList) {
                try {
                    Integer intValue = Integer.parseInt(id);
                    this.idList.add(intValue);
                } catch (NumberFormatException ignored) {
                }
            }
        });

        daoSignup.getEmailList(emailList -> {
            this.emailList.clear();
            this.emailList = emailList;
        });
    }

    private void signUp() {
        String getEmail = String.valueOf(txtEmail.getText());
        String getPassword = String.valueOf(txtPassword.getText());
        String getRePassword = String.valueOf(txtRePassword.getText());

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

        boolean flagCheckEmptyRePass = checkEmptyRePass(getPassword);
        if (flagCheckEmptyRePass) {
            Toast.makeText(mContext, "Trường mật khẩu không được rỗng!", Toast.LENGTH_SHORT).show();
            txtPassword.requestFocus();
            return;
        }

        boolean flagCheckMatchPassword = checkMatchPass(getPassword, getRePassword);
        if (!flagCheckMatchPassword) {
            Toast.makeText(mContext, "Mật khẩu nhập lại không khớp!", Toast.LENGTH_SHORT).show();
            txtPassword.requestFocus();
            return;
        }

        boolean flagIsValidEmail = isValidEmail(getEmail);
        if (!flagIsValidEmail) {
            Toast.makeText(mContext, "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
            txtEmail.requestFocus();
            return;
        }

        boolean flagCheckAgree = cbAgree.isChecked();
        if (!flagCheckAgree) {
            Toast.makeText(mContext, "Bạn chưa chấp điều khoản dịch vụ!", Toast.LENGTH_SHORT).show();
            cbAgree.requestFocus();
            return;
        }

        getAllData();

        boolean flagHasEmail = hasEmail(getEmail);
        if (flagHasEmail) {
            Toast.makeText(mContext, "Email này đã được sử dụng!", Toast.LENGTH_SHORT).show();
            txtEmail.requestFocus();
            return;
        }

        setPosition();

        int randomcode = (int) (Math.random() * (999998 - 100000 + 1) + 100000);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        String formattedDate = currentDate.format(formatter);

        Account ac;
        ac = new Account(marking, getEmail, getPassword, 0, randomcode, "NAME", formattedDate, false);
        daoSignup.addAccount(marking, ac);

        sendCodeToEmail(getEmail, randomcode);
        Toast.makeText(mContext, "Mã xác nhận đã gửi về email " + getEmail + "!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(SignupActivity.this, ConfirmCodeActivity.class);
        intent.putExtra("passEmail", getEmail);
        startActivity(intent);
    }

    private boolean checkEmptyEmail(String email) {
        return (email == null || email.equals(""));
    }

    private boolean checkEmptyPass(String pass) {
        return (pass == null || pass.equals(""));
    }

    private boolean checkEmptyRePass(String repass) {
        return (repass == null || repass.equals(""));
    }

    private boolean checkMatchPass(String pass, String repass) {
        return pass.equals(repass);
    }

    public static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void setPosition() {
        getAllData();
        int i = 1;
        while (true) {
            if (!idList.contains(i)) {
                marking = i;
                break;
            }
            i += 1;
        }
    }

    private boolean hasEmail(String email) {
        return emailList.contains(email);
    }

    private void sendCodeToEmail(String email, int code) {
        try {
            String stringSenderEmail = "xzeo11123@gmail.com";
            String stringPasswordSenderEmail = "jhzs vunl mlqs dwas";

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            mimeMessage.setSubject("Kích hoạt tài khoản");
            mimeMessage.setText("Mã kích hoạt tài khoản của bạn là:" + code);

            Thread thread = new Thread(() -> {
                try {
                    Transport.send(mimeMessage);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
