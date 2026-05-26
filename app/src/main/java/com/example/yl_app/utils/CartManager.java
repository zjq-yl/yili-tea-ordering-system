package com.example.yl_app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.yl_app.models.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static final String PREF_NAME = "cart";
    private static final String KEY_CART = "cart_items";
    private static CartManager instance;
    private List<CartItem> cartItems;
    private SharedPreferences prefs;
    private Gson gson;

    private CartManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        loadCart();
    }

    public static synchronized CartManager getInstance(Context context) {
        if (instance == null) {
            instance = new CartManager(context.getApplicationContext());
        }
        return instance;
    }

    private void loadCart() {
        String json = prefs.getString(KEY_CART, "");
        if (json.isEmpty()) {
            cartItems = new ArrayList<>();
        } else {
            try {
                Type type = new TypeToken<List<CartItem>>(){}.getType();
                cartItems = gson.fromJson(json, type);
                if (cartItems == null) {
                    cartItems = new ArrayList<>();
                }
            } catch (Exception e) {
                cartItems = new ArrayList<>();
            }
        }
    }

    private void saveCart() {
        String json = gson.toJson(cartItems);
        prefs.edit().putString(KEY_CART, json).apply();
    }

    public void addItem(CartItem item) {
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        for (CartItem existing : cartItems) {
            if (existing.getName().equals(item.getName()) &&
                    existing.getSugar().equals(item.getSugar()) &&
                    existing.getIce().equals(item.getIce()) &&
                    existing.getToppings().equals(item.getToppings())) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                saveCart();
                return;
            }
        }
        cartItems.add(item);
        saveCart();
    }

    public void removeItem(int position) {
        if (cartItems != null && position >= 0 && position < cartItems.size()) {
            cartItems.remove(position);
            saveCart();
        }
    }

    public void updateQuantity(int position, int quantity) {
        if (cartItems != null && position >= 0 && position < cartItems.size()) {
            if (quantity <= 0) {
                cartItems.remove(position);
            } else {
                cartItems.get(position).setQuantity(quantity);
            }
            saveCart();
        }
    }

    public List<CartItem> getCartItems() {
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        return cartItems;
    }

    public void clearCart() {
        if (cartItems != null) {
            cartItems.clear();
            saveCart();
        }
    }

    public double getTotalPrice() {
        if (cartItems == null) return 0;
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public int getItemCount() {
        if (cartItems == null) return 0;
        int count = 0;
        for (CartItem item : cartItems) {
            count += item.getQuantity();
        }
        return count;
    }
}