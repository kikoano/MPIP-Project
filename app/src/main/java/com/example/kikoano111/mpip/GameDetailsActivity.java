package com.example.kikoano111.mpip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.kikoano111.mpip.Api.GameDetail;

import at.markushi.ui.CircleButton;

public class GameDetailsActivity extends Activity {

    private static TextView textName;
    private static TextView textSummary;
    private static ImageView imgCover;
    private static CircleButton buttonAdd;
    private GameDetail game;
    private ResponseReceiverGameDetails receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);
        textName = findViewById(R.id.name3);
        textSummary = findViewById(R.id.summary);
        imgCover = findViewById(R.id.cover3);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.database.gameDao().insert(game);
                Log.e("aa", MainActivity.database.gameDao().fetchAllData().get(0).getName());
            }
        });

        Intent intent = new Intent(GameDetailsActivity.this, GetGameDetails.class);
        intent.putExtra("id", this.getIntent().getStringExtra("id"));
        startService(intent);

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_share:
                shareMovie();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareMovie() {
        Log.e("MainActivity","worksshare");
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, String.format("Check out this game!\n%s", game.getName()));
        startActivity(Intent.createChooser(intent, "Share via..."));
    }

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter(ResponseReceiverGameDetails.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiverGameDetails();
        registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }
    public class ResponseReceiverGameDetails extends BroadcastReceiver {
        public static final String ACTION_RESP = "RECEIVE_GAME_DETAILS";

        @Override
        public void onReceive(Context context, Intent intent) {
            game = (GameDetail)intent.getParcelableArrayListExtra("games").get(0);
            textName.setText(game.getName());
            textSummary.setText(game.getSummary());
            if(game.getCover()!=null) {
                String url = game.getCover().getUrl().replace("thumb", "720p");
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.mipmap.ic_launcher);
                requestOptions.error(R.drawable.ic_launcher_background);
                Glide.with(GameDetailsActivity.this)
                        .setDefaultRequestOptions(requestOptions)
                        .load(url)
                        .into(imgCover);
            }
        }
    }

}
