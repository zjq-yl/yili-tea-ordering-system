package com.example.yl_app.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface DrinkDao {
    @Insert
    void insert(DrinkEntity drink);

    @Update
    void update(DrinkEntity drink);

    @Delete
    void delete(DrinkEntity drink);

    @Query("SELECT * FROM drinks")
    List<DrinkEntity> getAllDrinks();

    @Query("SELECT * FROM drinks WHERE category = :category")
    List<DrinkEntity> getDrinksByCategory(String category);

    @Query("SELECT * FROM drinks WHERE isHot = 1")
    List<DrinkEntity> getHotDrinks();

    @Query("SELECT * FROM drinks WHERE name LIKE '%' || :keyword || '%'")
    List<DrinkEntity> searchDrinks(String keyword);
}