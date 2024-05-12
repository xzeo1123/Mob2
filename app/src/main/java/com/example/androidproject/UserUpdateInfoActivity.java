package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidproject.dao.AccountDAO;
import com.example.androidproject.dao.DAOUpdateInfo;
import com.example.androidproject.entity.Account;

public class UserUpdateInfoActivity extends AppCompatActivity {
    private EditText txtFullName;
    private TextView txtMoney, txtVip;
    private DatePicker datePicker;
    private Button btnActivate, btnBuy, btnUpdate;
    private ImageButton btnBack;
    private Context mContext;
    private final DAOUpdateInfo daoUpdateInfo = new DAOUpdateInfo();
    private AccountDAO accountDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_update_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mappingComponent();

        getSQLiteData();

        btnUpdate.setOnClickListener(v -> updateInfo());

        btnBuy.setOnClickListener(v -> TransferToBuy());

        btnActivate.setOnClickListener(v -> TransferToActivateVIP());

        btnBack.setOnClickListener(v -> goToUser());
    }

    private void mappingComponent() {
        txtFullName = findViewById(R.id.editTextFullName);

        txtMoney = findViewById(R.id.textViewMoney);
        txtVip = findViewById(R.id.textViewVip);

        datePicker = findViewById(R.id.datePicker);

        btnBuy = findViewById(R.id.buttonBuy);
        btnActivate = findViewById(R.id.buttonActivate);
        btnUpdate = findViewById(R.id.buttonUpdate);

        btnBack = findViewById(R.id.buttonBack);

        mContext = this;
    }

    private void getSQLiteData() {
        accountDAO = new AccountDAO(mContext);
        boolean check = accountDAO.checkExistAccount();
        if (check) {
            Account account = accountDAO.getAccount();
            txtFullName.setText(account.getFullName());

            String dateFromDatabase = account.getDob();

            String[] parts = dateFromDatabase.split(" ");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]) - 1;
            int year = Integer.parseInt(parts[2]);

            datePicker.init(year, month, day, null);

            txtMoney.setText(String.valueOf(account.getMoney()));
            txtVip.setText(String.valueOf(account.getVip()));
        }
    }

    private void updateInfo() {
        String fullName = String.valueOf(txtFullName.getText());

        boolean flagCheckEmptyFullName = checkEmptyFullName(fullName);
        if(flagCheckEmptyFullName) {
            Toast.makeText(mContext, "Trường họ và tên không được để trống!", Toast.LENGTH_SHORT).show();
            txtFullName.requestFocus();
            return;
        }

        accountDAO = new AccountDAO(mContext);
        Account account = accountDAO.getAccount();

        account.setFullName(fullName);

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();

        String formattedDate = String.format("%02d %02d %04d", day, month, year);

        account.setDob(formattedDate);

        daoUpdateInfo.UpdateInfo(account.getAccountID(), account);
        accountDAO.addAccount(account, true);

        Toast.makeText(mContext, "Cập nhật thông tin cá nhân thành công!", Toast.LENGTH_SHORT).show();
    }

    private boolean checkEmptyFullName(String fullName) {
        return fullName.equals("");
    }

    private void TransferToBuy() {
        Intent intent = new Intent(UserUpdateInfoActivity.this, UserPaymentActivity.class);
        startActivity(intent);
    }

    private void TransferToActivateVIP() {

    }

    private void goToUser() {
        Intent intent = new Intent(UserUpdateInfoActivity.this, UserAfter.class);
        startActivity(intent);
    }
}