package com.example.kikoano111.mpip;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.bumptech.glide.request.RequestOptions;
import com.example.kikoano111.mpip.Api.Game;
import com.example.kikoano111.mpip.Api.GameDetail;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;

import java.util.List;

public class MainActivity extends Activity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener{

    public static MyDatabase database;
    private static SliderLayout slider;
    private static RecyclerView recyclerViewSearch;
    private static RecyclerView recyclerViewPopular;
    private static RecyclerView recyclerViewTopRated;
    private static RecyclerView recyclerViewFavorites;
    private static RecyclerView.Adapter mAdapterSearch;
    private static RecyclerView.Adapter mAdapterPopular;
    private static RecyclerView.Adapter mAdapterTopRated;
    private static RecyclerView.Adapter mAdapterFavorites;
    private ResponseReceiverSearch receiverSearch;
    private ResponseReceiverPopular receiverPopular;
    private ResponseReceiverTopRated receiverTopRated;
    private ResponseReceiverUpcoming receiverUpcoming;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "MyDatabase").allowMainThreadQueries().build();
        recyclerViewSearch = findViewById(R.id.search);
        recyclerViewPopular = findViewById(R.id.popular);
        recyclerViewTopRated = findViewById(R.id.topRated);
        recyclerViewFavorites = findViewById(R.id.myfba);
        slider = findViewById(R.id.slider);
        recyclerViewSearch.setVisibility(View.GONE);
        loadSQLData();
        Intent intent;
        intent = new Intent(getApplicationContext(), GetPopular.class);
        startService(intent);

        intent = new Intent(getApplicationContext(), GetTopRated.class);
        startService(intent);

        intent = new Intent(getApplicationContext(), GetTopUpcoming.class);
        startService(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchbar, menu);
        SearchView search = (SearchView) menu.findItem(R.id.searchGame).getActionView();
        MenuItem menuItem = menu.findItem(R.id.searchGame);
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                recyclerViewSearch.setVisibility(View.GONE);
                return true;
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplicationContext(), GetSearch.class);
                intent.putExtra("query", query);
                startService(intent);
                recyclerViewSearch.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                //if(newText.isEmpty())
                    //loadSQLData();
                return false;
            }


        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter(ResponseReceiverSearch.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiverSearch = new ResponseReceiverSearch();
        registerReceiver(receiverSearch, filter);

        filter = new IntentFilter(ResponseReceiverPopular.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiverPopular = new ResponseReceiverPopular();
        registerReceiver(receiverPopular, filter);

        filter = new IntentFilter(ResponseReceiverTopRated.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiverTopRated = new ResponseReceiverTopRated();
        registerReceiver(receiverTopRated, filter);

        filter = new IntentFilter(ResponseReceiverUpcoming.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiverUpcoming= new ResponseReceiverUpcoming();
        registerReceiver(receiverUpcoming, filter);
        loadSQLData();
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiverSearch);
        unregisterReceiver(receiverPopular);
        unregisterReceiver(receiverTopRated);
        unregisterReceiver(receiverUpcoming);
        super.onPause();
    }

    public class ResponseReceiverSearch extends BroadcastReceiver {
        public static final String ACTION_RESP = "RECEIVE_MOVIES";

        @Override
        public void onReceive(Context context, Intent intent) {
            List<Game> games = intent.getParcelableArrayListExtra("games");
            mAdapterSearch = new MyAdapter(MainActivity.this, games);
            recyclerViewSearch.setAdapter(mAdapterSearch);
            recyclerViewSearch.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        }
    }
    public class ResponseReceiverPopular extends BroadcastReceiver {
        public static final String ACTION_RESP = "RECEIVE_POPULAR";

        @Override
        public void onReceive(Context context, Intent intent) {
            List<Game> games = intent.getParcelableArrayListExtra("games");
            mAdapterPopular = new MyAdapterPopular(MainActivity.this, games);
            recyclerViewPopular.setAdapter(mAdapterPopular);
            LinearLayoutManager myLayoutManager = new LinearLayoutManager(MainActivity.this);
            myLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerViewPopular.setLayoutManager(myLayoutManager);
        }
    }
    public class ResponseReceiverTopRated extends BroadcastReceiver {
        public static final String ACTION_RESP = "RECEIVE_TOP_RATED";

        @Override
        public void onReceive(Context context, Intent intent) {
            List<Game> games = intent.getParcelableArrayListExtra("games");
            mAdapterTopRated = new MyAdapterPopular(MainActivity.this, games);
            recyclerViewTopRated.setAdapter(mAdapterTopRated);
            LinearLayoutManager myLayoutManager = new LinearLayoutManager(MainActivity.this);
            myLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerViewTopRated.setLayoutManager(myLayoutManager);
        }
    }
    public class ResponseReceiverUpcoming extends BroadcastReceiver {
        public static final String ACTION_RESP = "RECEIVE_UPCOMING";

        @Override
        public void onReceive(Context context, Intent intent) {

            List<Game> games = intent.getParcelableArrayListExtra("games");
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.centerCrop();
          for(Game game : games){
              if(game.getCover() == null)
                  continue;
              String url = game.getCover().getUrl().replace("thumb","720p");
              Log.e("MainActivity",url);
              TextSliderView sliderView = new TextSliderView(MainActivity.this);
              // initialize SliderLayout
              sliderView
                      .image(url)
                      .description(game.getName())
                      .setRequestOption(requestOptions)
                      .setBackgroundColor(Color.WHITE)
                      .setProgressBarVisible(true)
                      .setOnSliderClickListener(MainActivity.this);

              //add your extra information
              sliderView.bundle(new Bundle());
              sliderView.getBundle().putString("extra", game.getName());
              slider.addSlider(sliderView);
          }

            // set Slider Transition Animation
            // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
            slider.setPresetTransformer(SliderLayout.Transformer.Accordion);

            slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            slider.setCustomAnimation(new DescriptionAnimation());
            slider.setDuration(4000);
            slider.addOnPageChangeListener(MainActivity.this);
          }
        }
    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        slider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void loadSQLData(){
        List<GameDetail> games = database.gameDao().fetchAllData();
        mAdapterFavorites = new MyAdapterGameDetails(MainActivity.this, games);
        recyclerViewFavorites.setAdapter(mAdapterFavorites);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(MainActivity.this);
        myLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewFavorites.setLayoutManager(myLayoutManager);
    }
}
