package com.example.kikoano111.mpip;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.kikoano111.mpip.Api.Game;
import com.example.kikoano111.mpip.Api.IgdbApi;
import com.example.kikoano111.mpip.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetTopUpcoming extends IntentService {

    public GetTopUpcoming() {
        super("GetTopUpcoming");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        IgdbApi igdbApi = IgdbApi.retrofit.create(IgdbApi.class);
        Map<String, String> map = new HashMap<>();
        map.put("fields", "id,name,cover.url,first_release_date");
        map.put("limit", "10");
        map.put("filter[popularity][gte]", "10");
        map.put("order", "release_dates.date:desc:min&limit=5");
        final Call<List<Game>> call = igdbApi.searchGames(map);
        call.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {

                if(response.isSuccessful() && response.body().size() != 0 &&  response.body() != null) {

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(MainActivity.ResponseReceiverUpcoming.ACTION_RESP);
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