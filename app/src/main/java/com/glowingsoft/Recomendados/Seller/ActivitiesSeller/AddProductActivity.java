package com.glowingsoft.Recomendados.Seller.ActivitiesSeller;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.Recomendados.Buyer.Adapter.DetailPagerAdaper;
import com.glowingsoft.Recomendados.Buyer.Adapter.ItemRecyclerViewAdapter;
import com.glowingsoft.Recomendados.Buyer.Models.HomeModelClass;
import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.ParentClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.RealPathUtil;
import com.glowingsoft.Recomendados.Seller.Adapters.CurrencyAdapter;
import com.glowingsoft.Recomendados.Seller.Models.CurrencyModel;
import com.glowingsoft.Recomendados.WebReq.Urls;
import com.glowingsoft.Recomendados.WebReq.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AddProductActivity extends ParentClass implements View.OnClickListener {
    ProgressDialog progressDialog;
    ImageView productIv;
    EditText titleEt, priceEt, descEt;
    Spinner categoryEt;
    TextView submitTv;
    int SELECT_PICTURE = 1;
    Uri uri = null;
    CurrencyAdapter currencyAdapter;
    List<CurrencyModel> currencyModels;
    RelativeLayout rootLayout;
    String categoryId, title, price, description;
    String imagePath = null;
    int REQUEST_PERMISSION_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        viewBinding();
    }


    private void viewBinding() {
        progressDialog = new ProgressDialog(AddProductActivity.this);
        progressDialog.setMessage("Loading....");
        productIv = findViewById(R.id.imageview);
        titleEt = findViewById(R.id.titleEt);
        priceEt = findViewById(R.id.priceEt);
        descEt = findViewById(R.id.descEt);
        categoryEt = findViewById(R.id.categorySp);
        rootLayout = findViewById(R.id.rootLayout);
        submitTv = findViewById(R.id.submitTv);
        submitTv.setOnClickListener(this);
        productIv.setOnClickListener(this);
        currencyModels = new ArrayList<>();

        currencyAdapter = new CurrencyAdapter(currencyModels, this);
        categoryEt.setAdapter(currencyAdapter);
        categoryEt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryId = currencyModels.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (GlobalClass.getInstance().isNetworkAvailable()) {
            RequestParams requestParams = new RequestParams();
            requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
            WebReq.post(Urls.shopPreferences, requestParams, new GetCategoryRestApi());
        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);

        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitTv:
                title = getValueFromEdittext(titleEt);
                description = getValueFromEdittext(descEt);
                price = getValueFromEdittext(priceEt);
                if (title.length() <= 0) {
                    Toast.makeText(this, "Title is Missing.!", Toast.LENGTH_SHORT).show();
                } else {
                    if (description.length() <= 0) {
                        Toast.makeText(this, "Description is missing!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (price.length() <= 0) {
                            Toast.makeText(this, "Price is Missing!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (imagePath == null) {
                                Toast.makeText(this, "Image is Missing", Toast.LENGTH_SHORT).show();
                            } else {
                                if (GlobalClass.getInstance().isNetworkAvailable()) {
                                    RequestParams requestParams = new RequestParams();
                                    requestParams.setForceMultipartEntityContentType(true);
                                    requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
                                    requestParams.put("shop_id", GlobalClass.getInstance().returnShopId());
                                    requestParams.put("title", title);
                                    requestParams.put("category_id", categoryId);
                                    requestParams.put("price", price);
                                    requestParams.put("description", description);
                                    try {
                                        requestParams.put("image", new File(imagePath));
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("param", requestParams.toString());
                                    WebReq.post(Urls.addProduct, requestParams, new AddProductRestApi());
                                } else {
                                    GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
                                }
                            }
                        }
                    }
                }
                break;
            case R.id.imageview:
                if (ContextCompat.checkSelfPermission(AddProductActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddProductActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    } else {
                        ActivityCompat.requestPermissions(AddProductActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSION_CAMERA);

                    }
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), SELECT_PICTURE);
                }

                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                productIv.setImageURI(selectedImageUri);
                imagePath = RealPathUtil.getRealPath(this, selectedImageUri);
                Log.d("path", imagePath);


            }
        }
    }


    public class AddProductRestApi extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            progressDialog.show();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("response", response.toString() + " ");
            try {
                if (response.getInt("status") == 200) {
                    GlobalClass.getInstance().SnackBar(rootLayout, "" + response.getString("message"), -1, -1);
                    Intent intent = new Intent(AddProductActivity.this, BottomNavigationSellerActivity.class);
                    startActivity(intent);
                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, "" + response.getString("message"), -1, -1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            progressDialog.dismiss();
            GlobalClass.getInstance().SnackBar(rootLayout, "" + errorResponse.toString(), -1, -1);
            Log.d("exception ", errorResponse.toString());
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.d("exception ", errorResponse.toString());

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d("exception ", responseString);
        }


        @Override
        public void onFinish() {
            super.onFinish();
            progressDialog.dismiss();
        }
    }

    public class GetCategoryRestApi extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                if (response.getInt("status") == 200) {
                    JSONArray categoryArray = response.getJSONArray("categories");
                    for (int i = 0; i < categoryArray.length(); i++) {
                        JSONObject jsonObject = categoryArray.getJSONObject(i);
                        CurrencyModel currencyModel = new CurrencyModel();
                        currencyModel.setId("" + jsonObject.getString("id"));
                        currencyModel.setTitle("" + jsonObject.getString("title"));
                        currencyModels.add(currencyModel);
                    }
                    currencyAdapter.notifyDataSetChanged();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }
    }

    public class HomeSeller extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), SELECT_PICTURE);

                } else {

                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", AddProductActivity.this.getPackageName(), null);
                    intent.setData(uri);
                    context.startActivity(intent);
                }
                return;
            }
        }
    }
}
