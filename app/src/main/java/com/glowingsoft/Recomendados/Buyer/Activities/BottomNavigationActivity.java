package com.glowingsoft.Recomendados.Buyer.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.glowingsoft.Recomendados.Buyer.ChatFragment;
import com.glowingsoft.Recomendados.Buyer.Fragments.FavouriteFragment;
import com.glowingsoft.Recomendados.Buyer.Fragments.HomeFragment;
import com.glowingsoft.Recomendados.Buyer.Fragments.MoreFragment;
import com.glowingsoft.Recomendados.Buyer.Fragments.ProfileFragment;
import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.ParentClass;
import com.glowingsoft.Recomendados.R;

public class BottomNavigationActivity extends ParentClass implements BottomNavigationView.OnNavigationItemSelectedListener {
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        frameLayout = findViewById(R.id.container);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        try {
            if (getIntent().getExtras().getInt("type") == 2) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", 2);
                bundle.putString("conversation_id", getIntent().getExtras().getString("conversation_id"));
                Fragment fragment = new ChatFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment, R.id.container);
            }
        } catch (Exception a) {
            try {
                if (getIntent().getExtras().getInt("type") == 1) {
                    loadFragment(new ChatFragment(), R.id.container);
                }

            } catch (Exception e) {
                e.printStackTrace();
                loadFragment(new HomeFragment(), R.id.container);

            }
        }


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home:
                loadFragment(new HomeFragment(), R.id.container);
                return true;
            case R.id.favourite:
                loadFragment(new FavouriteFragment(), R.id.container);
                return true;
            case R.id.chat:
                loadFragment(new ChatFragment(), R.id.container);
                return true;
            case R.id.profile:
                loadFragment(new ProfileFragment(), R.id.container);
                return true;
            case R.id.more:
                loadFragment(new MoreFragment(), R.id.container);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            finish();
        } else {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }
}
