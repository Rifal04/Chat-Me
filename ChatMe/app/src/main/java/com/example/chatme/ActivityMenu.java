package com.example.chatme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ActivityMenu extends AppCompatActivity {
    private final FragmentProfile fragmentProfile = new FragmentProfile();

    private final FragmentMenu fragmentMenu = new FragmentMenu();

    private BottomNavigationView bottomNavigationView;
    private FrameLayout container;

    TextView user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        load_fragment(fragmentMenu);
        initview();

        SharedPreferences prefs = getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "");

        user.setText(username);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.menu_chat) {
                    load_fragment(fragmentMenu);
                } else if (itemId == R.id.menu_profile) {
                    load_fragment(fragmentProfile);
                }

                return true;
            }
        });
    }

    private void initview() {
        container = findViewById(R.id.container);
        bottomNavigationView = findViewById(R.id.nav);
        user = findViewById(R.id.txtId);
    }

    private void load_fragment (Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
