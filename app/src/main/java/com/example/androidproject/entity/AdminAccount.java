package com.example.androidproject.entity;

import java.io.Serializable;

public class AdminAccount implements Serializable {
    public int AccountID;
    public String DisplayName;
    public String DoB;
    public String Email;
    public String Password;
    public String Role;
    public AdminAccount(){}
    public AdminAccount( int AccountID, String DisplayName, String DoB, String Email, String Password, String Role){
        this.AccountID = AccountID;
        this.DisplayName = DisplayName;
        this.DoB = DoB;
        this.Email = Email;
        this.Password = Password;
        this.Role = Role;
    }

    public int getAccountID() {
        return AccountID;
    }

    public void setAccountID(int accountID) {
        AccountID = accountID;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getDoB() {
        return DoB;
    }

    public void setDoB(String doB) {
        DoB = doB;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
