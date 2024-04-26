package com.example.androidproject;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidproject.dao.AccountDAO;
import com.example.androidproject.dao.DAOUpdateInfo;
import com.example.androidproject.entity.Account;

public class UserUpdateInfoActivity extends AppCompatActivity {
    private EditText txtFullName;
    private TextView txtDob, txtMoney, txtVip;
    private DatePicker datePicker;
    private Button btnPick, btnBank, btnBuy, btnUpdate;
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

        btnPick.setOnClickListener(v -> openDatePicker());

        btnUpdate.setOnClickListener(v -> updateInfo());

        btnBank.setOnClickListener(v -> TransfertopageBanking());

        btnBuy.setOnClickListener(v -> TransfertopageBanking());
    }

    private void mappingComponent() {
        txtFullName = findViewById(R.id.editTextFullName);

        txtDob = findViewById(R.id.textViewDob);
        txtMoney = findViewById(R.id.textViewMoney);
        txtVip = findViewById(R.id.textViewVip);

        datePicker = findViewById(R.id.datePicker);

        btnPick = findViewById(R.id.buttonPickDate);
        btnBank = findViewById(R.id.buttonBank);
        btnBuy = findViewById(R.id.buttonBuy);
        btnUpdate = findViewById(R.id.buttonUpdate);

        mContext = this;
    }

    private void getSQLiteData() {
        accountDAO = new AccountDAO(mContext);
        boolean check = accountDAO.checkExistAccount();
        if (check) {
            Account account = accountDAO.getAccount();
            txtFullName.setText(account.getFullName());

            txtDob.setText(String.valueOf(account.getDob()));
            txtMoney.setText(String.valueOf(account.getMoney()));
            txtVip.setText(String.valueOf(account.getVip()));
        }
    }

    private void openDatePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select Date");

        String dobText = txtDob.getText().toString();
        String[] dobValues = dobText.split("/");
        int day = Integer.parseInt(dobValues[0]);
        int month = Integer.parseInt(dobValues[1]) - 1;
        int year = Integer.parseInt(dobValues[2]);

        datePicker = new DatePicker(mContext);
        datePicker.init(year, month, day, null);

        builder.setView(datePicker);
        builder.setPositiveButton("OK", (dialog, which) -> {
            int day1 = datePicker.getDayOfMonth();
            int month1 = datePicker.getMonth() + 1;
            int year1 = datePicker.getYear();

            txtDob.setText(day1 + "/" + month1 + "/" + year1);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateInfo() {
        String fullName = String.valueOf(txtFullName.getText());
        String dob = String.valueOf(txtDob.getText());

        boolean flagCheckEmptyFullName = checkEmptyFullName(fullName);
        if(flagCheckEmptyFullName) {
            Toast.makeText(mContext, "Trường họ và tên không được để trống!", Toast.LENGTH_SHORT).show();
            txtFullName.requestFocus();
            return;
        }

        accountDAO = new AccountDAO(mContext);
        Account account = accountDAO.getAccount();

        account.setFullName(fullName);
        account.setDob(dob);

        daoUpdateInfo.UpdateInfo(account.getAccountID(), account);
        accountDAO.addAccount(account, true);

        Toast.makeText(mContext, "Cập nhật thông tin cá nhân thành công!", Toast.LENGTH_SHORT).show();
    }

    private boolean checkEmptyFullName(String fullName) {
        return fullName.equals("");
    }

    private void TransfertopageBanking() {

    }
}