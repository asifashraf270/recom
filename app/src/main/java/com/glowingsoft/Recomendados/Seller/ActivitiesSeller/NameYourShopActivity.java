package com.glowingsoft.Recomendados.Seller.ActivitiesSeller;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.ParentClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.WebReq.Urls;
import com.glowingsoft.Recomendados.WebReq.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class NameYourShopActivity extends ParentClass implements View.OnClickListener {
    EditText shopNameEt;
    TextView checkavailabilyTv;
    String shopName;
    ProgressDialog progressDialog;
    RelativeLayout rootLayout;
    RequestParams requestParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_your_shop);
        viewBinding();
    }

    private void viewBinding() {
        shopNameEt = findViewById(R.id.shopNameEt);
        checkavailabilyTv = findViewById(R.id.checkavailabilyTv);
        checkavailabilyTv.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        rootLayout = findViewById(R.id.rootLayout);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkavailabilyTv:
                shopName = getValueFromEdittext(shopNameEt);
                if (shopName.length() > 0) {
                    if (GlobalClass.getInstance().isNetworkAvailable()) {
                        requestParams = new RequestParams();
                        requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
                        requestParams.put("shop_name", shopName);
                        WebReq.post(Urls.viewShop, requestParams, new ShopAvailibility());
                    } else {
                        GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
                    }
                } else {
                    shopNameEt.setError("Shop Name is Compulsory");
                }
                break;

        }

    }

    public class ShopAvailibility extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                if (response.getInt("status") == 200) {
                    GlobalClass.getInstance().SnackBar(rootLayout, response.getString("message"), -1, -1);
                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, response.getString("message"), -1, -1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            progressDialog.dismiss();
            GlobalClass.getInstance().SnackBar(rootLayout, throwable.getMessage(), -1, -1);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            progressDialog.dismiss();
        }

        @Override
        public void onStart() {
            super.onStart();
            progressDialog.show();
        }
    }

}
