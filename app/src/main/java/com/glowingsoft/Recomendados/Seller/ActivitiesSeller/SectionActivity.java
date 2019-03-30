package com.glowingsoft.Recomendados.Seller.ActivitiesSeller;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.ParentClass;
import com.glowingsoft.Recomendados.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SectionActivity extends ParentClass implements View.OnClickListener {
    EditText tagsEt, materialEt;
    TextView addTvTags, addTvMaterial, saveandContinueTv;
    ProgressDialog progressDialog;
    RelativeLayout rootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        viewBinding();
    }

    private void viewBinding() {
        tagsEt = findViewById(R.id.tagEt);
        materialEt = findViewById(R.id.materialEt);
        addTvTags = findViewById(R.id.tagAddTv);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        addTvMaterial = findViewById(R.id.materialAddTv);
        saveandContinueTv = findViewById(R.id.saveAndcontinueTv);
        addTvTags.setOnClickListener(this);
        rootLayout = findViewById(R.id.rootLayout);
        saveandContinueTv.setOnClickListener(this);
        addTvMaterial.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tagAddTv:
                break;
            case R.id.materialAddTv:
                break;
            case R.id.saveAndcontinueTv:
                break;
        }
    }

    public class TagSearch extends JsonHttpResponseHandler {
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
            super.onFinish();
            progressDialog.dismiss();
        }
    }
}
