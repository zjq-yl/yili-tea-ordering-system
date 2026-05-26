package com.example.yl_app.ui.about;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.yl_app.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("关于我们");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}