package com.example.yl_app.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yl_app.R;
import com.example.yl_app.adapters.OrderAdapter;
import com.example.yl_app.database.DrinkDatabase;
import com.example.yl_app.database.OrderEntity;
import com.example.yl_app.models.OrderItem;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.SharedPreferences;
import android.content.Context;

public class OrderFragment extends Fragment {

    private RecyclerView orderList;
    private String currentUserId;
    private OrderAdapter adapter;
    private DrinkDatabase db;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Button btnOngoing, btnCompleted;
    private String currentStatus = "进行中";
    private List<OrderEntity> allOrders = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        SharedPreferences prefs = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        currentUserId = prefs.getString("username", "user");
        db = DrinkDatabase.getInstance(getContext());

        orderList = view.findViewById(R.id.order_list);
        btnOngoing = view.findViewById(R.id.btn_ongoing);
        btnCompleted = view.findViewById(R.id.btn_completed);

        orderList.setLayoutManager(new LinearLayoutManager(getContext()));

        updateButtonStyle();
        loadOrdersByStatus(currentStatus);

        btnOngoing.setOnClickListener(v -> {
            currentStatus = "进行中";
            updateButtonStyle();
            loadOrdersByStatus(currentStatus);
        });

        btnCompleted.setOnClickListener(v -> {
            currentStatus = "已完成";
            updateButtonStyle();
            loadOrdersByStatus(currentStatus);
        });

        return view;
    }

    private void updateButtonStyle() {
        if ("进行中".equals(currentStatus)) {
            btnOngoing.setBackgroundResource(R.drawable.bg_takeout_normal);
            btnOngoing.setTextColor(getResources().getColor(R.color.white));
            btnCompleted.setBackgroundResource(R.drawable.bg_takeout_selected);
            btnCompleted.setTextColor(getResources().getColor(R.color.purple_medium));
        } else {
            btnOngoing.setBackgroundResource(R.drawable.bg_takeout_selected);
            btnOngoing.setTextColor(getResources().getColor(R.color.purple_medium));
            btnCompleted.setBackgroundResource(R.drawable.bg_takeout_normal);
            btnCompleted.setTextColor(getResources().getColor(R.color.white));
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        loadOrdersByStatus(currentStatus);
    }

    private void loadOrdersByStatus(String status) {
        executor.execute(() -> {
            List<OrderEntity> orders = db.orderDao().getOrdersByUserAndStatus(currentUserId, status);
            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderEntity entity : orders) {
                orderItems.add(new OrderItem(
                        entity.getOrderId(),
                        entity.getDate(),
                        entity.getStatus(),
                        entity.getTotal(),
                        entity.getItems()
                ));
            }
            getActivity().runOnUiThread(() -> {
                adapter = new OrderAdapter(getContext(), orderItems);
                orderList.setAdapter(adapter);
            });
        });
    }

}