package com.example.androidproject.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CreateDatabaseAccount extends SQLiteOpenHelper {
    public static String TB_ACCOUNT = "Account";
    public static String TB_ACCOUNT_ACCOUNTID = "accountID";
    public static String TB_ACCOUNT_EMAIL = "email";
    public static String TB_ACCOUNT_PASSWORD = "password";
    public static String TB_ACCOUNT_MONEY = "money";
    public static String TB_ACCOUNT_CONFIRMEDCODE = "confirmedCode";
    public static String TB_ACCOUNT_FULLNAME = "fullName";
    public static String TB_ACCOUNT_DOB = "dob";
    public static String TB_ACCOUNT_ISVIP = "isVip";
    public CreateDatabaseAccount(@Nullable Context context) {
        super(context, "Mob2", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tbAccount = "CREATE TABLE " + TB_ACCOUNT + " (" +
                TB_ACCOUNT_ACCOUNTID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TB_ACCOUNT_EMAIL + " TEXT," +
                TB_ACCOUNT_PASSWORD + " TEXT," +
                TB_ACCOUNT_MONEY + " FLOAT," +
                TB_ACCOUNT_CONFIRMEDCODE + " INTEGER," +
                TB_ACCOUNT_FULLNAME + " TEXT," +
                TB_ACCOUNT_DOB + " TEXT," +
                TB_ACCOUNT_ISVIP + " BOOLEAN, " +
                "storeState BOOLEAN)";
        db.execSQL(tbAccount);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public SQLiteDatabase open() {
        return this.getWritableDatabase();
    }
}
