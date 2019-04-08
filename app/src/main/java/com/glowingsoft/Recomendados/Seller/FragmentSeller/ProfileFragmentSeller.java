package com.glowingsoft.Recomendados.Seller.FragmentSeller;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragmentSeller extends Fragment {
    ProgressDialog progressDialog;
    TextView emailTv, phoneNoTv, nameTv;
    RelativeLayout rootLayout;
    CircleImageView profileIv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_fragment_seller, container, false);
        viewbinding(view);
        return view;
    }

    private void viewbinding(View view) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        emailTv = view.findViewById(R.id.emailTv);
        phoneNoTv = view.findViewById(R.id.phoneNoTv);
        nameTv = view.findViewById(R.id.nameTv);
        rootLayout = view.findViewById(R.id.rootLayout);
        profileIv = view.findViewById(R.id.profileTv);
        if (GlobalClass.getInstance().isNetworkAvailable()) {
            RequestParams requestParams = new RequestParams();
            requestParams.put("user_id", GlobalClass.getInstance().returnUserId());
            WebReq.post(Urls.profileSeller, requestParams, new ProfileRestApi());
        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, getResources().getString(R.string.networkConnection), -1, -1);
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
                    emailTv.setText("" + jsonObject.getString("email"));
                    nameTv.setText("" + jsonObject.getString("name"));
                    phoneNoTv.setText("" + jsonObject.getString("phone"));
                    Picasso.get().load(jsonObject.getString("image")).fit().placeholder(R.drawable.placeholderviewplager).into(profileIv);

                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, response.getString("message"), -1, -1)
                    ;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            progressDialog.dismiss();
            GlobalClass.getInstance().SnackBar(rootLayout, "" + responseString, -1, -1);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            progressDialog.dismiss();
        }
    }

}
