package com.example.ebook_reader.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.ebook_reader.R;
import com.example.ebook_reader.data.database.AppDatabase;
import com.example.ebook_reader.data.entity.User;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawerLayout;
    private NavController navController;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        int theme = prefs.getInt("theme", 0);
        AppCompatDelegate.setDefaultNightMode(theme == 0 ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment == null) {
            android.util.Log.e("MainActivity", "NavHostFragment not found");
            return;
        }
        navController = navHostFragment.getNavController();

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_ebook_list, R.id.nav_history, R.id.nav_profile)
                .setOpenableLayout(drawerLayout)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.nav_auth) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });

        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(item -> {
            prefs.edit().remove("logged_in_user_id").apply();
            navController.navigate(R.id.nav_auth, null, new NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph, true)
                    .build());
            drawerLayout.closeDrawers();
            updateNavHeader(null);
            return true;
        });

        if (prefs.contains("logged_in_user_id")) {
            try {
                navController.navigate(R.id.nav_ebook_list);
                String uid = prefs.getString("logged_in_user_id", null);
                if (uid != null) {
                    new Thread(() -> {
                        User user = AppDatabase.getInstance(this).userDao().getUserById(uid);
                        runOnUiThread(() -> updateNavHeader(user));
                    }).start();
                }
            } catch (Exception e) {
                android.util.Log.e("Navigation", "Lỗi khi điều hướng đến ebook list", e);
            }
        }
    }

    private void updateNavHeader(User user) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderName = headerView.findViewById(R.id.nav_header_name);
        TextView navHeaderEmail = headerView.findViewById(R.id.nav_header_email);

        if (user != null) {
            navHeaderName.setText(user.name);
            navHeaderEmail.setText(user.email);
        } else {
            navHeaderName.setText("Người dùng");
            navHeaderEmail.setText("email@example.com");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}