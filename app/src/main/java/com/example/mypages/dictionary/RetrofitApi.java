package com.example.mypages.dictionary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitApi {
    @GET("en/{word}")
    Call<List<Root>> getData(@Path("word") String word);
}
