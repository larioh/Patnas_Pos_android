package com.example.mobilebanking.Model;

import com.google.gson.annotations.SerializedName;

public class Example {
    @SerializedName("id")
    private Integer id;
    @SerializedName("title")
    private String title;

    public Example(Integer id, String title){
        this.id = id;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
