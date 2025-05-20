package com.example.ebook_reader.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ebook_reader.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // DrawerLayout and NavigationView
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // NavHostFragment & NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        // AppBarConfiguration - pass top-level destinations to avoid showing "Up" button
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_ebook_list, R.id.nav_history, R.id.nav_profile, R.id.nav_auth)
                .setDrawerLayout(drawerLayout)
                .build();

        // Setup ActionBar with NavController and DrawerLayout
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Setup NavigationView with NavController
        NavigationUI.setupWithNavController(navigationView, navController);

        // Handle logout menu click if you have such menu item with id nav_logout
        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(item -> {
            // Clear login info from SharedPreferences
            SharedPreferences prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE);
            prefs.edit().remove("logged_in_user_id").apply();

            // Navigate back to login fragment
            navController.navigate(R.id.nav_auth);

            // Close drawer if needed (optional)
            drawerLayout.closeDrawers();

            return true;
        });

        // Auto-navigate to login if no user is logged in
        SharedPreferences prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        if (!prefs.contains("logged_in_user_id")) {
            navController.navigate(R.id.nav_auth);
        }
    }

    // Needed to support Up button in toolbar
    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
