package com.example.yl_app.ui.menu;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.example.yl_app.R;
import com.example.yl_app.models.CartItem;
import com.example.yl_app.utils.CartManager;

public class DrinkDetailDialog {

    public static void show(Context context, String name, double price, int imageRes) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_drink_detail, null);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);

        TextView tvName = view.findViewById(R.id.tv_drink_name);
        TextView tvPrice = view.findViewById(R.id.tv_drink_price);
        TextView tvQuantity = view.findViewById(R.id.tv_quantity);
        Button btnAdd = view.findViewById(R.id.btn_add_to_cart);
        TextView btnMinus = view.findViewById(R.id.btn_minus);
        TextView btnPlus = view.findViewById(R.id.btn_plus);

        tvName.setText(name);
        tvPrice.setText("¥" + price);

        // 甜度选择
        TextView sugarNormal = view.findViewById(R.id.sugar_normal);
        TextView sugarLess = view.findViewById(R.id.sugar_less);
        TextView sugarHalf = view.findViewById(R.id.sugar_half);
        // 冰量选择
        TextView iceNormal = view.findViewById(R.id.ice_normal);
        TextView iceLess = view.findViewById(R.id.ice_less);
        TextView iceNo = view.findViewById(R.id.ice_no);
        // 加料选择
        TextView toppingsNone = view.findViewById(R.id.toppings_none);
        TextView toppingsPearl = view.findViewById(R.id.toppings_pearl);
        TextView toppingsCoconut = view.findViewById(R.id.toppings_coconut);

        final String[] selectedSugar = {"正常"};
        final String[] selectedIce = {"正常冰"};
        final String[] selectedToppings = {"不加"};
        final int[] quantity = {1};

        // 甜度点击事件
        setSugarClick(sugarNormal, sugarLess, sugarHalf, selectedSugar);
        setIceClick(iceNormal, iceLess, iceNo, selectedIce);
        setToppingsClick(toppingsNone, toppingsPearl, toppingsCoconut, selectedToppings);

        // 数量加减
        btnMinus.setOnClickListener(v -> {
            if (quantity[0] > 1) {
                quantity[0]--;
                tvQuantity.setText(String.valueOf(quantity[0]));
            }
        });
        btnPlus.setOnClickListener(v -> {
            quantity[0]++;
            tvQuantity.setText(String.valueOf(quantity[0]));
        });

        // 加入购物车
        btnAdd.setOnClickListener(v -> {
            CartItem cartItem = new CartItem(name, price, imageRes, quantity[0], selectedSugar[0], selectedIce[0], selectedToppings[0]);
            CartManager.getInstance(context).addItem(cartItem);
            dialog.dismiss();
            android.widget.Toast.makeText(context, "已加入购物车", android.widget.Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    private static void setSugarClick(TextView normal, TextView less, TextView half, String[] selected) {
        normal.setBackgroundResource(R.drawable.bg_takeout_normal);
        normal.setTextColor(normal.getContext().getResources().getColor(R.color.white));
        normal.setOnClickListener(v -> {
            selected[0] = "正常";
            updateSelection(normal, less, half);
        });
        less.setOnClickListener(v -> {
            selected[0] = "少糖";
            updateSelection(less, normal, half);
        });
        half.setOnClickListener(v -> {
            selected[0] = "半糖";
            updateSelection(half, normal, less);
        });
        normal.callOnClick();
    }

    private static void setIceClick(TextView normal, TextView less, TextView no, String[] selected) {
        normal.setOnClickListener(v -> {
            selected[0] = "正常冰";
            updateSelection(normal, less, no);
        });
        less.setOnClickListener(v -> {
            selected[0] = "少冰";
            updateSelection(less, normal, no);
        });
        no.setOnClickListener(v -> {
            selected[0] = "去冰";
            updateSelection(no, normal, less);
        });
        normal.callOnClick();
    }

    private static void setToppingsClick(TextView none, TextView pearl, TextView coconut, String[] selected) {
        none.setOnClickListener(v -> {
            selected[0] = "不加";
            updateToppingsSelection(none, pearl, coconut);
        });
        pearl.setOnClickListener(v -> {
            selected[0] = "珍珠";
            updateToppingsSelection(pearl, none, coconut);
        });
        coconut.setOnClickListener(v -> {
            selected[0] = "椰果";
            updateToppingsSelection(coconut, none, pearl);
        });
        none.callOnClick();
    }

    private static void updateSelection(TextView selected, TextView other1, TextView other2) {
        selected.setBackgroundResource(R.drawable.bg_takeout_normal);
        selected.setTextColor(selected.getContext().getResources().getColor(R.color.white));
        other1.setBackgroundResource(R.drawable.bg_takeout_selected);
        other1.setTextColor(other1.getContext().getResources().getColor(R.color.purple_medium));
        other2.setBackgroundResource(R.drawable.bg_takeout_selected);
        other2.setTextColor(other2.getContext().getResources().getColor(R.color.purple_medium));
    }

    private static void updateToppingsSelection(TextView selected, TextView other1, TextView other2) {
        selected.setBackgroundResource(R.drawable.bg_takeout_normal);
        selected.setTextColor(selected.getContext().getResources().getColor(R.color.white));
        other1.setBackgroundResource(R.drawable.bg_takeout_selected);
        other1.setTextColor(other1.getContext().getResources().getColor(R.color.purple_medium));
        other2.setBackgroundResource(R.drawable.bg_takeout_selected);
        other2.setTextColor(other2.getContext().getResources().getColor(R.color.purple_medium));
    }
}