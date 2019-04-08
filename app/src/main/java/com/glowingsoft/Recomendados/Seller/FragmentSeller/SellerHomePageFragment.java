package com.glowingsoft.Recomendados.Seller.FragmentSeller;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.Seller.Adapters.SellerHomePageAdapter;
import com.glowingsoft.Recomendados.Seller.Models.HomeSellerModel;
import com.glowingsoft.Recomendados.WebReq.Urls;
import com.glowingsoft.Recomendados.WebReq.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerHomePageFragment extends Fragment implements View.OnClickListener {
    Toolbar toolbar;
    GridView gridView;
    SwipeRefreshLayout swipeRefreshLayout;
    List<HomeSellerModel> homeSellerModels;
    SellerHomePageAdapter adapter;
    RelativeLayout rootLayout;
    ProgressDialog progressDialog;
    ImageView addProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_home_page, container, false);
        viewBinding(view);
        return view;
    }

    private void viewBinding(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        gridView = view.findViewById(R.id.gridView);
        addProduct = view.findViewById(R.id.addProductTv);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.container, new HomeSellerFragment()).addToBackStack(null).commit();
            }
        });
        setHasOptionsMenu(true);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (GlobalClass.getInstance().isNetworkAvailable()) {
                    homeSellerModels.clear();
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
                    requestParams.put("shop_id", GlobalClass.getInstance().returnShopId());
                    WebReq.post(Urls.sellerHome, requestParams, new HomeSellerApi());
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
                }
            }
        });
        rootLayout = view.findViewById(R.id.rootLayout);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        homeSellerModels = new ArrayList<>();
        adapter = new SellerHomePageAdapter(getActivity(), homeSellerModels);
        gridView.setAdapter(adapter);
        if (GlobalClass.getInstance().isNetworkAvailable()) {
            RequestParams requestParams = new RequestParams();
            requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
            requestParams.put("shop_id", GlobalClass.getInstance().returnShopId());
            WebReq.post(Urls.sellerHome, requestParams, new HomeSellerApi());
        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
        }
    }

    @Override
    public void onClick(View v) {

    }

    public class HomeSellerApi extends JsonHttpResponseHandler {
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
                    for (int i = 0; i < jsonArrayProduct.length(); i++) {
                        HomeSellerModel homeSellerModel = new HomeSellerModel();
                        JSONObject jsonObject = jsonArrayProduct.getJSONObject(i);
                        homeSellerModel.setId("" + jsonObject.getString("id"));
                        homeSellerModel.setTitle("" + jsonObject.getString("title"));
                        homeSellerModel.setImage("" + jsonObject.getString("image"));
                        homeSellerModel.setCategory_id("" + jsonObject.getString("category_id"));
                        homeSellerModel.setPrice("" + jsonObject.getString("price"));
                        homeSellerModel.setDescription("" + jsonObject.getString("description"));
                        homeSellerModel.setBusiness_id("" + jsonObject.getString("business_id"));
                        homeSellerModel.setActive("" + jsonObject.getString("active"));
                        homeSellerModel.setCategory_title("" + jsonObject.getString("category_title"));
                        homeSellerModel.setShop("" + jsonObject.getString("shop"));
                        homeSellerModel.setOwner_name("" + jsonObject.getString("owner_name"));
                        homeSellerModel.setOwner_id("" + jsonObject.getString("owner_id"));
                        homeSellerModel.setOwner_image("" + jsonObject.getString("owner_image"));
                        homeSellerModels.add(homeSellerModel);


                    }
                    adapter.notifyDataSetChanged();
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
            GlobalClass.getInstance().SnackBar(rootLayout, "" + responseString, -1, -1);
            progressDialog.dismiss();
        }

        @Override
        public void onFinish() {
            super.onFinish();
            progressDialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 101) {
        }
    }


}
