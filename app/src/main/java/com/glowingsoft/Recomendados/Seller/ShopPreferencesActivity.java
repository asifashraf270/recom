package com.glowingsoft.Recomendados.Seller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.Recomendados.Buyer.Models.ShopPreferneceModel;
import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.Seller.Adapters.CurrencyAdapter;
import com.glowingsoft.Recomendados.Seller.Adapters.LanguageAdapter;
import com.glowingsoft.Recomendados.Seller.Models.CurrencyModel;
import com.glowingsoft.Recomendados.Seller.Models.LanguageModel;
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

public class ShopPreferencesActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner spinner, currencySp, languageSp;
    TextView countryTv;
    TextView searchTv;
    RelativeLayout rootLayout;
    RequestParams requestParams;
    List<ShopPreferneceModel> shopPreferneceModels;
    ProgressDialog progressDialog;
    CustomAdapter customAdapterSpinner;
    CurrencyAdapter currencyAdapter;
    List<CurrencyModel> currencyModels;
    List<LanguageModel> languageModels;
    LanguageAdapter languageAdapter;
    int PLACE_PICKER_REQUEST = 1;
    String latLng = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_preferences);
        viewBinding();
    }

    private void viewBinding() {
        spinner = findViewById(R.id.categorySp);
        countryTv = findViewById(R.id.countryTv);
        countryTv.setOnClickListener(this);
        searchTv = findViewById(R.id.searchTv);
        searchTv.setOnClickListener(this);
        languageSp = findViewById(R.id.languageSp);
        requestParams = new RequestParams();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        currencySp = findViewById(R.id.currencySp);
        rootLayout = findViewById(R.id.rootLayout);
        shopPreferneceModels = new ArrayList<>();

        languageModels = new ArrayList<>();
        languageAdapter = new LanguageAdapter(languageModels, this);
        languageSp.setAdapter(languageAdapter);
        currencyModels = new ArrayList<>();
        currencyAdapter = new CurrencyAdapter(currencyModels, this);
        currencySp.setAdapter(currencyAdapter);
        customAdapterSpinner = new CustomAdapter(shopPreferneceModels);
        spinner.setAdapter(customAdapterSpinner);
        Toast.makeText(this, "" + spinner.getSelectedItemPosition(), Toast.LENGTH_SHORT).show();

        if (GlobalClass.getInstance().isNetworkAvailable()) {
            requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
            WebReq.post(Urls.shopPreferences, requestParams, new ShopPreferences());
        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchTv:
                break;

            case R.id.countryTv:
//                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//                try {
//                    startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }
                break;
        }
    }

    public class ShopPreferences extends JsonHttpResponseHandler {
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
                    JSONArray jsonArray = response.getJSONArray("categories");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ShopPreferneceModel PreferencesModel = new ShopPreferneceModel();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        PreferencesModel.setId("" + jsonObject.getString("id"));
                        PreferencesModel.setImage("" + jsonObject.getString("image"));
                        PreferencesModel.setActive("" + jsonObject.getString("active"));
                        PreferencesModel.setTitle("" + jsonObject.getString("title"));
                        shopPreferneceModels.add(PreferencesModel);
                    }
                    customAdapterSpinner.notifyDataSetChanged();
                    jsonArray = response.getJSONArray("currencies");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        CurrencyModel currencyModel = new CurrencyModel();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        currencyModel.setId("" + jsonObject.getString("id"));
                        currencyModel.setSymbol("" + jsonObject.getString("symbol"));

                        currencyModel.setTitle("" + jsonObject.getString("title"));
                        currencyModels.add(currencyModel);

                    }
                    currencyAdapter.notifyDataSetChanged();
                    jsonArray = response.getJSONArray("languages");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        LanguageModel languageModel = new LanguageModel();
                        languageModel.setId("" + jsonObject.getString("id"));
                        languageModel.setTitle("" + jsonObject.getString("title"));
                        languageModels.add(languageModel);

                    }
                    languageAdapter.notifyDataSetChanged();

                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, response.getString("message"), -1, -1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                GlobalClass.getInstance().SnackBar(rootLayout, e.getMessage(), -1, -1);
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
            super.onFinish();
            progressDialog.dismiss();
        }
    }

    public class CustomAdapter extends BaseAdapter {
        List<ShopPreferneceModel> shopPreferneceModels;
        LayoutInflater layoutInflater;

        public CustomAdapter(List<ShopPreferneceModel> shopPreferneceModels) {
            this.shopPreferneceModels = shopPreferneceModels;
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return shopPreferneceModels.size();
        }

        @Override
        public Object getItem(int position) {
            return shopPreferneceModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = layoutInflater.inflate(R.layout.custom_adapter_spinner, parent, false);
            TextView textView = view.findViewById(R.id.valueTv);
            textView.setText("" + shopPreferneceModels.get(position).getTitle());
            return view;
        }
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLACE_PICKER_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlacePicker.getPlace(data, this);
//                latLng = place.getLatLng().latitude + "," + place.getLatLng().longitude;
//                Geocoder gcd = new Geocoder(this, Locale.getDefault());
//                List<Address> addresses = null;
//                try {
//                    addresses = gcd.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (addresses.size() > 0) {
//                    String toastMsg = String.format("Place: %s", addresses.get(0).getLocality() + " - " + addresses.get(0).getCountryName() + " - " + addresses.get(0).getCountryCode());
//                    Toast.makeText(this, "" + toastMsg, Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//        }
//    }
}
