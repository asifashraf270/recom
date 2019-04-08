package com.glowingsoft.Recomendados.Seller.ActivitiesSeller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tommykw.tagview.TagView;
import com.glowingsoft.Recomendados.Buyer.Models.TagsModel;
import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.ParentClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.RealPathUtil;
import com.glowingsoft.Recomendados.Seller.Adapters.AutoCompleteTextAdapter;
import com.glowingsoft.Recomendados.Seller.Adapters.LanguageAdapter;
import com.glowingsoft.Recomendados.Seller.Models.LanguageModel;
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
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class SectionActivity extends ParentClass implements View.OnClickListener {
    EditText materialEt;
    TextView addTvTags, addTvMaterial, saveandContinueTv;
    RelativeLayout rootLayout;
    TagView<TagsModel> tagsView;
    List<LanguageModel> tagsListModels;
    AutoCompleteTextView tagsautoCompleteTv;
    AutoCompleteTextAdapter adapter;
    ImageView shopImage;
    int SELECT_PICTURE = 1;
    ProgressDialog progressDialog;
    byte[] inputData = null;
    String tagId, materialId;
    String imagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        viewBinding();
    }

    private void viewBinding() {
        tagId = "9,10";
        materialId = "11,13";
        shopImage = findViewById(R.id.shopImage);
        materialEt = findViewById(R.id.materialEt);
        addTvTags = findViewById(R.id.tagAddTv);
        addTvMaterial = findViewById(R.id.materialAddTv);
        saveandContinueTv = findViewById(R.id.saveAndcontinueTv);
        addTvTags.setOnClickListener(this);
        tagsListModels = new ArrayList<>();
        rootLayout = findViewById(R.id.rootLayout);
        saveandContinueTv.setOnClickListener(this);
        addTvMaterial.setOnClickListener(this);
        tagsautoCompleteTv = findViewById(R.id.tagsSearch);
        tagsView = findViewById(R.id.tags);
        tagsautoCompleteTv.setThreshold(0);
//        tagsListModels = retriveData();
        shopImage.setOnClickListener(this);
        progressDialog = new ProgressDialog(SectionActivity.this);
        progressDialog.setMessage("Loading...");

        tagsautoCompleteTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (GlobalClass.getInstance().isNetworkAvailable()) {
                    tagsListModels.clear();
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
                    requestParams.put("title", s);
                    WebReq.client.cancelAllRequests(true);
                    WebReq.post(Urls.tagSearch, requestParams, new TagSearch());

                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tagAddTv:
                break;
            case R.id.materialAddTv:
                break;
            case R.id.saveAndcontinueTv:
                if (tagId != null && materialId != null) {
                    if (GlobalClass.getInstance().isNetworkAvailable()) {
                        RequestParams requestParams = new RequestParams();
                        requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
                        requestParams.put("name", GlobalClass.getInstance().returnshopName());
                        requestParams.put("currency_id", GlobalClass.getInstance().returncurrencyId());
                        requestParams.put("language_id", GlobalClass.getInstance().returnLangId());
                        requestParams.put("tags", tagId);
                        requestParams.put("materials", materialId);
                        requestParams.put("latitude", GlobalClass.getInstance().returnLatitude());
                        requestParams.put("longitude", GlobalClass.getInstance().returnLongitude());
                        requestParams.put("address", GlobalClass.getInstance().returnAddress());
                        requestParams.put("city", GlobalClass.getInstance().returncityName());
                        requestParams.put("country", GlobalClass.getInstance().returnCountryName());
                        try {
                            requestParams.put("image", new File(imagePath));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        requestParams.put("shop_type", GlobalClass.getInstance().returnShoptype());
                        Log.d("param", requestParams.toString());
                        WebReq.post(Urls.addShop, requestParams, new AddShopRestApi());
                    } else {
                        GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
                    }
                } else {
                    if (tagId == null) {
                        GlobalClass.getInstance().SnackBar(rootLayout, "Select Tags Minimum 1 and Max 3", -1, -1);
                    }
                    if (materialId == null) {
                        GlobalClass.getInstance().SnackBar(rootLayout, "Select Material Minimum 1 and Max 3", -1, -1);
                    }
                    if (inputData == null) {
                        GlobalClass.getInstance().SnackBar(rootLayout, "Select shop Image Its's compulsory", -1, -1);
                    }
                }

                break;
            case R.id.shopImage:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
                break;
        }
    }

    public class TagSearch extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();


        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                if (response.getInt("status") == 200) {
                    JSONArray tagsArray = response.getJSONArray("result");
                    for (int i = 0; i < tagsArray.length(); i++) {
                        LanguageModel tagsModel = new LanguageModel();
                        JSONObject jsonObject = tagsArray.getJSONObject(i);
                        tagsModel.setId("" + jsonObject.getString("id"));
                        tagsModel.setTitle("" + jsonObject.getString("title"));
                        tagsListModels.add(tagsModel);
                    }
                    Log.d("response", tagsListModels.size() + " size");

                    adapter = new AutoCompleteTextAdapter(SectionActivity.this, R.layout.activity_section, retriveData());
                    tagsautoCompleteTv.setAdapter(adapter);


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
        }
    }

    private List<LanguageModel> retriveData() {
        List<LanguageModel> languageModels = new ArrayList<>();
        LanguageModel model = new LanguageModel();
        model.setTitle("Hello world");
        model.setId("1");
        languageModels.add(model);
        model.setTitle("Hello world");
        model.setId("1");
        languageModels.add(model);
        model.setTitle("Hello world");
        model.setId("1");
        languageModels.add(model);
        model.setTitle("Hello world");
        model.setId("1");
        languageModels.add(model);
        model.setTitle("Hello world");
        model.setId("1");
        languageModels.add(model);
        return languageModels;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                shopImage.setImageURI(selectedImageUri);
                imagePath = RealPathUtil.getRealPath(this, selectedImageUri);
                Log.d("path", imagePath);
            }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public class AddShopRestApi extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            progressDialog.show();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("response", response.toString());
            try {
                if (response.getInt("status") == 200) {
                    JSONObject jsonObject = response.getJSONObject("shop");
                    GlobalClass.getInstance().storeShopId("" + jsonObject.getString("id"));
                    final AlertDialog alertDialog = new AlertDialog.Builder(SectionActivity.this).create();
                    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = layoutInflater.inflate(R.layout.congratulation_alert_dialog, null);
                    TextView textView = view.findViewById(R.id.continueTv);
                    alertDialog.setCancelable(false);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SectionActivity.this, BottomNavigationSellerActivity.class);
                            startActivity(intent);
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setView(view);
                    alertDialog.show();

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
            GlobalClass.getInstance().SnackBar(rootLayout, responseString.toLowerCase(), -1, -1);
            Log.d("response ", responseString.toString());
            progressDialog.dismiss();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.d("response", errorResponse.toString());

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.d("response", errorResponse.toString());

        }

        @Override
        public void onFinish() {
            super.onFinish();
            progressDialog.dismiss();
        }
    }
}
