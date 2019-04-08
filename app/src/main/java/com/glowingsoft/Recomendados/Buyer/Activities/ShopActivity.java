package com.glowingsoft.Recomendados.Buyer.Activities;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tommykw.tagview.DataTransform;
import com.github.tommykw.tagview.TagView;
import com.glowingsoft.Recomendados.Buyer.Adapter.HomeFragmentAdapter;
import com.glowingsoft.Recomendados.Buyer.ChatFragment;
import com.glowingsoft.Recomendados.Buyer.Models.HomeModelClass;
import com.glowingsoft.Recomendados.Buyer.Models.TagsModel;
import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.WebReq.Urls;
import com.glowingsoft.Recomendados.WebReq.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
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
    LinearLayout tagsLayout, materialLayout;
    ImageView chatIv;
    TagView<TagsModel> tagsView, materialTags;
    List<TagsModel> tagsviewModel, materialTagsModel;


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
        tagsView = findViewById(R.id.tagView);
        materialTags = findViewById(R.id.materialsTag);
        tagsviewModel = new ArrayList<>();
        materialTagsModel = new ArrayList<>();
        chatIv.setOnClickListener(this);
        tagsLayout = findViewById(R.id.tagsLayout);
        materialLayout = findViewById(R.id.materialLayout);
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
                Intent intent = new Intent(ShopActivity.this, BottomNavigationActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
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
                    final JSONArray tagArray = jsonObject.getJSONArray("tags");
                    if (tagArray.length() > 0) {
                        for (int i = 0; i < tagArray.length(); i++) {
                            TagsModel tagsModel = new TagsModel();
                            tagsModel.setId("" + tagArray.getJSONObject(i).getString("id"));
                            tagsModel.setTitle("" + tagArray.getJSONObject(i).getString("title"));
                            tagsviewModel.add(tagsModel);
                        }
                        tagsView.setTags(tagsviewModel, new DataTransform<TagsModel>() {
                            @NotNull
                            @Override
                            public String transfer(TagsModel tagsModel) {
                                return tagsModel.getTitle();
                            }
                        });
                    } else {
                        tagsLayout.setVisibility(View.GONE);
                    }
                    JSONArray materialArray = jsonObject.getJSONArray("materials");
                    if (materialArray.length() > 0) {
                        for (int i = 0; i < materialArray.length(); i++) {
                            TagsModel tagsModel = new TagsModel();
                            tagsModel.setId("" + materialArray.getJSONObject(i).getString("id"));
                            tagsModel.setTitle("" + materialArray.getJSONObject(i).getString("title"));
                            materialTagsModel.add(tagsModel);
                        }
                        materialTags.setTags(materialTagsModel, new DataTransform<TagsModel>() {
                            @NotNull
                            @Override
                            public String transfer(TagsModel tagsModel) {
                                return tagsModel.getTitle();
                            }
                        });
                    } else {
                        materialLayout.setVisibility(View.GONE);
                    }

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
