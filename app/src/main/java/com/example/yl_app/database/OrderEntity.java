package com.example.yl_app.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class OrderEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String orderId;
    private String date;
    private String status;
    private double total;
    private String items;
    private String userId;  // 新增：下单用户ID

    public OrderEntity(String orderId, String date, String status, double total, String items) {
        this.orderId = orderId;
        this.date = date;
        this.status = status;
        this.total = total;
        this.items = items;
    }

    // Getter 和 Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}