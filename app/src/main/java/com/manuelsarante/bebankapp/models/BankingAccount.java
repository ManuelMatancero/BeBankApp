package com.manuelsarante.bebankapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BankingAccount implements Serializable {

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
    private Cards cards;

    @SerializedName("transactions")
    @Expose
    private List<AccountTransactions> transactions;

    public List<AccountTransactions> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<AccountTransactions> transactions) {
        this.transactions = transactions;
    }

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

    public Cards getCards() {
        return cards;
    }

    public void setCards(Cards cards) {
        this.cards = cards;
    }
}
