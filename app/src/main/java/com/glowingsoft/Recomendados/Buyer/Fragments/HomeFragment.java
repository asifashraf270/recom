package com.glowingsoft.Recomendados.Buyer.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.glowingsoft.Recomendados.Buyer.Activities.SearchActivity;
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

public class HomeFragment extends Fragment implements View.OnClickListener{
    HomeFragmentAdapter adapter;
    View rootLayout;
    GridView gridView;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    List<HomeModelClass> homeModelClasses;

    RequestParams requestParams;
    HomeModelClass homeModelClass;
    ProgressDialog progressDialog;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewBinding(view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }

    private void viewBinding(View view) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....");
        gridView = view.findViewById(R.id.gridView);
        toolbar = view.findViewById(R.id.toolbar);
        rootLayout = getActivity().findViewById(R.id.rootLayout);

        swipeRefreshLayout = view.findViewById(R.id.refreshLayout);
        homeModelClasses = new ArrayList<HomeModelClass>();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (GlobalClass.getInstance().isNetworkAvailable()) {
                    homeModelClasses.clear();
                    WebReq.post(Urls.home, requestParams, new HomeRestApi());

                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
                }
            }
        });

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");
        adapter = new HomeFragmentAdapter(getActivity(), homeModelClasses);
        gridView.setAdapter(adapter);
        requestParams = new RequestParams();
        requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
        if (GlobalClass.getInstance().isNetworkAvailable()) {
            homeModelClasses.clear();
            WebReq.post(Urls.home, requestParams, new HomeRestApi());
        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
        }
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                intent = new Intent(getActivity(), DetailActivity.class);
//                intent.putExtra("product_id", homeModelClasses.get(position).getId());
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
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
            swipeRefreshLayout.setRefreshing(false);
            progressDialog.dismiss();
        }
    }
}
