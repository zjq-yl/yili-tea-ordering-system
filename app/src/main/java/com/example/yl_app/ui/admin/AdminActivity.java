package com.example.yl_app.ui.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yl_app.R;
import com.example.yl_app.database.DrinkDatabase;
import com.example.yl_app.database.DrinkEntity;
import com.example.yl_app.database.OrderEntity;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.widget.LinearLayout;

public class AdminActivity extends AppCompatActivity {

    private DrinkDatabase db;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    // 饮品管理
    private RecyclerView drinkList;
    private DrinkManageAdapter drinkAdapter;
    private List<DrinkEntity> drinkData = new ArrayList<>();

    // 订单管理
    private RecyclerView orderList;
    private OrderManageAdapter orderAdapter;
    private List<OrderEntity> orderData = new ArrayList<>();

    private TabLayout tabLayout;
    private LinearLayout drinkPanel, orderPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        db = DrinkDatabase.getInstance(this);

        // 标题栏
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("管理后台");

        // 初始化控件
        tabLayout = findViewById(R.id.tab_layout);
        drinkPanel = findViewById(R.id.drink_panel);
        orderPanel = findViewById(R.id.order_panel);

        // 饮品列表
        drinkList = findViewById(R.id.admin_list);
        drinkList.setLayoutManager(new LinearLayoutManager(this));
        drinkAdapter = new DrinkManageAdapter(this, drinkData, this::showEditDialog, this::deleteDrink);
        drinkList.setAdapter(drinkAdapter);

        // 订单列表
        orderList = findViewById(R.id.order_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderManageAdapter(this, orderData, this::completeOrder);
        orderList.setAdapter(orderAdapter);

        // 添加饮品按钮
        findViewById(R.id.btn_add).setOnClickListener(v -> showAddDialog());

        // Tab 切换
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    drinkPanel.setVisibility(View.VISIBLE);
                    orderPanel.setVisibility(View.GONE);
                    loadDrinks();
                } else {
                    drinkPanel.setVisibility(View.GONE);
                    orderPanel.setVisibility(View.VISIBLE);
                    loadOrders();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // 默认加载饮品列表
        loadDrinks();
    }

    // 加载饮品
    private void loadDrinks() {
        executor.execute(() -> {
            drinkData.clear();
            drinkData.addAll(db.drinkDao().getAllDrinks());
            runOnUiThread(() -> drinkAdapter.notifyDataSetChanged());
        });
    }

    // 加载订单
    private void loadOrders() {
        executor.execute(() -> {
            orderData.clear();
            // 获取所有用户的订单（管理员用）
            orderData.addAll(db.orderDao().getAllOrdersForAdmin());
            runOnUiThread(() -> orderAdapter.notifyDataSetChanged());
        });
    }
    private void loadOrdersByStatus(String status) {
        executor.execute(() -> {
            orderData.clear();
            orderData.addAll(db.orderDao().getAllOrdersByStatusForAdmin(status));
            runOnUiThread(() -> orderAdapter.notifyDataSetChanged());
        });
    }

    // 完成订单（将状态改为已完成）
    private void completeOrder(OrderEntity order) {
        new AlertDialog.Builder(this)
                .setTitle("确认完成")
                .setMessage("将订单 " + order.getOrderId() + " 标记为已完成？")
                .setPositiveButton("确认", (dialog, which) -> {
                    order.setStatus("已完成");
                    executor.execute(() -> {
                        db.orderDao().update(order);
                        runOnUiThread(() -> {
                            loadOrders();
                            Toast.makeText(this, "订单已完成", Toast.LENGTH_SHORT).show();
                        });
                    });
                })
                .setNegativeButton("取消", null)
                .show();
    }

    // 添加饮品弹窗
    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_drink, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        EditText etName = view.findViewById(R.id.et_name);
        EditText etPrice = view.findViewById(R.id.et_price);
        Spinner spinnerCategory = view.findViewById(R.id.spinner_category);
        Spinner spinnerTag = view.findViewById(R.id.spinner_tag);
        CheckBox cbHot = view.findViewById(R.id.cb_hot);

        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btn_save).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "请填写完整", Toast.LENGTH_SHORT).show();
                return;
            }
            double price = Double.parseDouble(priceStr);
            String category = spinnerCategory.getSelectedItem().toString();
            String tag = spinnerTag.getSelectedItem().toString();
            if (tag.equals("无标签")) tag = "";
            boolean isHot = cbHot.isChecked();

            DrinkEntity drink = new DrinkEntity(name, category, price, "default", "", tag, isHot);
            executor.execute(() -> {
                db.drinkDao().insert(drink);
                runOnUiThread(() -> {
                    loadDrinks();
                    Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                });
            });
            dialog.dismiss();
        });
        dialog.show();
    }

    // 编辑饮品弹窗
    private void showEditDialog(DrinkEntity drink) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_drink, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        EditText etName = view.findViewById(R.id.et_name);
        EditText etPrice = view.findViewById(R.id.et_price);
        Spinner spinnerCategory = view.findViewById(R.id.spinner_category);
        Spinner spinnerTag = view.findViewById(R.id.spinner_tag);
        CheckBox cbHot = view.findViewById(R.id.cb_hot);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText("编辑饮品");

        etName.setText(drink.getName());
        etPrice.setText(String.valueOf(drink.getPrice()));

        String[] categories = getResources().getStringArray(R.array.categories);
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(drink.getCategory())) {
                spinnerCategory.setSelection(i);
                break;
            }
        }

        String[] tags = getResources().getStringArray(R.array.tags);
        String drinkTag = drink.getTag().isEmpty() ? "无标签" : drink.getTag();
        for (int i = 0; i < tags.length; i++) {
            if (tags[i].equals(drinkTag)) {
                spinnerTag.setSelection(i);
                break;
            }
        }
        cbHot.setChecked(drink.isHot());

        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btn_save).setOnClickListener(v -> {
            drink.setName(etName.getText().toString().trim());
            drink.setPrice(Double.parseDouble(etPrice.getText().toString().trim()));
            drink.setCategory(spinnerCategory.getSelectedItem().toString());
            String tag = spinnerTag.getSelectedItem().toString();
            drink.setTag(tag.equals("无标签") ? "" : tag);
            drink.setHot(cbHot.isChecked());

            executor.execute(() -> {
                db.drinkDao().update(drink);
                runOnUiThread(() -> {
                    loadDrinks();
                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                });
            });
            dialog.dismiss();
        });
        dialog.show();
    }

    // 删除饮品
    private void deleteDrink(DrinkEntity drink) {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("确定要删除 " + drink.getName() + " 吗？")
                .setPositiveButton("删除", (dialog, which) -> {
                    executor.execute(() -> {
                        db.drinkDao().delete(drink);
                        runOnUiThread(() -> {
                            loadDrinks();
                            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                        });
                    });
                })
                .setNegativeButton("取消", null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}