package com.manuelsarante.bebankapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankingAccount {

    @SerializedName("idAccount")
    @Expose
    private Long idAccount;

    @SerializedName("accountNumber")
    @Expose
    private int accountNumber;

    @SerializedName("mountAccount")
    @Expose
    private double mountAccount;

    @SerializedName("cards")
    @Expose
    private List<Cards> cards;

    public Long getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Long idAccount) {
        this.idAccount = idAccount;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getMountAccount() {
        return mountAccount;
    }

    public void setMountAccount(double mountAccount) {
        this.mountAccount = mountAccount;
    }

    public List<Cards> getCards() {
        return cards;
    }

    public void setCards(List<Cards> cards) {
        this.cards = cards;
    }
}
