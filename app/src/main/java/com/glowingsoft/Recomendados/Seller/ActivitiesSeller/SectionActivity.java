package com.glowingsoft.Recomendados.Seller.ActivitiesSeller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.solver.GoalRow;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tommykw.tagview.DataTransform;
import com.github.tommykw.tagview.TagView;
import com.glowingsoft.Recomendados.Buyer.Models.TagsModel;
import com.glowingsoft.Recomendados.ContactsCompletionView;
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

import org.jetbrains.annotations.NotNull;
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
    TextView addTvTags, addTvMaterial, saveandContinueTv;
    RelativeLayout rootLayout;
    List<LanguageModel> tagsListModels;
    ImageView shopImage;
    int SELECT_PICTURE = 1;
    ProgressDialog progressDialog;
    byte[] inputData = null;
    String tagId, materialId;
    String imagePath = null;
    ImageView backIv;
    AutoCompleteTextView completionView;
    AutoCompleteTextView materialAtv;
    ArrayAdapter<String> materialAdapter;
    String[] materialtitle, materialid;
    ArrayAdapter<String> adapter;
    TagView<TagsModel> tagsTv;
    TagView<TagsModel> maTagsTv;
    String[] tagsTitle, tagsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        viewBinding();
    }


    private void viewBinding() {

        tagsTv = findViewById(R.id.tagsTv);
        maTagsTv = findViewById(R.id.materialsTv);
        materialAtv = findViewById(R.id.materialAtv);
        backIv = findViewById(R.id.backIv);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tagsListModels = new ArrayList<>();
        completionView = findViewById(R.id.tagsAtv);
        materialAtv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (GlobalClass.getInstance().isNetworkAvailable()) {
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
                    requestParams.put("title", s.toString());
                    WebReq.post(Urls.tagSearch, requestParams, new MaterialSearch());
                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, "" + getResources().getString(R.string.networkConnection), -1, -1);
                }

            }
        });
        completionView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (GlobalClass.getInstance().isNetworkAvailable()) {
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
                    requestParams.put("title", s.toString());
                    WebReq.post(Urls.tagSearch, requestParams, new TagSearch());
                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, "" + getResources().getString(R.string.networkConnection), -1, -1);
                }

            }
        });
        shopImage = findViewById(R.id.shopImage);
        addTvTags = findViewById(R.id.tagAddTv);
        addTvMaterial = findViewById(R.id.materialAddTv);
        saveandContinueTv = findViewById(R.id.saveAndcontinueTv);
        addTvTags.setOnClickListener(this);

        rootLayout = findViewById(R.id.rootLayout);
        saveandContinueTv.setOnClickListener(this);
        addTvMaterial.setOnClickListener(this);

        shopImage.setOnClickListener(this);
        progressDialog = new ProgressDialog(SectionActivity.this);
        progressDialog.setMessage("Loading...");


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tagAddTv:
                if (GlobalClass.getInstance().isNetworkAvailable()) {
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
                    requestParams.put("title", completionView.getText().toString());
                    WebReq.post(Urls.addTags, requestParams, new AddTagRetagAddTvstApi());

                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, "" + getResources().getString(R.string.networkConnection), -1, -1);
                }
                break;
            case R.id.materialAddTv:
                if (GlobalClass.getInstance().isNetworkAvailable()) {
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
                    requestParams.put("title", completionView.getText().toString());
                    WebReq.post(Urls.addTags, requestParams, new AddTagRetagAddTvstApi());

                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, "" + getResources().getString(R.string.networkConnection), -1, -1);
                }
                break;
            case R.id.saveAndcontinueTv:
                List<TagsModel> tagsModels = (List<TagsModel>) tagsTv.getTag();
//                tagId = tagsModels.get(0).getId() + ",";
//                for (int i = 1; i < tagsModels.size(); i++) {
//                    tagId = tagsId + tagsModels.get(i).getId();
//                }
                Log.d("response_tagId", tagId);
                if (tagId != null && materialId != null && imagePath != null) {
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

    public class MaterialSearch extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();


        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("response_tag", response.toString());
            try {
                if (response.getInt("status") == 200) {
                    JSONArray tagsArray = response.getJSONArray("result");
                    materialtitle = new String[tagsArray.length()];
                    materialid = new String[tagsArray.length()];
                    for (int i = 0; i < tagsArray.length(); i++) {
                        JSONObject jsonObject = tagsArray.getJSONObject(i);
                        materialtitle[i] = jsonObject.getString("title");
                        materialid[i] = jsonObject.getString("id");
                    }
                    if (materialtitle.length == 0) {
                        addTvMaterial.setVisibility(View.VISIBLE);
                    } else {
                        addTvMaterial.setVisibility(View.INVISIBLE);
                    }

                    materialAdapter = new ArrayAdapter<String>(SectionActivity.this, android.R.layout.simple_list_item_1, materialtitle);
                    materialAtv.setThreshold(1);
                    materialAtv.setAdapter(materialAdapter);
                    materialAtv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            List<TagsModel> tagsModels = new ArrayList<>();
                            TagsModel model = new TagsModel();
                            model.setTitle("" + materialtitle[position]);
                            model.setId("" + materialid[position]);
                            tagsModels.add(model);
                            maTagsTv.setTags(tagsModels, new DataTransform<TagsModel>() {
                                @NotNull
                                @Override
                                public String transfer(TagsModel tagsModel) {
                                    return tagsModel.getTitle();
                                }
                            });

                        }
                    });


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


    public class TagSearch extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();


        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("response_tag", response.toString());
            try {
                if (response.getInt("status") == 200) {
                    JSONArray tagsArray = response.getJSONArray("result");
                    tagsTitle = new String[tagsArray.length()];
                    tagsId = new String[tagsArray.length()];
                    for (int i = 0; i < tagsArray.length(); i++) {
                        JSONObject jsonObject = tagsArray.getJSONObject(i);
                        tagsTitle[i] = jsonObject.getString("title");
                        tagsId[i] = jsonObject.getString("id");
                    }
                    if (tagsTitle.length == 0) {
                        addTvTags.setVisibility(View.VISIBLE);
                    } else {
                        addTvTags.setVisibility(View.INVISIBLE);
                    }

                    adapter = new ArrayAdapter<String>(SectionActivity.this, android.R.layout.simple_list_item_1, tagsTitle);
                    completionView.setThreshold(1);
                    completionView.setAdapter(adapter);
                    completionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            List<TagsModel> tagsModels = new ArrayList<>();
                            TagsModel model = new TagsModel();
                            model.setTitle("" + tagsTitle[position]);
                            model.setId("" + tagsId[position]);
                            tagsModels.add(model);
                            tagsTv.setTags(tagsModels, new DataTransform<TagsModel>() {
                                @NotNull
                                @Override
                                public String transfer(TagsModel tagsModel) {
                                    return tagsModel.getTitle();
                                }
                            });

                        }
                    });


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

    public class AddTagRetagAddTvstApi extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                if (response.getInt("status") == 200) {
                    GlobalClass.getInstance().SnackBar(rootLayout, "" + response.getString("message"), -1, -1);

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
            GlobalClass.getInstance().SnackBar(rootLayout, responseString, -1, -1);
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }
    }
}
