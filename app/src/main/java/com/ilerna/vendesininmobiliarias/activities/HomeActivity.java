package com.ilerna.vendesininmobiliarias.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.fragments.ChatsFragment;
import com.ilerna.vendesininmobiliarias.fragments.FiltersFragment;
import com.ilerna.vendesininmobiliarias.fragments.HomeFragment;
import com.ilerna.vendesininmobiliarias.fragments.ProfileFragment;
import com.ilerna.vendesininmobiliarias.providers.FirebaseAuthProvider;

public class HomeActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigation;
    Toolbar toolbar;
    FirebaseAuthProvider fap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(HomeFragment.newInstance("", ""));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fap = new FirebaseAuthProvider();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.itemLogout) logout();
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        fap.logout();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //clear all history
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.itemHome:
                        openFragment(HomeFragment.newInstance("", ""));
                        return true;
                    case R.id.itemFilters:
                        openFragment(FiltersFragment.newInstance("", ""));
                        return true;
                    case R.id.itemChats:
                        openFragment(ChatsFragment.newInstance("", ""));
                        return true;
                    case R.id.itemProfile:
                        openFragment(ProfileFragment.newInstance("", ""));
                        return true;
                }
                return false;
            };

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}