package com.example.mobilebanking.Rest;

import com.example.mobilebanking.Model.Example;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("example")
    Call<List<Example>> getExamples();

    @GET("movie/top_rated")
    Call<Example> getTopExamples(@Query("api_key") String apiKey);

//    @POST("users/new")
//    Call<Example> createUser(@Body Example example);

//    @GET("movie/{id}")
//    Call<Example> getExamples(@Path("id") int id, @Query("api_key") String apiKey);
}
