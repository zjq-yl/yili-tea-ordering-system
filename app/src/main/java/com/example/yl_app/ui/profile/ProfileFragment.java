package com.example.yl_app.ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import com.example.yl_app.ui.chat.ChatActivity;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import com.example.yl_app.ui.address.AddressActivity;
import com.example.yl_app.R;
import com.example.yl_app.ui.login.LoginActivity;
import com.example.yl_app.ui.about.AboutActivity;

public class ProfileFragment extends Fragment {

    private Button logoutBtn;
    private LinearLayout llCoupon, llAddress, llContact, llAbout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        logoutBtn = view.findViewById(R.id.logout_btn);
        llCoupon = view.findViewById(R.id.ll_coupon);
        llAddress = view.findViewById(R.id.ll_address);
        llContact = view.findViewById(R.id.ll_contact);
        llAbout = view.findViewById(R.id.ll_about);

        // 我的优惠券
        llCoupon.setOnClickListener(v -> showCouponDialog());

        // 联系客服
        llContact.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            startActivity(intent);
        });

        // 关于我们
        llAbout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AboutActivity.class);
            startActivity(intent);
        });

        // 收货地址管理
        llAddress.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddressActivity.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }

    private void showCouponDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_coupon, null);
        builder.setView(view);
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();

        // 领取八折券
        Button btnGet = view.findViewById(R.id.btn_get_coupon);
        TextView tvCouponStatus = view.findViewById(R.id.tv_coupon_status);

        // 检查是否已领取（使用SharedPreferences）
        android.content.SharedPreferences prefs = getContext().getSharedPreferences("coupon", 0);
        boolean hasCoupon = prefs.getBoolean("has_coupon", false);

        if (hasCoupon) {
            tvCouponStatus.setText("已领取 ✓");
            tvCouponStatus.setTextColor(getResources().getColor(R.color.purple_medium));
            btnGet.setEnabled(false);
            btnGet.setText("已领取");
        }

        btnGet.setOnClickListener(v -> {
            android.content.SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("has_coupon", true);
            editor.apply();

            tvCouponStatus.setText("已领取 ✓");
            tvCouponStatus.setTextColor(getResources().getColor(R.color.purple_medium));
            btnGet.setEnabled(false);
            btnGet.setText("已领取");

            Toast.makeText(getContext(), "领取成功！八折券已放入卡包", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}