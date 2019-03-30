package com.glowingsoft.Recomendados.Buyer.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.Recomendados.Buyer.Activities.LoginInActivity;
import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.WebReq.Urls;
import com.glowingsoft.Recomendados.WebReq.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    CircleImageView circleImageView;
    TextView nameTv, emailTv;
    RequestParams requestParams;
    View rootLayout;
    ProgressDialog progressDialog;
    TextView logOutTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        viewBinding(view);
        return view;
    }

    private void viewBinding(View view) {
        circleImageView = view.findViewById(R.id.profileIv);
        nameTv = view.findViewById(R.id.nameTv);
        emailTv = view.findViewById(R.id.emailTv);
        requestParams = new RequestParams();
        rootLayout = getActivity().findViewById(R.id.rootLayout);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....");
        logOutTv = view.findViewById(R.id.logout);
        logOutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.getInstance().logoOut();
                Intent intent = new Intent(getActivity(), LoginInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
        if (GlobalClass.getInstance().isNetworkAvailable()) {
            WebReq.post(Urls.profile, requestParams, new ProfileRestApi());
        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    public class ProfileRestApi extends JsonHttpResponseHandler {
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
                    JSONObject jsonObject = response.getJSONObject("user");
                    String image = jsonObject.getString("image");
                    Picasso.get().load(image).fit().centerCrop().into(circleImageView);
                    nameTv.setText("" + jsonObject.getString("name"));
                    emailTv.setText("" + jsonObject.getString("email"));
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
            progressDialog.dismiss();
            GlobalClass.getInstance().SnackBar(rootLayout, throwable.getMessage(), -1, -1);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            progressDialog.dismiss();
        }
    }


}
