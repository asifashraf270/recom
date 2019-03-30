package com.glowingsoft.Recomendados;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.stetho.Stetho;
import com.glowingsoft.Recomendados.Buyer.Activities.BottomNavigationActivity;
import com.glowingsoft.Recomendados.Buyer.Activities.LoginInActivity;
import com.glowingsoft.Recomendados.Seller.ShopPreferencesActivity;

public class SplashActivity extends ParentClass {
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);


        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    if (GlobalClass.getInstance().returnUserId() != null) {
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
