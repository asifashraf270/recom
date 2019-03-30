package com.glowingsoft.Recomendados.Seller.ActivitiesSeller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.glowingsoft.Recomendados.Buyer.Fragments.MoreFragment;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.Seller.FragmentSeller.HomeSellerFragment;
import com.glowingsoft.Recomendados.Seller.FragmentSeller.ProfileFragmentSeller;
import com.glowingsoft.Recomendados.Seller.FragmentSeller.SubscriptionFragment;

public class BottomNavigationSellerActivity extends AppCompatActivity {
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_seller);
        frameLayout = findViewById(R.id.container);
        loadFragment();
    }

    private void loadFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeSellerFragment()).commit();

    }
}
