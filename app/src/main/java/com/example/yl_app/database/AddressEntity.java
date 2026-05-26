package com.example.yl_app.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "addresses")
public class AddressEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userId;      // 关联的用户名
    private String name;        // 收货人姓名
    private String phone;       // 联系电话
    private String address;     // 详细地址
    private boolean isDefault;  // 是否为默认地址

    public AddressEntity(String userId, String name, String phone, String address, boolean isDefault) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isDefault = isDefault;
    }

    // Getter 和 Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }
}