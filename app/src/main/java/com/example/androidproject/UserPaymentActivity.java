package com.example.androidproject;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.dao.AccountDAO;
import com.example.androidproject.dao.DAOCard;
import com.example.androidproject.dao.DAOPayment;
import com.example.androidproject.entity.Account;

import java.util.ArrayList;
import java.util.List;

public class UserPaymentActivity extends AppCompatActivity {

    private Context mContext;
    private Button btnPay;
    private TextView txtMoney;
    private EditText etxtCardCode, etxtMoney;
    private Account account;
    private final DAOPayment daoPayment = new DAOPayment();
    private final DAOCard daoCard = new DAOCard();
    private List<String> codeList = new ArrayList<>();
    private final List<Float> valueList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payment);

        mappingComponent();

        getSQLiteData();

        btnPay.setOnClickListener(v -> PayWithCard());

    }

    private void mappingComponent() {
        mContext = this;

        txtMoney = findViewById(R.id.textViewMoney);
        etxtCardCode = findViewById(R.id.editTextCardCode);
        etxtMoney = findViewById(R.id.editTextMoney);
        btnPay = findViewById(R.id.buttonPay);
    }

    private void getSQLiteData() {
        AccountDAO accountDAO = new AccountDAO(mContext);
        boolean check = accountDAO.checkExistAccount();
        if (check) {
            this.account = accountDAO.getAccount();
            txtMoney.setText((int) this.account.getMoney());
        }
    }

    private void getAllData() {
        daoCard.getCodeList(codeList -> {
            this.codeList.clear();
            this.codeList = codeList;
        });

        daoCard.getValueList(valueList -> {
            this.valueList.clear();
            for (String value : valueList) {
                try {
                    Float floatValue = Float.parseFloat(value);
                    this.valueList.add(floatValue);
                } catch (NumberFormatException ignored) {
                }
            }
        });
    }

    private void PayPalButton() {
        boolean flagCheckPayPalInput = checkPayPalInput();
        if (!flagCheckPayPalInput) {
            return;
        }
        String sInputMoney = String.valueOf(etxtMoney.getText());
        float inputMoney = Float.parseFloat(sInputMoney);

    }

    private boolean checkPayPalInput() {
        float inputMoney;
        String sInputMoney = String.valueOf(etxtMoney.getText());

        if(sInputMoney.equals("")) {
            Toast.makeText(mContext, "The input value cannot be empty!", Toast.LENGTH_SHORT).show();
            etxtMoney.requestFocus();
            return false;
        }

        inputMoney = Float.parseFloat(sInputMoney);

        if(inputMoney == 0) {
            Toast.makeText(mContext, "The input value cannot equal to 0!", Toast.LENGTH_SHORT).show();
            etxtMoney.requestFocus();
            return false;
        }
        return true;
    }

    private void updateNewMoney(float inputMoney) {
        AccountDAO accountDAO = new AccountDAO(mContext);
        float oldMoney = account.getMoney();
        float newMoney = oldMoney + inputMoney;

        account.setMoney(newMoney);

        accountDAO.addAccount(account, accountDAO.getStoreState());

        txtMoney.setText(String.valueOf(newMoney));
    }

    private void PayWithCard() {
        String cardCode = String.valueOf(etxtCardCode.getText());
        getAllData();

        if(!codeList.contains(cardCode)) {
            Toast.makeText(mContext, "The card code isn't available", Toast.LENGTH_SHORT).show();
            etxtCardCode.requestFocus();
        } else {
            int position = codeList.indexOf(cardCode);
            float value = valueList.get(position);
            daoPayment.Pay(account.getAccountID(), value);
            updateNewMoney(value);
            Toast.makeText(mContext, "Payment Successful", Toast.LENGTH_SHORT).show();
        }
    }
}