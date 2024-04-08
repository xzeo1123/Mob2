package com.example.androidproject.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.androidproject.entity.Account;
import com.example.androidproject.sqlitedb.CreateDatabaseAccount;

public class AccountDAO {
    SQLiteDatabase db;

    public AccountDAO(Context context) {
        CreateDatabaseAccount createDatabase = new CreateDatabaseAccount(context);
        db = createDatabase.open();
    }

    public void addAccount(Account account, boolean storeState) {
        try {
            delAccount();

            ContentValues values = new ContentValues();

            values.put(CreateDatabaseAccount.TB_ACCOUNT_ACCOUNTID, account.getAccountID());
            values.put(CreateDatabaseAccount.TB_ACCOUNT_EMAIL, account.getEmail());
            values.put(CreateDatabaseAccount.TB_ACCOUNT_PASSWORD, account.getPassword());
            values.put(CreateDatabaseAccount.TB_ACCOUNT_MONEY, account.getMoney());
            values.put(CreateDatabaseAccount.TB_ACCOUNT_CONFIRMEDCODE, account.getConfirmedCode());
            values.put(CreateDatabaseAccount.TB_ACCOUNT_FULLNAME, account.getFullName());
            values.put(CreateDatabaseAccount.TB_ACCOUNT_DOB, account.getDob());
            values.put(CreateDatabaseAccount.TB_ACCOUNT_ISVIP, account.getVip());
            values.put("storeState", storeState);
            db.insert(CreateDatabaseAccount.TB_ACCOUNT, null, values);
        } catch (Exception e) {
            Log.i("WTF", e.toString());
        }
    }

    public void delAccount() {
        db.delete(CreateDatabaseAccount.TB_ACCOUNT, null, null);
    }

    public boolean checkExistAccount() {
        String query = "SELECT * FROM " + CreateDatabaseAccount.TB_ACCOUNT;
        Cursor cursor = db.rawQuery(query, null);
        return cursor.getCount() != 0;
    }

    public Account getAccount() {
        String query = "SELECT * FROM " + CreateDatabaseAccount.TB_ACCOUNT;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            Account account = new Account();
            account.setAccountID(cursor.getInt(0));
            account.setEmail(cursor.getString(1));
            account.setPassword(cursor.getString(2));
            account.setMoney(cursor.getFloat(3));
            account.setConfirmedCode(cursor.getInt(4));
            account.setFullName(cursor.getString(5));
            account.setDob(cursor.getString(6));
            account.setVip(cursor.getInt(7) == 1);
            cursor.close();
            return account;
        } else {
            cursor.close();
            return null;
        }
    }

    public int getID() {
        String query = "SELECT accountID FROM " + CreateDatabaseAccount.TB_ACCOUNT_ACCOUNTID;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        } else {
            cursor.close();
            return 0;
        }
    }

    public String getEmail() {
        String query = "SELECT email FROM " + CreateDatabaseAccount.TB_ACCOUNT_EMAIL;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            String email = cursor.getString(0);
            cursor.close();
            return email;
        } else {
            cursor.close();
            return "";
        }
    }

    public String getPassword() {
        String query = "SELECT password FROM " + CreateDatabaseAccount.TB_ACCOUNT_PASSWORD;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            String pass = cursor.getString(0);
            cursor.close();
            return pass;
        } else {
            cursor.close();
            return "";
        }
    }

    public float getMoney() {
        String query = "SELECT money FROM " + CreateDatabaseAccount.TB_ACCOUNT_MONEY;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(0);
            cursor.close();
            return money;
        } else {
            cursor.close();
            return 0;
        }
    }

    public int getConfirmedCode() {
        String query = "SELECT confirmedCode FROM " + CreateDatabaseAccount.TB_ACCOUNT_CONFIRMEDCODE;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int code = cursor.getInt(0);
            cursor.close();
            return code;
        } else {
            cursor.close();
            return 0;
        }
    }

    public String getFullName() {
        String query = "SELECT fullName FROM " + CreateDatabaseAccount.TB_ACCOUNT_FULLNAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            String fullName = cursor.getString(0);
            cursor.close();
            return fullName;
        } else {
            cursor.close();
            return "";
        }
    }

    public String getDOB() {
        String query = "SELECT dob FROM " + CreateDatabaseAccount.TB_ACCOUNT_DOB;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            String dob = cursor.getString(0);
            cursor.close();
            return dob;
        } else {
            cursor.close();
            return "";
        }
    }

    public boolean getIsVip() {
        String query = "SELECT isVip FROM " + CreateDatabaseAccount.TB_ACCOUNT_ISVIP;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            boolean isVip = (cursor.getInt(0) == 1);
            cursor.close();
            return isVip;
        } else {
            cursor.close();
            return false;
        }
    }

    public boolean getStoreState() {
        String query = "SELECT storeState FROM " + CreateDatabaseAccount.TB_ACCOUNT;
        Cursor cursor = db.rawQuery(query, null);

        boolean flag;

        if (cursor.moveToFirst()) {
            flag = (cursor.getInt(0) == 1);
            cursor.close();
        } else {
            cursor.close();
            return false;
        }
        return flag;
    }

}
