package com.manuelsarante.bebankapp.dto;

public class TransactionDto {

    //Account you will be sending or reciving the money from
    private String outAccount;
    private double amount;
    //account of the actual user
    private String actualAccount;

    public String getOutAccount() {
        return outAccount;
    }

    public void setOutAccount(String outAccount) {
        this.outAccount = outAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getActualAccount() {
        return actualAccount;
    }

    public void setActualAccount(String actualAccount) {
        this.actualAccount = actualAccount;
    }
}
