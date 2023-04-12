package com.manuelsarante.bebankapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    @SerializedName("idUser")
    @Expose
    private Long idUser;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("role")
    @Expose
    private int role;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("pin")
    @Expose
    private String pin;

    @SerializedName("bankingAccounts")
    @Expose
    private List<BankingAccount> bankingAccounts;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public List<BankingAccount> getBankingAccounts() {
        return bankingAccounts;
    }

    public void setBankingAccounts(List<BankingAccount> bankingAccounts) {
        this.bankingAccounts = bankingAccounts;
    }
}
