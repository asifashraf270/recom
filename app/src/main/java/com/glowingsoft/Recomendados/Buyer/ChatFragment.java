package com.glowingsoft.Recomendados.Buyer;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.glowingsoft.Recomendados.Buyer.Adapter.ChatFragmentAdapter;
import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.Seller.Models.ConversionModel;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ChatFragment extends Fragment {
    ListView listView;
    ChatFragmentAdapter adapter;
    List<ConversionModel> conversionModels;
    ProgressDialog progressDialog;
    RelativeLayout rootLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        viewBinding(view);
        return view;
    }

    private void viewBinding(View view) {
        listView = view.findViewById(R.id.listView);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....");
        rootLayout = view.findViewById(R.id.rootLayout);
        adapter = new ChatFragmentAdapter(getContext());
        listView.setAdapter(adapter);

    }

    public class ConversionRestApi extends JsonHttpResponseHandler {
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
                    JSONArray jsonArrayconversations = response.getJSONArray("conversations");
                    for (int i = 0; i < jsonArrayconversations.length(); i++) {
                        ConversionModel conversionModel = new ConversionModel();
                        JSONObject jsonObject = jsonArrayconversations.getJSONObject(i);
                        conversionModel.setConversionId("" + jsonObject.getString(""));
                    }
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
