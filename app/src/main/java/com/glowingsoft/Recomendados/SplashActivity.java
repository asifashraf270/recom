package com.glowingsoft.Recomendados;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.glowingsoft.Recomendados.Buyer.Activities.BottomNavigationActivity;
import com.glowingsoft.Recomendados.Buyer.Activities.LoginInActivity;
import com.glowingsoft.Recomendados.Seller.ActivitiesSeller.BottomNavigationSellerActivity;
import com.glowingsoft.Recomendados.Seller.ShopPreferencesActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import me.leolin.shortcutbadger.ShortcutBadger;

public class SplashActivity extends ParentClass {
    Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        Log.d("token", token);
                        GlobalClass.getInstance().storeFcmToken(token);
                    }
                });


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
