package com.example.yl_app.models;

public class DrinkItem {
    private String name;
    private String category;
    private double price;
    private int imageRes;
    private String tag;  // 新增：人气爆款/新品

    private String slogan;

    public DrinkItem(String name, String category, double price, int imageRes, String slogan, String tag) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageRes = imageRes;
        this.slogan = slogan;
        this.tag = tag;
    }

    public String getSlogan() { return slogan; }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getImageRes() { return imageRes; }
    public String getTag() { return tag; }
}