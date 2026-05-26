package com.example.yl_app.models;

public class OrderItem {
    private String orderId;
    private String date;
    private String status;
    private double total;
    private String items;

    public OrderItem(String orderId, String date, String status, double total, String items) {
        this.orderId = orderId;
        this.date = date;
        this.status = status;
        this.total = total;
        this.items = items;
    }

    public String getOrderId() { return orderId; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
    public double getTotal() { return total; }
    public String getItems() { return items; }
}