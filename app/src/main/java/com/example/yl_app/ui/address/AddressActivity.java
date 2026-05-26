package com.example.yl_app.ui.address;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yl_app.R;
import com.example.yl_app.database.AddressEntity;
import com.example.yl_app.database.DrinkDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddressActivity extends AppCompatActivity {

    private RecyclerView addressList;
    private AddressAdapter adapter;
    private List<AddressEntity> addressData = new ArrayList<>();
    private DrinkDatabase db;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        db = DrinkDatabase.getInstance(this);

        // 获取当前登录用户
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        currentUserId = prefs.getString("username", "user");

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addressList = findViewById(R.id.address_list);
        addressList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddressAdapter(this, addressData, this::setDefaultAddress, this::editAddress, this::deleteAddress);
        addressList.setAdapter(adapter);

        findViewById(R.id.btn_add_address).setOnClickListener(v -> showAddDialog(null));

        loadAddresses();
    }

    private void loadAddresses() {
        executor.execute(() -> {
            addressData.clear();
            addressData.addAll(db.addressDao().getAddressesByUser(currentUserId));
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        });
    }

    private void showAddDialog(AddressEntity existAddress) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_address, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        EditText etName = view.findViewById(R.id.et_name);
        EditText etPhone = view.findViewById(R.id.et_phone);
        EditText etAddress = view.findViewById(R.id.et_address);
        CheckBox cbDefault = view.findViewById(R.id.cb_default);

        if (existAddress != null) {
            dialogTitle.setText("编辑地址");
            etName.setText(existAddress.getName());
            etPhone.setText(existAddress.getPhone());
            etAddress.setText(existAddress.getAddress());
            cbDefault.setChecked(existAddress.isDefault());
        }

        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.btn_save).setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "请填写完整", Toast.LENGTH_SHORT).show();
                return;
            }

            if (existAddress != null) {
                // 编辑
                existAddress.setName(name);
                existAddress.setPhone(phone);
                existAddress.setAddress(address);
                if (cbDefault.isChecked()) {
                    setAsDefault(existAddress);
                } else {
                    executor.execute(() -> {
                        db.addressDao().update(existAddress);
                        runOnUiThread(() -> {
                            loadAddresses();
                            Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                        });
                    });
                }
            } else {
                // 新增
                AddressEntity newAddress = new AddressEntity(currentUserId, name, phone, address, cbDefault.isChecked());
                if (cbDefault.isChecked()) {
                    setAsDefault(newAddress);
                } else {
                    executor.execute(() -> {
                        db.addressDao().insert(newAddress);
                        runOnUiThread(() -> {
                            loadAddresses();
                            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                        });
                    });
                }
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    private void setDefaultAddress(AddressEntity address) {
        setAsDefault(address);
    }

    private void setAsDefault(AddressEntity address) {
        executor.execute(() -> {
            db.addressDao().clearDefault(currentUserId);
            address.setDefault(true);
            if (address.getId() == 0) {
                db.addressDao().insert(address);
            } else {
                db.addressDao().update(address);
            }
            runOnUiThread(() -> {
                loadAddresses();
                Toast.makeText(this, "已设为默认地址", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void editAddress(AddressEntity address) {
        showAddDialog(address);
    }

    private void deleteAddress(AddressEntity address) {
        new AlertDialog.Builder(this)
                .setTitle("确认删除")
                .setMessage("确定要删除这个地址吗？")
                .setPositiveButton("删除", (dialog, which) -> {
                    executor.execute(() -> {
                        db.addressDao().delete(address);
                        runOnUiThread(() -> {
                            loadAddresses();
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