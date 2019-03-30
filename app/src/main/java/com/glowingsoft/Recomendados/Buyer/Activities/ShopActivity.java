package com.glowingsoft.Recomendados.Buyer.Activities;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.Recomendados.Buyer.Adapter.HomeFragmentAdapter;
import com.glowingsoft.Recomendados.Buyer.Models.HomeModelClass;
import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.WebReq.Urls;
import com.glowingsoft.Recomendados.WebReq.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShopActivity extends AppCompatActivity implements View.OnClickListener {
    GridView gridView;
    HomeFragmentAdapter adapter;
    List<HomeModelClass> modelClasses;
    ImageView backIv;
    CircleImageView profileIv;
    TextView locationTv, shoptTitleTv;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;
    RequestParams requestParams;
    RelativeLayout rootLayout;
    //    TagContainerLayout tagContainerLayout;
//    List<String> tags;
    ImageView chatIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        viewBinding();
    }

    private void viewBinding() {
        gridView = findViewById(R.id.gridView);
        modelClasses = new ArrayList<>();
        backIv = findViewById(R.id.backIv);
        chatIv = findViewById(R.id.chatIv);
        chatIv.setOnClickListener(this);
//        tagContainerLayout = findViewById(R.id.tags);
        profileIv = findViewById(R.id.profileIv);
        locationTv = findViewById(R.id.locationTv);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        rootLayout = findViewById(R.id.rootLayout);
        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        shoptTitleTv = findViewById(R.id.shoptTitleTv);
        backIv.setOnClickListener(this);
        adapter = new HomeFragmentAdapter(this, modelClasses);
        gridView.setAdapter(adapter);
        if (GlobalClass.getInstance().isNetworkAvailable()) {
            requestParams = new RequestParams();
            requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
            requestParams.put("shop_id", getIntent().getExtras().getString("shop_id"));
            Log.d("userId", GlobalClass.getInstance().returnUserId());
            Log.d("shopId", getIntent().getExtras().getString("shop_id"));
            WebReq.post(Urls.viewShop, requestParams, new ViewShopRestApi());

        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backIv:
                finish();
                break;
            case R.id.chatIv:
                Toast.makeText(this, "Development in Progress", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public class ViewShopRestApi extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                Log.d("Response", response.toString());
                if (response.getInt("status") == 200) {
                    locationTv.setText("" + response.getJSONObject("shop").getJSONObject("location").getString("address"));
                    JSONObject jsonObject = response.getJSONObject("shop");
                    shoptTitleTv.setText("" + jsonObject.getString("name"));
                    Picasso.get().load(jsonObject.getString("image")).fit().placeholder(R.drawable.placeholderviewplager).into(profileIv);
                    JSONArray jsonArrayProducts = jsonObject.getJSONArray("products");
//                    tags = new ArrayList<>();
//                    JSONArray jsonArray = jsonObject.getJSONArray("tags");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                        tags.add("" + jsonObject1.getString("title"));
//                    }
//                    tagContainerLayout.setTags(tags);

                    for (int i = 0; i < jsonArrayProducts.length(); i++) {
                        HomeModelClass homeModelClass = new HomeModelClass();
                        JSONObject jsonObject1 = jsonArrayProducts.getJSONObject(i);
                        homeModelClass.setId(jsonObject1.getString("id"));
                        homeModelClass.setTitle(jsonObject1.getString("title"));
                        homeModelClass.setCategory_id(jsonObject1.getString("category_id"));
                        homeModelClass.setImage(jsonObject1.getString("image"));
                        homeModelClass.setPrice(jsonObject1.getString("price"));
                        homeModelClass.setActive(jsonObject1.getString("active"));
                        homeModelClass.setBusiness_id(jsonObject1.getString("business_id"));
                        homeModelClass.setCategory_title(jsonObject1.getString("category_title"));
                        homeModelClass.setShop(jsonObject1.getString("shop"));
                        homeModelClass.setOwner_name(jsonObject1.getString("owner_name"));
                        homeModelClass.setOwner_id(jsonObject1.getString("owner_id"));
                        homeModelClass.setOwner_image(jsonObject1.getString("owner_image"));
                        homeModelClass.setIs_favorite("" + jsonObject1.getString("is_favourite"));
                        modelClasses.add(homeModelClass);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, response.getString("message"), -1, -1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                GlobalClass.getInstance().SnackBar(rootLayout, e.getMessage(), -1, -1);
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            progressDialog.show();

        }

        @Override
        public void onFinish() {
            super.onFinish();
            progressDialog.dismiss();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            progressDialog.dismiss();
            GlobalClass.getInstance().SnackBar(rootLayout, throwable.getMessage(), -1, -1);
        }
    }
}
