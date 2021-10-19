package com.example.mobilebanking.Model;

import com.google.gson.annotations.SerializedName;

public class MemberModel {
    @SerializedName("username")
    private String usercode;
    @SerializedName("password")
    private  String password;
    @SerializedName("operation")
    private  String operation;

    public MemberModel(String usercode, String password, String operation) {
        this.usercode = usercode;
        this.password = password;
        this.operation = operation;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
