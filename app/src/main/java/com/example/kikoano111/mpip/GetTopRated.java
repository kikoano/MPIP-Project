package com.example.kikoano111.mpip;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.kikoano111.mpip.Api.Game;
import com.example.kikoano111.mpip.Api.IgdbApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetTopRated extends IntentService {

    public GetTopRated() {
        super("GetGetTopRated");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("MainActivity","works");
        IgdbApi igdbApi = IgdbApi.retrofit.create(IgdbApi.class);
        Map<String, String> map = new HashMap<>();
        map.put("filter[total_rating][gte]", "99");
        map.put("fields", "id,name,cover.url,first_release_date");
        map.put("limit", "20");
        map.put("order", "total_rating:dec");
        final Call<List<Game>> call = igdbApi.searchGames(map);
        call.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                if(response.isSuccessful() && response.body().size() != 0 &&  response.body() != null) {

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(MainActivity.ResponseReceiverTopRated.ACTION_RESP);
                    broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    broadcastIntent.putParcelableArrayListExtra("games", (ArrayList) response.body());
                    sendBroadcast(broadcastIntent);
                }
                Toast.makeText(getApplicationContext(),"No game found!",Toast.LENGTH_SHORT);
            }
            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {
                Log.e("MainActivity",t.getMessage());
            }
        });
    }

}