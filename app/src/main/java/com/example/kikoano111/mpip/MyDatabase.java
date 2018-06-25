package com.example.kikoano111.mpip;

/**
 * Created by kikoano111 on 6/12/2017.
 */

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.kikoano111.mpip.Api.GameDetail;

@Database(entities = {GameDetail.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract GameDetailDao gameDao();
}
