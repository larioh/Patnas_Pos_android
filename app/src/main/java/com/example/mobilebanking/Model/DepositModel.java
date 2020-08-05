package com.example.mobilebanking.Model;

import com.google.gson.annotations.SerializedName;

public class DepositModel {
    @SerializedName("operation")
    private  String operation;
    @SerializedName("amount")
    private Double amount;
    @SerializedName("fingerePrint")
    private  String fingerePrint;
    @SerializedName("pin")
    private String pin;
    @SerializedName("sNo")
    private  String sNo;

    public DepositModel(String operation, Double amount, String fingerePrint, String pin, String sNo) {
        this.operation = operation;
        this.amount = amount;
        this.fingerePrint = fingerePrint;
        this.pin = pin;
        this.sNo = sNo;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getFingerePrint() {
        return fingerePrint;
    }

    public void setFingerePrint(String fingerePrint) {
        this.fingerePrint = fingerePrint;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }
}
