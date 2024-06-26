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

import com.example.androidproject.dao.DAORestorePassword;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
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

public class UserRestorePassActivity extends AppCompatActivity {
    private Button btnSend;
    private ImageButton btnBack;
    private EditText txtEmail;
    private Context mContext;
    private final List<Integer> idList = new ArrayList<>();
    private List<String> emailList = new ArrayList<>();
    private final DAORestorePassword daoRestorePassword = new DAORestorePassword();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_restore_pass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mappingComponent();

        getParseData();

        getAllData();

        btnSend.setOnClickListener(v -> sendNewPassword());

        btnBack.setOnClickListener(v -> goToLogin());
    }

    private void mappingComponent() {
        txtEmail = findViewById(R.id.editTextEmail);

        btnSend = findViewById(R.id.buttonSend);
        btnBack = findViewById(R.id.buttonBack);

        mContext = this;
    }

    private void getParseData() {
        Intent intent = getIntent();
        if (intent != null) {
            String receivedString = intent.getStringExtra("passEmail");
            txtEmail.setText(receivedString);
        }
    }

    private void getAllData() {
        daoRestorePassword.getIDList(idList -> {
            this.idList.clear();
            for (String id : idList) {
                try {
                    Integer intValue = Integer.parseInt(id);
                    this.idList.add(intValue);
                } catch (NumberFormatException ignored) {
                }
            }
        });

        daoRestorePassword.getEmailList(emailList -> {
            this.emailList.clear();
            this.emailList = emailList;
        });
    }

    private void sendNewPassword() {
        String getEmail = String.valueOf(txtEmail.getText());

        boolean flagCheckEmptyEmail = checkEmptyEmail(getEmail);
        if (flagCheckEmptyEmail) {
            Toast.makeText(mContext, "Trường email không được rỗng!", Toast.LENGTH_SHORT).show();
            txtEmail.requestFocus();
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

        String newPassword = generateRandomString();
        String md5Pass = MD5.md5(newPassword);
        int position = emailList.indexOf(getEmail);
        String sid = String.valueOf(idList.get(position));

        daoRestorePassword.updatePassword(sid, md5Pass);

        sendNewPassToEmail(getEmail, newPassword);
        Toast.makeText(mContext, "Mật khẩu mới đã được gửi về email " + getEmail + "!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(UserRestorePassActivity.this, UserLoginActivity.class);
        intent.putExtra("passEmail", getEmail);
        startActivity(intent);
    }

    private boolean checkEmptyEmail(String email) {
        return (email == null || email.equals(""));
    }

    public static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String generateRandomString() {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    private void sendNewPassToEmail(String email, String newPass) {
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

            mimeMessage.setSubject("Khôi phục tài khoản");
            mimeMessage.setText("Mật khẩu mới của tài khoản bạn là:" + newPass);

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

    private void goToLogin() {
        Intent intent = new Intent(UserRestorePassActivity.this, UserLoginActivity.class);
        startActivity(intent);
    }
}