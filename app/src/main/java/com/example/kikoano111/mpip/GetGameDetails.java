package com.example.kikoano111.mpip;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.kikoano111.mpip.Api.Game;
import com.example.kikoano111.mpip.Api.GameDetail;
import com.example.kikoano111.mpip.Api.IgdbApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetGameDetails extends IntentService {

    public GetGameDetails() {
        super("GetGameDetails");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        IgdbApi igdbApi = IgdbApi.retrofit.create(IgdbApi.class);
        Map<String, String> map = new HashMap<>();
        map.put("fields", "id,name,cover.url,first_release_date,summary");
        final Call<List<GameDetail>> call = igdbApi.getGamesDetail(intent.getStringExtra("id"),map);
        call.enqueue(new Callback<List<GameDetail>>() {
            @Override
            public void onResponse(Call<List<GameDetail>> call, Response<List<GameDetail>> response) {
                String error="empty";
                try{
                    error=response.errorBody().string();
                }catch (Exception e){

                }
                Log.e("MainActivity",error);
                Log.e("MainActivity",Boolean.toString(response.isSuccessful()));
                if(response.isSuccessful() &&  response.body() != null) {
                    Log.e("MainActivity",response.body().get(0).getName());
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction(GameDetailsActivity.ResponseReceiverGameDetails.ACTION_RESP);
                    broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    broadcastIntent.putParcelableArrayListExtra("games", (ArrayList) response.body());
                    sendBroadcast(broadcastIntent);
                }
                Toast.makeText(getApplicationContext(),"No game found!",Toast.LENGTH_SHORT);
            }
            @Override
            public void onFailure(Call<List<GameDetail>> call, Throwable t) {
                Log.e("MainActivity",t.getMessage());
            }
        });
    }

}