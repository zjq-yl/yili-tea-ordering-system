package com.example.yl_app.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    void insert(OrderEntity order);

    @Update
    void update(OrderEntity order);

    @Delete
    void delete(OrderEntity order);

    @Query("SELECT * FROM orders ORDER BY date DESC")
    List<OrderEntity> getAllOrders();

    @Query("SELECT * FROM orders WHERE status = :status ORDER BY date DESC")
    List<OrderEntity> getOrdersByStatus(String status);

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY date DESC")
    List<OrderEntity> getOrdersByUser(String userId);

    @Query("SELECT * FROM orders WHERE userId = :userId AND status = :status ORDER BY date DESC")
    List<OrderEntity> getOrdersByUserAndStatus(String userId, String status);

    // 管理员专用
    @Query("SELECT * FROM orders ORDER BY date DESC")
    List<OrderEntity> getAllOrdersForAdmin();

    @Query("SELECT * FROM orders WHERE status = :status ORDER BY date DESC")
    List<OrderEntity> getAllOrdersByStatusForAdmin(String status);
}