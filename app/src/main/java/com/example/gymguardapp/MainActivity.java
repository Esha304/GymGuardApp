package com.example.gymguardapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Toggle for the navigation drawer icon
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set item click listener for the NavigationView
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle item clicks here
                switch (item.getItemId()) {
                    case R.id.menu_membership_plans:
                        // Launch the Membership Plans activity
                        // Replace 'MembershipPlansActivity.class' with the actual class name
                        Toast.makeText(MainActivity.this, "Membership Plans", Toast.LENGTH_SHORT).show();
                        Intent membershipPlansIntent = new Intent(MainActivity.this, MembershipPlansActivity.class);
                        startActivity(membershipPlansIntent);
                        break;
                    case R.id.menu_gym_access:
                        // Launch the Gym Access activity
                        // Replace 'GymAccessActivity.class' with the actual class name
                        Toast.makeText(MainActivity.this, "Gym Access", Toast.LENGTH_SHORT).show();
                        // Start the Gym Access activity
                        Intent gymAccessIntent = new Intent(MainActivity.this, GymAccessActivity.class);
                        startActivity(gymAccessIntent);
                        break;
                }

                // Close the drawer after an item is selected
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }
}