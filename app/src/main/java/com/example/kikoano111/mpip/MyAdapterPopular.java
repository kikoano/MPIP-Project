package com.example.kikoano111.mpip;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.kikoano111.mpip.Api.Game;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by kikoano111 on 6/12/2017.
 */

public class MyAdapterPopular extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String INTENT_MOVIE = "POPULAR";


    public class MyHolder extends RecyclerView.ViewHolder {

        TextView movieName;
        ImageView imagePoster;
        TextView movieYear;
        String id;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            movieName = (TextView) itemView.findViewById(R.id.name2);
            imagePoster = (ImageView) itemView.findViewById(R.id.cover2);
            movieYear = (TextView) itemView.findViewById(R.id.year2);

        }

    }


    private Context context;
    private LayoutInflater inflater;
    List<Game> data; // init?

    // create constructor to innitilize context and data sent from MainActivity
    public MyAdapterPopular(Context context, List<Game> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item2, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        final Game current = data.get(position);
        myHolder.movieName.setText(current.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d yyyy");
        Date date =new Date((long)current.getDate());
        myHolder.movieYear.setText(sdf.format(date));
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        requestOptions.error(R.drawable.ic_launcher_background);

        if(current.getCover() != null) {
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(current.getCover().getUrl().replace("thumb","cover_big"))
                    .apply(options)
                    .into(myHolder.imagePoster);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GameDetailsActivity.class);
                intent.putExtra("id", current.getId());
                v.getContext().startActivity(intent);
            }
        });
       /* holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                data.remove(position);

                notifyDataSetChanged();
                MainActivity.database.movieDao().deleteById(current.getImdbID());
                return false;
            }
        });*/

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

}
