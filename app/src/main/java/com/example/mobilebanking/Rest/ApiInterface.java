package com.example.mobilebanking.Rest;

import com.example.mobilebanking.Model.LoginResponse;
import com.example.mobilebanking.Model.Member;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("databaseScripts/dbScript.php")
    Call<LoginResponse> getMember(@Body Member member);

    @GET("databaseScripts/dbScript.php")
    Call<LoginResponse> getMembers();

//    @POST("users/new")
//    Call<Example> createUser(@Body Example example);

//    @GET("movie/{id}")
//    Call<Example> getExamples(@Path("id") int id, @Query("api_key") String apiKey);
}
