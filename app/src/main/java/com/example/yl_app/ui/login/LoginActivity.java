package com.example.yl_app.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.yl_app.MainActivity;
import com.example.yl_app.R;
import com.example.yl_app.database.DrinkDatabase;
import com.example.yl_app.database.UserEntity;
import com.example.yl_app.ui.admin.AdminActivity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.content.SharedPreferences;
public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private DrinkDatabase db;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = DrinkDatabase.getInstance(this);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        // 初始化管理员账号（如果不存在）
        initAdminAccount();

        btnLogin.setOnClickListener(v -> login());
        btnRegister.setOnClickListener(v -> register());
    }

    private void initAdminAccount() {
        executor.execute(() -> {
            UserEntity admin = db.userDao().getUserByUsername("admin");
            if (admin == null) {
                db.userDao().insert(new UserEntity("admin", "123456", "admin"));
            }
        });
    }

    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        executor.execute(() -> {
            UserEntity user = db.userDao().login(username, password);
            runOnUiThread(() -> {
                if (user != null) {
                    // 保存当前登录用户
                    SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
                    prefs.edit().putString("username", user.getUsername()).apply();

                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                    if ("admin".equals(user.getRole())) {
                        startActivity(new Intent(this, AdminActivity.class));
                    } else {
                        startActivity(new Intent(this, MainActivity.class));
                    }
                    finish();
                } else {
                    Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void register() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 3) {
            Toast.makeText(this, "密码长度至少3位", Toast.LENGTH_SHORT).show();
            return;
        }

        executor.execute(() -> {
            UserEntity existingUser = db.userDao().getUserByUsername(username);
            runOnUiThread(() -> {
                if (existingUser != null) {
                    Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
                } else {
                    executor.execute(() -> {
                        db.userDao().insert(new UserEntity(username, password, "user"));
                        runOnUiThread(() -> {
                            Toast.makeText(this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                            etUsername.setText("");
                            etPassword.setText("");
                        });
                    });
                }
            });
        });
    }
}