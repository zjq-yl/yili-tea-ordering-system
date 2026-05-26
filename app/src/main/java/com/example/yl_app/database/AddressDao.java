package com.example.yl_app.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface AddressDao {
    @Insert
    void insert(AddressEntity address);

    @Update
    void update(AddressEntity address);

    @Delete
    void delete(AddressEntity address);

    @Query("SELECT * FROM addresses WHERE userId = :userId ORDER BY isDefault DESC, id DESC")
    List<AddressEntity> getAddressesByUser(String userId);

    @Query("SELECT * FROM addresses WHERE userId = :userId AND isDefault = 1")
    AddressEntity getDefaultAddress(String userId);

    @Query("UPDATE addresses SET isDefault = 0 WHERE userId = :userId")
    void clearDefault(String userId);
}