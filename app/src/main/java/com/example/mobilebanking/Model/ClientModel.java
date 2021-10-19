package com.example.mobilebanking.Model;

import com.google.gson.annotations.SerializedName;

public class ClientModel {
    @SerializedName("operation")
    private  String operation;
    @SerializedName("pin")
    private String pin;
    @SerializedName("fingerePrint")
    private  String fingerePrint;
    @SerializedName("sNo")
    private  String sNo;

    public ClientModel(String operation, String pin, String fingerePrint, String sNo) {
        this.operation = operation;
        this.pin = pin;
        this.fingerePrint = fingerePrint;
        this.sNo = sNo;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getFingerePrint() {
        return fingerePrint;
    }

    public void setFingerePrint(String fingerePrint) {
        this.fingerePrint = fingerePrint;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }
}
