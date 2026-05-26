package com.example.yl_app.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yl_app.R;
import com.example.yl_app.adapters.DrinkAdapter;
import com.example.yl_app.database.DrinkDatabase;
import com.example.yl_app.database.DrinkEntity;
import com.example.yl_app.models.DrinkItem;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.Intent;
import com.example.yl_app.ui.cart.CartActivity;
import android.widget.LinearLayout;
import android.widget.Button;
import com.example.yl_app.utils.CartManager;

public class MenuFragment extends Fragment {

    private TextView tvMilkTea, tvFruitTea, tvTopping, tvIceCream, tvPureTea;
    private FrameLayout contentContainer;
    private RecyclerView recyclerView;
    private DrinkAdapter adapter;
    private DrinkDatabase db;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private LinearLayout cartBar;
    private TextView cartCount, cartTotal;
    private Button btnCheckout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        db = DrinkDatabase.getInstance(getContext());

        initViews(view);
        setupCategoryClick();
        loadDrinksByCategory("奶茶");

        refreshCart();

        return view;
    }

    private void initViews(View view) {


        cartBar = view.findViewById(R.id.cart_bar);
        cartCount = view.findViewById(R.id.cart_count);
        cartTotal = view.findViewById(R.id.cart_total);
        btnCheckout = view.findViewById(R.id.btn_checkout);

        tvMilkTea = view.findViewById(R.id.category_milk_tea);
        tvFruitTea = view.findViewById(R.id.category_fruit_tea);
        tvTopping = view.findViewById(R.id.category_topping);
        tvIceCream = view.findViewById(R.id.category_icecream);
        tvPureTea = view.findViewById(R.id.category_pure_tea);
        contentContainer = view.findViewById(R.id.content_container);

        recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contentContainer.addView(recyclerView);
        // 购物车栏点击结算
        btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CartActivity.class);
            startActivity(intent);
        });

// 购物车图标点击
        cartBar.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CartActivity.class);
            startActivity(intent);
        });
    }

    private void setupCategoryClick() {
        tvMilkTea.setOnClickListener(v -> {
            updateCategorySelection(tvMilkTea);
            loadDrinksByCategory("奶茶");
        });
        tvFruitTea.setOnClickListener(v -> {
            updateCategorySelection(tvFruitTea);
            loadDrinksByCategory("果茶");
        });
        tvTopping.setOnClickListener(v -> {
            updateCategorySelection(tvTopping);
            loadDrinksByCategory("小料");
        });
        tvIceCream.setOnClickListener(v -> {
            updateCategorySelection(tvIceCream);
            loadDrinksByCategory("冰淇淋");
        });
        tvPureTea.setOnClickListener(v -> {
            updateCategorySelection(tvPureTea);
            loadDrinksByCategory("纯茶");
        });
    }

    private void updateCategorySelection(TextView selected) {
        TextView[] categories = {tvMilkTea, tvFruitTea, tvTopping, tvIceCream, tvPureTea};
        for (TextView tv : categories) {
            tv.setBackgroundResource(R.drawable.bg_category_normal);
            tv.setTextColor(getResources().getColor(R.color.gray_light));
        }
        selected.setBackgroundResource(R.drawable.bg_category_selected);
        selected.setTextColor(getResources().getColor(R.color.purple_dark));
    }

    private void loadDrinksByCategory(String category) {
        executor.execute(() -> {
            List<DrinkEntity> drinkEntities = db.drinkDao().getDrinksByCategory(category);
            List<DrinkItem> drinks = new ArrayList<>();
            for (DrinkEntity entity : drinkEntities) {
                drinks.add(new DrinkItem(
                        entity.getName(),
                        entity.getCategory(),
                        entity.getPrice(),
                        getDrawableResId(entity.getImageName()),
                        entity.getSlogan(),
                        entity.getTag()
                ));
            }
            getActivity().runOnUiThread(() -> {
                if (adapter == null) {
                    adapter = new DrinkAdapter(getContext(), drinks);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.updateData(drinks);
                    refreshCart();
                }
            });
        });
    }

    private int getDrawableResId(String imageName) {
        return getResources().getIdentifier(imageName, "drawable", getContext().getPackageName());
    }

    private void refreshCart() {
        int count = CartManager.getInstance(getContext()).getItemCount();
        double total = CartManager.getInstance(getContext()).getTotalPrice();
        if (count > 0) {
            cartBar.setVisibility(View.VISIBLE);
            cartCount.setText(String.valueOf(count));
            cartTotal.setText("¥" + total);
        } else {
            cartBar.setVisibility(View.GONE);
        }
    }
}