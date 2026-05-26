package com.example.yl_app.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {DrinkEntity.class, OrderEntity.class, UserEntity.class, AddressEntity.class},
        version = 10, exportSchema = false)
public abstract class DrinkDatabase extends RoomDatabase {
    private static DrinkDatabase instance;

    public abstract DrinkDao drinkDao();
    public abstract OrderDao orderDao();
    public abstract UserDao userDao();
    public abstract AddressDao addressDao();

    public static synchronized DrinkDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            DrinkDatabase.class, "drink_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}