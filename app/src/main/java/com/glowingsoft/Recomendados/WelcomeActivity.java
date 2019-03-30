package com.glowingsoft.Recomendados;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.glowingsoft.Recomendados.Buyer.Activities.LoginInActivity;

public class WelcomeActivity extends ParentClass implements View.OnClickListener {
    TextView sellerBtn;
    TextView buyerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_welcome);
        viewBinding();
    }

    private void viewBinding() {
        sellerBtn = findViewById(R.id.sellerBtn);
        buyerBtn = findViewById(R.id.buyerTv);
        sellerBtn.setOnClickListener(this);
        buyerBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sellerBtn:
                intent = new Intent(WelcomeActivity.this, LoginInActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
            case R.id.buyerTv:
                intent = new Intent(WelcomeActivity.this, LoginInActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
                break;
        }

    }
}
