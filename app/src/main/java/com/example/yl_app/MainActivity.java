package com.example.yl_app;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.yl_app.ui.home.HomeFragment;
import com.example.yl_app.ui.order.OrderFragment;
import com.example.yl_app.ui.profile.ProfileFragment;
import com.example.yl_app.ui.menu.MenuFragment;
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            selectedFragment = new HomeFragment();
        } else if (id == R.id.nav_menu) {
            selectedFragment = new MenuFragment();
        } else if (id == R.id.nav_order) {
            selectedFragment = new OrderFragment();
        } else if (id == R.id.nav_profile) {
            selectedFragment = new ProfileFragment();
        }

        return loadFragment(selectedFragment);
    }
}