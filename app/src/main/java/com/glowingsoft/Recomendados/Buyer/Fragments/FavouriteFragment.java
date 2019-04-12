package com.glowingsoft.Recomendados.Buyer.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glowingsoft.Recomendados.Buyer.Adapter.FavouriteFragmentAdapter;
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

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment {
    SwipeRefreshLayout swipeRefreshLayout;
    GridView gridView;
    FavouriteFragmentAdapter adapter;
    View rootLayout;
    List<HomeModelClass> homeModelClasses;
    RequestParams requestParams;
    ProgressDialog progressDialog;
    HomeModelClass homeModelClass;
    TextView noRecordTv;


    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        viewBinding(view);
        return view;
    }

    private void viewBinding(View view) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading....");
        swipeRefreshLayout = view.findViewById(R.id.refreshLayout);
        gridView = view.findViewById(R.id.gridView);
        rootLayout = getActivity().findViewById(R.id.rootLayout);
        homeModelClasses = new ArrayList<HomeModelClass>();
        adapter = new FavouriteFragmentAdapter(getActivity(), homeModelClasses);
        gridView.setAdapter(adapter);
        noRecordTv = view.findViewById(R.id.noRecord);
        requestParams = new RequestParams();
        requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (GlobalClass.getInstance().isNetworkAvailable()) {
                    homeModelClasses.clear();
                    WebReq.post(Urls.favourite, requestParams, new FavouriteRestApi());
                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
                }
            }
        });
        if (GlobalClass.getInstance().isNetworkAvailable()) {
            homeModelClasses.clear();
            WebReq.post(Urls.favourite, requestParams, new FavouriteRestApi());
        } else {
//            GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
        }

    }

    public class FavouriteRestApi extends JsonHttpResponseHandler {
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
            progressDialog.dismiss();
        }

        @Override
        public void onFinish() {
            progressDialog.dismiss();
            swipeRefreshLayout.setRefreshing(false);
            super.onFinish();
        }
    }

}
