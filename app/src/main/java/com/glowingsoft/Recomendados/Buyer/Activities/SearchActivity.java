package com.glowingsoft.Recomendados.Buyer.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    SearchView searchEt;
    GridView gridView;
    HomeFragmentAdapter adapter;
    List<HomeModelClass> homeModelClasses;
    HomeModelClass homeModelClass;
    RelativeLayout rootLayout;
    ProgressDialog progressDialog;
    TextView noRecordTv;
    ImageView backTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchEt = findViewById(R.id.searchEt);
        gridView = findViewById(R.id.gridView);
        backTv = findViewById(R.id.backTv);
        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchEt.setQueryHint("Search");
        searchEt.setIconifiedByDefault(true);
        searchEt.setFocusable(true);
        searchEt.setIconified(false);
        noRecordTv = findViewById(R.id.noRecordtv);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        homeModelClasses = new ArrayList<>();
        adapter = new HomeFragmentAdapter(this, homeModelClasses);
        gridView.setAdapter(adapter);


        rootLayout = findViewById(R.id.rootLayout);
        searchEt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (GlobalClass.getInstance().isNetworkAvailable()) {
                    homeModelClasses.clear();
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
                    requestParams.put("query", query);
                    WebReq.client.cancelAllRequests(true);
                    WebReq.post(Urls.buyerSearch, requestParams, new HomeRestApi());
                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (GlobalClass.getInstance().isNetworkAvailable()) {
                    homeModelClasses.clear();
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
                    requestParams.put("query", newText);
                    WebReq.client.cancelAllRequests(true);
                    WebReq.post(Urls.buyerSearch, requestParams, new HomeRestApi());
                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
                }
                return false;
            }
        });

    }

    public class HomeRestApi extends JsonHttpResponseHandler {
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
                    JSONArray jsonArray = response.getJSONArray("products");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        homeModelClass = new HomeModelClass();
                        homeModelClass.setId(jsonObject.getString("id"));
                        homeModelClass.setTitle(jsonObject.getString("title"));
                        homeModelClass.setCategory_id(jsonObject.getString("category_id"));
                        homeModelClass.setImage(jsonObject.getString("image"));
                        homeModelClass.setPrice(jsonObject.getString("price"));
                        homeModelClass.setActive(jsonObject.getString("active"));
                        homeModelClass.setBusiness_id(jsonObject.getString("business_id"));
                        homeModelClass.setCategory_title(jsonObject.getString("category_title"));
                        homeModelClass.setShop(jsonObject.getString("shop"));
                        homeModelClass.setOwner_name(jsonObject.getString("owner_name"));
                        homeModelClass.setOwner_id(jsonObject.getString("owner_id"));
                        homeModelClass.setOwner_image(jsonObject.getString("owner_image"));
                        homeModelClass.setIs_favorite("" + jsonObject.getString("is_favourite"));
                        homeModelClasses.add(homeModelClass);
                    }
                    if (homeModelClasses.size() == 0) {
                        noRecordTv.setVisibility(View.VISIBLE);
                    } else {
                        noRecordTv.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
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
            GlobalClass.getInstance().SnackBar(rootLayout, throwable.getMessage(), -1, -1);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            progressDialog.dismiss();
        }
    }


}
