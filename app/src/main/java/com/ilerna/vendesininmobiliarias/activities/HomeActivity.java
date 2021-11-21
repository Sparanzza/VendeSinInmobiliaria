package com.ilerna.vendesininmobiliarias.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.SearchView;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.Query;
import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.Utils.CategoriesEnum;
import com.ilerna.vendesininmobiliarias.adapters.PostsAdapter;
import com.ilerna.vendesininmobiliarias.fragments.ChatsFragment;
import com.ilerna.vendesininmobiliarias.fragments.FiltersFragment;
import com.ilerna.vendesininmobiliarias.fragments.HomeFragment;
import com.ilerna.vendesininmobiliarias.fragments.ProfileFragment;
import com.ilerna.vendesininmobiliarias.models.Post;
import com.ilerna.vendesininmobiliarias.providers.FirebaseAuthProvider;
import com.ilerna.vendesininmobiliarias.providers.PostsProvider;
import com.ilerna.vendesininmobiliarias.providers.TokensProvider;
import com.ilerna.vendesininmobiliarias.providers.UsersProvider;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    Toolbar toolbar;
    FirebaseAuthProvider fap;
    PostsAdapter postsAdapter;
    PostsProvider pp;
    TokensProvider tp;
    UsersProvider up;

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
        pp = new PostsProvider();
        tp = new TokensProvider();
        up = new UsersProvider();

        createToken();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Toast.makeText(HomeActivity.this, "Expand", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Toast.makeText(HomeActivity.this, "Collapse", Toast.LENGTH_SHORT).show();
                openFragment(HomeFragment.newInstance("ALL", ""));
                return true;
            }
        };

        menu.findItem(R.id.itemSearch).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView = (SearchView) menu.findItem(R.id.itemSearch).getActionView();
        searchView.setQueryHint("Search post here ...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //use this action
                // Toast.makeText(HomeActivity.this, query, Toast.LENGTH_SHORT).show();
                openFragment(HomeFragment.newInstance("ALL", query));

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemLogout) logout();
        // if (item.getItemId() == R.id.itemSearch) search();
        return super.onOptionsItemSelected(item);
    }

    private void search() {
    }


    @Override
    protected void onStart() {
        super.onStart();
        // update online user status
        up.updateChatOnline(true, fap.getCurrentUid());
    }

    @Override
    protected void onStop() {
        super.onStop();
        up.updateChatOnline(false, fap.getCurrentUid());
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
                        openFragment(HomeFragment.newInstance("ALL", ""));
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

    private void createToken() {
        tp.createToken(fap.getCurrentUid());
    }
}