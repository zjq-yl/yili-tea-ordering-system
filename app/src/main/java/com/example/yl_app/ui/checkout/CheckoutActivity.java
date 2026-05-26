package com.example.yl_app.ui.checkout;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yl_app.R;
import com.example.yl_app.database.AddressEntity;
import com.example.yl_app.database.DrinkDatabase;
import com.example.yl_app.database.OrderEntity;
import com.example.yl_app.models.CartItem;
import com.example.yl_app.ui.address.AddressActivity;
import com.example.yl_app.utils.CartManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tabSelf, tabDelivery;
    private LinearLayout addressSection, selfSection;
    private TextView tvAddressName, tvAddressDetail, btnChangeAddress;
    private TextView tvSubtotal, tvDelivery, tvTotal;
    private Button btnSubmit;
    private RadioGroup rgPayment;
    private RecyclerView checkoutList;
    private CartManager cartManager;
    private DrinkDatabase db;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private AddressEntity selectedAddress;
    private String currentUserId;
    private String orderType = "自取";  // 自取/外卖
    private double deliveryFee = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = DrinkDatabase.getInstance(this);
        cartManager = CartManager.getInstance(this);

        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        currentUserId = prefs.getString("username", "user");

        // 初始化控件
        tabSelf = findViewById(R.id.tab_self);
        tabDelivery = findViewById(R.id.tab_delivery);
        addressSection = findViewById(R.id.address_section);
        selfSection = findViewById(R.id.self_section);
        tvAddressName = findViewById(R.id.tv_address_name);
        tvAddressDetail = findViewById(R.id.tv_address_detail);
        btnChangeAddress = findViewById(R.id.btn_change_address);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvDelivery = findViewById(R.id.tv_delivery);
        tvTotal = findViewById(R.id.tv_total);
        btnSubmit = findViewById(R.id.btn_submit);
        rgPayment = findViewById(R.id.rg_payment);
        checkoutList = findViewById(R.id.checkout_list);

        checkoutList.setLayoutManager(new LinearLayoutManager(this));

        // 自取/外卖切换
        tabSelf.setOnClickListener(v -> {
            orderType = "自取";
            updateOrderTypeUI();
            loadAddress();
        });
        tabDelivery.setOnClickListener(v -> {
            orderType = "外卖";
            updateOrderTypeUI();
            loadAddress();
        });

        btnChangeAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddressActivity.class);
            startActivityForResult(intent, 100);
        });

        btnSubmit.setOnClickListener(v -> showQRCodeDialog());

        loadCart();
        loadAddress();
        updateOrderTypeUI();
    }

    private void updateOrderTypeUI() {
        if ("自取".equals(orderType)) {
            tabSelf.setBackgroundResource(R.drawable.bg_takeout_normal);
            tabSelf.setTextColor(getResources().getColor(R.color.white));
            tabDelivery.setBackgroundResource(R.drawable.bg_takeout_selected);
            tabDelivery.setTextColor(getResources().getColor(R.color.purple_medium));
            addressSection.setVisibility(View.GONE);
            selfSection.setVisibility(View.VISIBLE);
            deliveryFee = 0;
        } else {
            tabSelf.setBackgroundResource(R.drawable.bg_takeout_selected);
            tabSelf.setTextColor(getResources().getColor(R.color.purple_medium));
            tabDelivery.setBackgroundResource(R.drawable.bg_takeout_normal);
            tabDelivery.setTextColor(getResources().getColor(R.color.white));
            addressSection.setVisibility(View.VISIBLE);
            selfSection.setVisibility(View.GONE);
            deliveryFee = 5;
        }
        updateTotal();
    }

    private void loadAddress() {
        if (!"外卖".equals(orderType)) {
            return;
        }
        executor.execute(() -> {
            AddressEntity address = db.addressDao().getDefaultAddress(currentUserId);
            runOnUiThread(() -> {
                if (address != null) {
                    selectedAddress = address;
                    tvAddressName.setText(address.getName() + " " + address.getPhone());
                    tvAddressDetail.setText(address.getAddress());
                } else {
                    selectedAddress = null;
                    tvAddressName.setText("请添加收货地址");
                    tvAddressDetail.setText("");
                }
            });
        });
    }

    private void loadCart() {
        List<CartItem> items = cartManager.getCartItems();
        CheckoutAdapter adapter = new CheckoutAdapter(items);
        checkoutList.setAdapter(adapter);

        double subtotal = cartManager.getTotalPrice();
        tvSubtotal.setText("¥" + subtotal);
        updateTotal();
    }

    private void updateTotal() {
        double subtotal = cartManager.getTotalPrice();
        double total = subtotal + deliveryFee;
        tvDelivery.setText("¥" + deliveryFee);
        tvTotal.setText("¥" + total);
    }

    private void showQRCodeDialog() {
        if ("外卖".equals(orderType) && selectedAddress == null) {
            Toast.makeText(this, "请选择收货地址", Toast.LENGTH_SHORT).show();
            return;
        }

        List<CartItem> items = cartManager.getCartItems();
        if (items.isEmpty()) {
            Toast.makeText(this, "购物车是空的", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_qrcode, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();

        android.widget.ImageView ivQrCode = dialogView.findViewById(R.id.iv_qrcode);
        TextView tvAmount = dialogView.findViewById(R.id.tv_amount);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm_pay);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel_pay);

        double total = Double.parseDouble(tvTotal.getText().toString().replace("¥", ""));
        tvAmount.setText("¥" + total);

        // 生成假的二维码
        String qrData = "微信支付|艺黎奶茶铺|订单金额:" + total;
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(qrData, BarcodeFormat.QR_CODE, 300, 300);
            Bitmap bitmap = bitMatrixToBitmap(bitMatrix);
            ivQrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            submitOrder();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private Bitmap bitMatrixToBitmap(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[y * width + x] = matrix.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private void submitOrder() {
        List<CartItem> items = cartManager.getCartItems();
        if (items.isEmpty()) {
            Toast.makeText(this, "购物车是空的", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 构建商品清单
        StringBuilder itemsBuilder = new StringBuilder();
        for (CartItem item : items) {
            itemsBuilder.append(item.getName())
                    .append(" x").append(item.getQuantity())
                    .append(" (").append(item.getSugar()).append("/").append(item.getIce()).append("/+").append(item.getToppings()).append("), ");
        }
        String itemsStr = itemsBuilder.toString();
        if (itemsStr.endsWith(", ")) {
            itemsStr = itemsStr.substring(0, itemsStr.length() - 2);
        }

        int selectedId = rgPayment.getCheckedRadioButtonId();
        String payment = selectedId == R.id.rb_wechat ? "微信支付" : "支付宝";

        String orderId = "ORD" + System.currentTimeMillis();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        double total = Double.parseDouble(tvTotal.getText().toString().replace("¥", ""));

        String addressInfo = "";
        if ("外卖".equals(orderType) && selectedAddress != null) {
            addressInfo = " | 地址:" + selectedAddress.getAddress();
        }

        OrderEntity order = new OrderEntity(orderId, date, "进行中", total, itemsStr + " | " + orderType + addressInfo);
        order.setUserId(currentUserId);

        executor.execute(() -> {
            db.orderDao().insert(order);
            runOnUiThread(() -> {
                cartManager.clearCart();
                Toast.makeText(this, "订单提交成功！", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadAddress();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}