package com.example.kikoano111.mpip;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.kikoano111.mpip.Api.GameDetail;

import java.util.List;

/**
 * Created by kikoano111 on 6/12/2017.
 */
@Dao
public interface GameDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<GameDetail> game);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GameDetail game);

    @Update
    void update(GameDetail game);

    @Delete
    void delete(GameDetail game);

    @Query("DELETE FROM GameDetail WHERE id = :id")
    void deleteById(String id);

    @Query("SELECT * FROM GameDetail ORDER BY name")
    List<GameDetail> fetchAllData();
}
