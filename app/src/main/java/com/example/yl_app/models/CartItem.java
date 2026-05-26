package com.example.yl_app.models;

public class CartItem {
    private String name;
    private double price;
    private int imageRes;
    private int quantity;
    private String sugar;   // 甜度
    private String ice;     // 冰量
    private String toppings; // 加料

    public CartItem(String name, double price, int imageRes, int quantity, String sugar, String ice, String toppings) {
        this.name = name;
        this.price = price;
        this.imageRes = imageRes;
        this.quantity = quantity;
        this.sugar = sugar;
        this.ice = ice;
        this.toppings = toppings;
    }

    // Getter
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getImageRes() { return imageRes; }
    public int getQuantity() { return quantity; }
    public String getSugar() { return sugar; }
    public String getIce() { return ice; }
    public String getToppings() { return toppings; }
    public double getTotalPrice() { return price * quantity; }

    // Setter
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setSugar(String sugar) { this.sugar = sugar; }
    public void setIce(String ice) { this.ice = ice; }
    public void setToppings(String toppings) { this.toppings = toppings; }
}