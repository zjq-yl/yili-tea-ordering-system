package com.example.yl_app.ui.home;

import com.example.yl_app.database.DrinkDatabase;
import com.example.yl_app.utils.DatabaseHelper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Bundle;
import android.os.Handler;
import com.example.yl_app.database.DrinkEntity;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.yl_app.R;
import com.example.yl_app.adapters.BannerAdapter;
import com.example.yl_app.adapters.HotDrinkAdapter;
import com.example.yl_app.models.DrinkItem;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private DrinkDatabase db;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    // 外卖自取相关
    private LinearLayout llTakeout, llSelfPickup;
    private TextView tvTakeout, tvSelfPickup;
    private ImageView ivTakeoutCheck, ivSelfPickupCheck;
    private boolean isTakeout = false;

    // 爆款饮品相关
    private RecyclerView hotDrinksList;
    private HotDrinkAdapter adapter;

    // Banner轮播相关
    private ViewPager2 bannerViewPager;
    private LinearLayout bannerIndicator;
    private int currentPosition = 0;
    private Handler bannerHandler = new Handler(Looper.getMainLooper());
    private Runnable bannerRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db = DrinkDatabase.getInstance(getContext());

        // 初始化默认数据（在子线程中执行）
        new Thread(() -> {
            if (db.drinkDao().getAllDrinks().isEmpty()) {
                DatabaseHelper.initDefaultData(getContext());
            }
        }).start();

        initViews(view);
        setupBanner();
        setupTakeoutSelector();
        setupHotDrinks();

        return view;
    }

    private void initViews(View view) {
        llTakeout = view.findViewById(R.id.ll_takeout);
        llSelfPickup = view.findViewById(R.id.ll_selfpickup);
        tvTakeout = view.findViewById(R.id.tv_takeout);
        tvSelfPickup = view.findViewById(R.id.tv_selfpickup);
        ivTakeoutCheck = view.findViewById(R.id.iv_takeout_check);
        ivSelfPickupCheck = view.findViewById(R.id.iv_selfpickup_check);
        hotDrinksList = view.findViewById(R.id.hot_drinks_list);
        bannerViewPager = view.findViewById(R.id.banner_viewpager);
        bannerIndicator = view.findViewById(R.id.banner_indicator);
    }

    private void setupBanner() {
        List<Integer> bannerImages = new ArrayList<>();
        bannerImages.add(R.drawable.banner_1);
        bannerImages.add(R.drawable.banner_2);
        bannerImages.add(R.drawable.banner_3);

        BannerAdapter bannerAdapter = new BannerAdapter(bannerImages);
        bannerViewPager.setAdapter(bannerAdapter);
        addIndicatorDots(bannerImages.size());

        bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateIndicator(position);
                currentPosition = position;
            }
        });
        startAutoScroll();
    }

    private void addIndicatorDots(int count) {
        bannerIndicator.removeAllViews();
        for (int i = 0; i < count; i++) {
            View dot = new View(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(12, 12);
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);
            dot.setBackgroundResource(R.drawable.bg_category_indicator);
            dot.setAlpha(0.4f);
            bannerIndicator.addView(dot);
        }
        updateIndicator(0);
    }

    private void updateIndicator(int position) {
        for (int i = 0; i < bannerIndicator.getChildCount(); i++) {
            View dot = bannerIndicator.getChildAt(i);
            dot.setAlpha(i == position ? 1.0f : 0.4f);
        }
    }

    private void startAutoScroll() {
        bannerRunnable = new Runnable() {
            @Override
            public void run() {
                if (bannerViewPager.getAdapter() != null) {
                    int total = bannerViewPager.getAdapter().getItemCount();
                    if (total > 0) {
                        int next = (currentPosition + 1) % total;
                        bannerViewPager.setCurrentItem(next, true);
                        bannerHandler.postDelayed(this, 3000);
                    }
                }
            }
        };
        bannerHandler.postDelayed(bannerRunnable, 3000);
    }

    private void setupTakeoutSelector() {
        updateTakeoutUI(false);

        llTakeout.setOnClickListener(v -> {
            if (!isTakeout) {
                isTakeout = true;
                updateTakeoutUI(true);
                Toast.makeText(getContext(), "外卖模式", Toast.LENGTH_SHORT).show();
            }
        });

        llSelfPickup.setOnClickListener(v -> {
            if (isTakeout) {
                isTakeout = false;
                updateTakeoutUI(false);
                Toast.makeText(getContext(), "自取模式", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTakeoutUI(boolean takeout) {
        if (takeout) {
            llTakeout.setBackgroundResource(R.drawable.bg_takeout_normal);
            llSelfPickup.setBackgroundResource(R.drawable.bg_takeout_selected);
            tvTakeout.setTextColor(getResources().getColor(R.color.white));
            tvSelfPickup.setTextColor(getResources().getColor(R.color.purple_medium));
            ivTakeoutCheck.setImageResource(R.drawable.ic_scooter);
            ivSelfPickupCheck.setImageResource(R.drawable.ic_milk_tea);
        } else {
            llTakeout.setBackgroundResource(R.drawable.bg_takeout_selected);
            llSelfPickup.setBackgroundResource(R.drawable.bg_takeout_normal);
            tvTakeout.setTextColor(getResources().getColor(R.color.purple_medium));
            tvSelfPickup.setTextColor(getResources().getColor(R.color.white));
            ivTakeoutCheck.setImageResource(R.drawable.ic_milk_tea);
            ivSelfPickupCheck.setImageResource(R.drawable.ic_scooter);
        }
    }

    private void setupHotDrinks() {
        executor.execute(() -> {
            List<DrinkEntity> hotDrinksFromDb = db.drinkDao().getHotDrinks();
            List<DrinkItem> hotDrinks = new ArrayList<>();
            for (DrinkEntity entity : hotDrinksFromDb) {
                hotDrinks.add(new DrinkItem(
                        entity.getName(),
                        entity.getCategory(),
                        entity.getPrice(),
                        getDrawableResId(entity.getImageName()),
                        entity.getSlogan(),
                        entity.getTag()
                ));
            }
            getActivity().runOnUiThread(() -> {
                hotDrinksList.setLayoutManager(new GridLayoutManager(getContext(), 2));
                adapter = new HotDrinkAdapter(getContext(), hotDrinks);
                hotDrinksList.setAdapter(adapter);
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bannerHandler.removeCallbacks(bannerRunnable);
    }

    private int getDrawableResId(String imageName) {
        return getResources().getIdentifier(imageName, "drawable", getContext().getPackageName());
    }
}