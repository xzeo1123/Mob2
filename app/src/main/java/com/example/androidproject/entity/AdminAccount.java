package com.example.androidproject.entity;

import java.io.Serializable;

public class AdminAccount implements Serializable {
    private String AccountID;
    private String DisplayName;
    private String DoB;
    private String Email;
    private String Password;
    private String Role;
    public AdminAccount(){}
    public AdminAccount( String AccountID, String DisplayName, String DoB, String Email, String Password, String Role){
        this.AccountID = AccountID;
        this.DisplayName = DisplayName;
        this.DoB = DoB;
        this.Email = Email;
        this.Password = Password;
        this.Role = Role;
    }

    public String getAccountID() {
        return AccountID;
    }

    public void setAccountID(String accountID) {
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
