package com.glowingsoft.Recomendados.Seller.ActivitiesSeller;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.glowingsoft.Recomendados.Buyer.ChatFragment;
import com.glowingsoft.Recomendados.Buyer.Fragments.MoreFragment;
import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.Seller.FragmentSeller.ProfileFragmentSeller;
import com.glowingsoft.Recomendados.Seller.FragmentSeller.SellerHomePageFragment;
import com.glowingsoft.Recomendados.Seller.FragmentSeller.SubscriptionFragment;
import com.glowingsoft.Recomendados.Seller.FragmentSeller.HomeSellerFragment;
import com.glowingsoft.Recomendados.WebReq.Urls;
import com.glowingsoft.Recomendados.WebReq.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class BottomNavigationSellerActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    FrameLayout frameLayout;
    RelativeLayout rootLayout;
    ProgressDialog progressDialog;
    Fragment mainFragment;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_seller);
        rootLayout = findViewById(R.id.rootLayout);
        frameLayout = findViewById(R.id.container);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        bottomNavigationView = findViewById(R.id.navigationSeller);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (GlobalClass.getInstance().isNetworkAvailable()) {
            RequestParams requestParams = new RequestParams();
            requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
            requestParams.put("shop_id", GlobalClass.getInstance().returnShopId());
            WebReq.post(Urls.sellerHome, requestParams, new SellerHomeRestApi());

        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, "" + getResources().getString(R.string.networkConnection), -1, -1);
        }

    }

    protected void loadFragment(Fragment fragment, int container) {
        getSupportFragmentManager().beginTransaction().replace(container, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            finish();
        } else {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.homeSeller:
                loadFragment(mainFragment, R.id.container);
                return true;
            case R.id.subSeller:
                loadFragment(new SubscriptionFragment(), R.id.container);
                return true;
            case R.id.chatSeller:
                loadFragment(new ChatFragment(), R.id.container);
                return true;
            case R.id.profileSeller:
                loadFragment(new ProfileFragmentSeller(), R.id.container);
                return true;
            case R.id.sellerMore:
                loadFragment(new MoreFragment(), R.id.container);
                return true;
        }
        return false;
    }

    public class SellerHomeRestApi extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            progressDialog.show();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                if (response.getInt("status") == 200) {
                    JSONArray jsonArrayProduct = response.getJSONArray("products");
                    if (jsonArrayProduct.length() > 0) {
                        mainFragment = new SellerHomePageFragment();

                    } else {
                        mainFragment = new HomeSellerFragment();
                    }
                    loadFragment(mainFragment, R.id.container);
                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, "" + response.getString("message"), -1, -1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            progressDialog.dismiss();
            GlobalClass.getInstance().SnackBar(rootLayout, "" + responseString, -1, -1);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            progressDialog.dismiss();
        }
    }
}
