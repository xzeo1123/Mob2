package com.example.androidproject.entity;

public class Account {
    private int accountID;
    private String email;
    private String password;
    private float money;
    private int confirmedCode;
    private String fullName;
    private String dob;
    private boolean isVip;

    public Account() {
    }

    public Account(int accountID, String email, String password, float money, int confirmedCode, String fullName, String dob, boolean isVip){
        this.accountID = accountID;
        this.email = email;
        this.password = password;
        this.money = money;
        this.confirmedCode = confirmedCode;
        this.fullName = fullName;
        this.dob = dob;
        this.isVip = isVip;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getConfirmedCode() {
        return confirmedCode;
    }

    public void setConfirmedCode(int confirmedCode) {
        this.confirmedCode = confirmedCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public boolean getVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }
}
