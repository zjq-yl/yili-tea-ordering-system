package com.example.yl_app.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(UserEntity user);

    @Query("SELECT * FROM users WHERE username = :username")
    UserEntity getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    UserEntity login(String username, String password);

    @Query("SELECT * FROM users")
    List<UserEntity> getAllUsers();
}