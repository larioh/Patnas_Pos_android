package com.example.mobilebanking.Rest;

import com.example.mobilebanking.Model.ClientModel;
import com.example.mobilebanking.Model.DepositModel;
import com.example.mobilebanking.Model.Response;
import com.example.mobilebanking.Model.MemberModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("api/users.php")
    Call<Response> getMember(@Body MemberModel memberModel);

    @POST("api/users.php")
    Call<Response> createClient(@Body ClientModel clientModel);

    @POST("api/users.php")
    Call<Response> createUser(@Body MemberModel memberModel);

    @POST("api/transactions.php")
    Call<Response> deposit(@Body DepositModel depositModel);

    @POST("api/transactions.php")
    Call<Response> withdraw(@Body DepositModel depositModel);

    @GET("api/users.php")
    Call<Response> getMembers();

//    @POST("users/new")
//    Call<Example> createUser(@Body Example example);

//    @GET("movie/{id}")
//    Call<Example> getExamples(@Path("id") int id, @Query("api_key") String apiKey);
}
