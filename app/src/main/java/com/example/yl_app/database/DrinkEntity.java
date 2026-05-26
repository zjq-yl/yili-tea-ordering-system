package com.example.yl_app.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "drinks")
public class DrinkEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String category;
    private double price;
    private String imageName;
    private String tag;
    private boolean isHot;
    private String slogan;

    public DrinkEntity(String name, String category, double price, String imageName, String slogan, String tag, boolean isHot) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageName = imageName;
        this.slogan = slogan;
        this.tag = tag;
        this.isHot = isHot;
    }


    // Getter 和 Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
    public boolean isHot() { return isHot; }
    public void setHot(boolean hot) { isHot = hot; }
    public String getSlogan() { return slogan; }
    public void setSlogan(String slogan) { this.slogan = slogan; }
}