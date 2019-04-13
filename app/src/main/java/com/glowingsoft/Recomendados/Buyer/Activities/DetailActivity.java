package com.glowingsoft.Recomendados.Buyer.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.Recomendados.Buyer.Adapter.DetailPagerAdaper;
import com.glowingsoft.Recomendados.Buyer.Adapter.ItemRecyclerViewAdapter;
import com.glowingsoft.Recomendados.Buyer.Models.DetailsModelClass;
import com.glowingsoft.Recomendados.Buyer.Models.HomeModelClass;
import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.Seller.Chat.UserChatMessages;
import com.glowingsoft.Recomendados.WebReq.Urls;
import com.glowingsoft.Recomendados.WebReq.WebReq;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ViewPager viewPager;
    DetailPagerAdaper adaper;
    TabLayout tabLayout;
    RecyclerView recyclerView;
    ItemRecyclerViewAdapter recyclerViewAdapter;
    ProgressDialog progressDialog;
    RelativeLayout rootLayout;
    RequestParams requestParams;
    TextView viewShopTv;
    TextView priceTv, locationTv, descriptionTv;
    TextView nameTv, ownerNameTv;
    CircleImageView ownerIv;
    List<DetailsModelClass> detailsModelClasses;
    List<HomeModelClass> recyclerViewMoreShop;
    String shopId;
    ImageView chatIV, phoneIv;
    String productNamePrice = null, ownerPhoneNumber;
    PermissionListener permissionlistener;
    String convId = null;
    int currentPage = 0;
    Handler handler;
    Runnable Update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        viewBinding();
    }

    private void viewBinding() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handler = new Handler();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backsecond);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        priceTv = findViewById(R.id.priceTv);
        viewPager = findViewById(R.id.viewPager);
        recyclerView = findViewById(R.id.recyclerviewItems);
        chatIV = findViewById(R.id.chatIv);
        phoneIv = findViewById(R.id.phoneIv);
        phoneIv.setOnClickListener(this);
        chatIV.setOnClickListener(this);
        recyclerViewMoreShop = new ArrayList<>();
        recyclerViewAdapter = new ItemRecyclerViewAdapter(recyclerViewMoreShop, DetailActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(recyclerViewAdapter);
        viewShopTv = findViewById(R.id.viewShopTv);
        viewShopTv.setOnClickListener(this);
        nameTv = findViewById(R.id.nameTv);
        locationTv = findViewById(R.id.locationTv);
        descriptionTv = findViewById(R.id.descriptionTv);
        ownerIv = findViewById(R.id.ownerIv);
        ownerNameTv = findViewById(R.id.nameOwnerTv);

        recyclerView.setHasFixedSize(true);
        detailsModelClasses = new ArrayList<>();
        adaper = new DetailPagerAdaper(this, detailsModelClasses);
        viewPager.setAdapter(adaper);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        tabLayout = findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager, true);
        rootLayout = findViewById(R.id.rootLayout);
        requestParams = new RequestParams();
        requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
        requestParams.put("product_id", getIntent().getExtras().getString("product_id"));
        if (GlobalClass.getInstance().isNetworkAvailable()) {
            detailsModelClasses.clear();
            WebReq.post(Urls.detail, requestParams, new ProductProfileRestApi());
        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
        }
        permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:" + ownerPhoneNumber));
                startActivity(call);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(DetailActivity.this, "Permission is required for call", Toast.LENGTH_SHORT).show();
            }


        };

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewShopTv:
                Intent intent = new Intent(this, ShopActivity.class);
                intent.putExtra("shop_id", shopId);
                startActivity(intent);

                break;

            case R.id.chatIv:
                Intent chatIntent = new Intent(DetailActivity.this, UserChatMessages.class);
                chatIntent.putExtra("conversation_id", convId);
                chatIntent.putExtra("user_id", GlobalClass.getInstance().returnUserId());
                startActivity(chatIntent);
                break;
            case R.id.phoneIv:
                TedPermission.with(this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.CALL_PHONE)
                        .check();

                break;

        }
    }

    public class ProductProfileRestApi extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            progressDialog.show();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);

            try {
                Log.d("response", response.toString());
                if (response.getInt("status") == 200) {
                    JSONObject jsonObject = response.getJSONObject("product");
                    productNamePrice = jsonObject.getString("title") + "\n" + jsonObject.getString("price");
                    ownerNameTv.setText("" + jsonObject.getString("owner_name"));
                    Picasso.get()
                            .load(jsonObject.getString("owner_image"))
                            .fit()
                            .placeholder(R.drawable.placeholderviewplager)
                            .into(ownerIv, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                    JSONObject jsonObjectShop = response.getJSONObject("shop");
                    String conversitional_id = jsonObjectShop.getString("conversation_id");
                    if (conversitional_id.equals("0")) {
                        chatIV.setVisibility(View.INVISIBLE);
                    }
                    convId = conversitional_id;
                    locationTv.setText("" + jsonObjectShop.getJSONObject("location").getString("address"));
                    ownerPhoneNumber = jsonObjectShop.getJSONObject("owner").getString("phone");
                    nameTv.setText("" + jsonObjectShop.getString("name"));
                    shopId = jsonObjectShop.getString("id");
                    Log.d("shopId", shopId);
                    descriptionTv.setText("" + response.getJSONObject("product").getString("description"));
                    JSONArray attachments = response.getJSONObject("product").getJSONArray("attachments");
                    for (int i = 0; i < attachments.length(); i++) {
                        DetailsModelClass modelClass = new DetailsModelClass();
                        modelClass.setId("" + attachments.getJSONObject(i).getString("id"));
                        modelClass.setImageUrl("" + attachments.getJSONObject(i).getString("image"));
                        modelClass.setProduct_id("" + attachments.getJSONObject(i).getString("subject_id"));
                        modelClass.setSubject_type("" + attachments.getJSONObject(i).getString("subject_type"));
                        detailsModelClasses.add(modelClass);
                    }
                    if (detailsModelClasses.size() == 0) {
                        DetailsModelClass modelClass = new DetailsModelClass();
                        modelClass.setId("");
                        modelClass.setImageUrl("" + response.getJSONObject("product").getString("image"));
                        modelClass.setProduct_id("");
                        modelClass.setSubject_type("");
                        detailsModelClasses.add(modelClass);
                    }
                    adaper.notifyDataSetChanged();
                    Update = new Runnable() {
                        public void run() {
                            if (currentPage == detailsModelClasses.size()) {
                                currentPage = 0;
                            }
                            viewPager.setCurrentItem(currentPage++, true);
                        }
                    };

                    Timer timer = new Timer(); // This will create a new Thread
                    timer.schedule(new TimerTask() { // task to be scheduled
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 3000, 3000);
                    JSONArray jsonArrayProducts = response.getJSONObject("shop").getJSONArray("products");
                    Log.d("response", jsonArrayProducts.toString());

                    for (int i = 0; i < jsonArrayProducts.length(); i++) {
                        HomeModelClass modelClass1 = new HomeModelClass();
                        modelClass1.setId("" + jsonArrayProducts.getJSONObject(i).getString("id"));
                        modelClass1.setTitle("" + jsonArrayProducts.getJSONObject(i).getString("title"));
                        modelClass1.setImage("" + jsonArrayProducts.getJSONObject(i).getString("image"));
                        modelClass1.setCategory_id("" + jsonArrayProducts.getJSONObject(i).getString("category_id"));
                        modelClass1.setPrice("" + jsonArrayProducts.getJSONObject(i).getString("price"));
                        modelClass1.setActive("" + jsonArrayProducts.getJSONObject(i).getString("active"));
                        modelClass1.setBusiness_id("" + jsonArrayProducts.getJSONObject(i).getString("business_id"));
                        modelClass1.setCategory_title("" + jsonArrayProducts.getJSONObject(i).getString("category_title"));
                        modelClass1.setShop("" + jsonArrayProducts.getJSONObject(i).getString("shop"));
                        modelClass1.setOwner_name("" + jsonArrayProducts.getJSONObject(i).getString("owner_name"));
                        modelClass1.setOwner_id("" + jsonArrayProducts.getJSONObject(i).getString("owner_id"));
                        modelClass1.setOwner_name("" + jsonArrayProducts.getJSONObject(i).getString("owner_image"));
                        recyclerViewMoreShop.add(modelClass1);
                    }
                    recyclerViewAdapter.notifyDataSetChanged();

                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, response.getString("message"), -1, -1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("exception", e.getMessage());
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);

            Log.d("failer", throwable.getMessage());
            GlobalClass.getInstance().SnackBar(rootLayout, throwable.getMessage(), -1, -1);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.d("failer", errorResponse.toString());
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d("failer", responseString.toString());
        }

        @Override
        public void onFinish() {
            super.onFinish();
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
