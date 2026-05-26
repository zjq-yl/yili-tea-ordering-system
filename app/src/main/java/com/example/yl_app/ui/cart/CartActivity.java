package com.example.yl_app.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yl_app.R;
import com.example.yl_app.models.CartItem;
import com.example.yl_app.ui.checkout.CheckoutActivity;
import com.example.yl_app.utils.CartManager;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartList;
    private TextView tvTotal;
    private Button btnCheckout;
    private CartAdapter adapter;
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("购物车");

        cartList = findViewById(R.id.cart_list);
        tvTotal = findViewById(R.id.tv_total);
        btnCheckout = findViewById(R.id.btn_checkout);

        cartList.setLayoutManager(new LinearLayoutManager(this));
        cartManager = CartManager.getInstance(this);

        loadCart();

        btnCheckout.setOnClickListener(v -> {
            if (cartManager.getItemCount() > 0) {
                Intent intent = new Intent(this, CheckoutActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadCart() {
        List<CartItem> items = cartManager.getCartItems();
        if (items == null || items.isEmpty()) {
            finish();
            return;
        }
        adapter = new CartAdapter(this, items, () -> {
            runOnUiThread(() -> {
                double total = cartManager.getTotalPrice();
                tvTotal.setText("¥" + total);
                if (cartManager.getItemCount() == 0) {
                    finish();
                } else {
                    loadCart(); // 刷新列表
                }
            });
        });
        cartList.setAdapter(adapter);
        tvTotal.setText("¥" + cartManager.getTotalPrice());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCart();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}