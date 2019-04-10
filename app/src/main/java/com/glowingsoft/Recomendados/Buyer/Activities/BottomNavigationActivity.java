package com.glowingsoft.Recomendados.Buyer.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
                loadFragment(fragment, R.id.container, null);
            }
        } catch (Exception a) {
            try {
                if (getIntent().getExtras().getInt("type") == 1) {
                    loadFragment(new ChatFragment(), R.id.container, null);
                }

            } catch (Exception e) {
                e.printStackTrace();
                loadFragment(new HomeFragment(), R.id.container, "Home");

//                loadFragment(new HomeFragment(), R.id.container);

            }
        }


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home:
                loadFragment(new HomeFragment(), R.id.container, "Home");
                return true;
            case R.id.favourite:
                loadFragment(new FavouriteFragment(), R.id.container, null);
                return true;
            case R.id.chat:
                loadFragment(new ChatFragment(), R.id.container, null);
                return true;
            case R.id.profile:
                loadFragment(new ProfileFragment(), R.id.container, null);
                return true;
            case R.id.more:
                loadFragment(new MoreFragment(), R.id.container, null);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("Home");
        if (fragment instanceof HomeFragment && fragment.isVisible()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(BottomNavigationActivity.this);
            alertDialog.setIcon(R.drawable.warning);
            alertDialog.setTitle("Warning");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Are you sure to close this Application.");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    dialog.dismiss();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog1 = alertDialog.create();
            alertDialog1.show();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                loadFragment(new HomeFragment(), R.id.container, "Home");
            } else {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }


    }
}
