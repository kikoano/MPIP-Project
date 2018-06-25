package com.example.kikoano111.mpip.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Headers;
import java.util.List;
/**
 * Created by kikoano111 on 23/6/2018.
 */

public interface IgdbApi {
    @Headers({
            "user-key: 47d12ad6dbaa4f02256aed6925edf4be",
            "Accept: application/json"
    })
    @GET("/games/")
    Call<List<Game>> searchGames(@QueryMap Map<String, String> map);

    @Headers({
            "user-key: 47d12ad6dbaa4f02256aed6925edf4be",
            "Accept: application/json"
    })
    @GET("/games/{id}/")
    Call<List<GameDetail>> getGamesDetail(@Path("id") String id, @QueryMap Map<String, String> map);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api-endpoint.igdb.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
