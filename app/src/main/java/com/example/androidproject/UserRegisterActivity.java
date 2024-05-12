package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidproject.dao.DAOPlayList;
import com.example.androidproject.dao.DAOSignup;
import com.example.androidproject.entity.Account;
import com.example.androidproject.entity.PlayList;

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

public class UserRegisterActivity extends AppCompatActivity {
    private EditText txtEmail, txtPassword, txtRePassword;
    private CheckBox cbAgree;
    private Button btnSignup;
    private ImageButton btnBack;
    private Context mContext;
    private int marking = 1;
    private int ListRMarking = 1;
    private int ListFMarking = 1;
    private List<String> emailList = new ArrayList<>();
    private final List<Integer> idList = new ArrayList<>();
    private final List<Integer> idPlayList = new ArrayList<>();
    private final DAOSignup daoSignup = new DAOSignup();
    private final DAOPlayList daoPlayList = new DAOPlayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mappingComponent();

        getAllData();

        btnSignup.setOnClickListener(v -> signUp());

        btnBack.setOnClickListener(v -> goToLogin());
    }

    private void mappingComponent() {
        txtEmail = findViewById(R.id.editTextEmail);
        txtPassword = findViewById(R.id.editTextPassword);
        txtRePassword = findViewById(R.id.editTextRePassword);

        cbAgree = findViewById(R.id.checkBoxAgree);

        btnSignup = findViewById(R.id.buttonSignup);
        btnBack = findViewById(R.id.buttonBack);

        mContext = this;
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
        daoPlayList.getIDList(idList -> {
            this.idPlayList.clear();
            for (String id : idList) {
                try {
                    Integer intValue = Integer.parseInt(id);
                    this.idPlayList.add(intValue);
                } catch (NumberFormatException ignored) {
                }
            }
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
        String md5Pass = MD5.md5(getPassword);
        ac = new Account(marking, getEmail, md5Pass, 0, randomcode, "NAME", formattedDate, false);
        daoSignup.addAccount(marking, ac);

        //add default list
        addFavoritePlayList();

        sendCodeToEmail(getEmail, randomcode);
        Toast.makeText(mContext, "Mã xác nhận đã gửi về email " + getEmail + "!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(UserRegisterActivity.this, UserConfirmCodeActivity.class);
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
    private void setFListPosition() {
        getAllData();
        while (idPlayList.contains(ListFMarking)) {
            ListFMarking++;
        }
        ListRMarking = ListFMarking + 1;
        while (idPlayList.contains(ListRMarking)) {
            ListRMarking++;
        }
    }
    private void addFavoritePlayList() {
        setFListPosition();
        PlayList newFPlayList = new PlayList(ListFMarking, "Favorite",marking);
        DAOPlayList.addPlayList(ListFMarking, newFPlayList);
        PlayList newRPlayList = new PlayList(ListRMarking, "Reading",marking);
        DAOPlayList.addPlayList(ListRMarking, newRPlayList);
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

    private void goToLogin() {
        Intent intent = new Intent(UserRegisterActivity.this, UserLoginActivity.class);
        startActivity(intent);
    }
}