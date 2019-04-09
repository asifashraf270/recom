package com.glowingsoft.Recomendados;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.stetho.Stetho;
import com.glowingsoft.Recomendados.Buyer.Activities.BottomNavigationActivity;
import com.glowingsoft.Recomendados.Buyer.Activities.LoginInActivity;
import com.glowingsoft.Recomendados.Seller.ActivitiesSeller.BottomNavigationSellerActivity;
import com.glowingsoft.Recomendados.Seller.ShopPreferencesActivity;

import me.leolin.shortcutbadger.ShortcutBadger;

public class SplashActivity extends ParentClass {
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);

        if (GlobalClass.getInstance().returnCount() != -1) {
            ShortcutBadger.applyCount(SplashActivity.this, GlobalClass.getInstance().returnCount());
        }


        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    if (GlobalClass.getInstance().returnUserId() != null) {

//                        if (GlobalClass.getInstance().returnShopId().length() > 0) {
//                            intent = new Intent(SplashActivity.this, BottomNavigationSellerActivity.class);
//                            startActivity(intent);
//                        } else {
//                            intent = new Intent(SplashActivity.this, ShopPreferencesActivity.class);
//                            startActivity(intent);
//                        }
                        intent = new Intent(SplashActivity.this, BottomNavigationActivity.class);
                        startActivity(intent);
                    } else {
                        intent = new Intent(SplashActivity.this, LoginInActivity.class);
                        startActivity(intent);
                    }
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }
}
