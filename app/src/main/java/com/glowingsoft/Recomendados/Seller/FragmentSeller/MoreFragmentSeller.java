package com.glowingsoft.Recomendados.Seller.FragmentSeller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glowingsoft.Recomendados.Buyer.Activities.BottomNavigationActivity;
import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.Seller.ActivitiesSeller.MyshopActivity;
import com.glowingsoft.Recomendados.Seller.ShopPreferencesActivity;


public class MoreFragmentSeller extends Fragment implements View.OnClickListener {
    TextView titleTv;
    ImageView googleIv, facebookIv, instaIv;
    TextView myshopTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more_fragment_seller, container, false);
        googleIv = view.findViewById(R.id.googleIv);
        facebookIv = view.findViewById(R.id.facebookIv);
        instaIv = view.findViewById(R.id.instaIv);
        titleTv = view.findViewById(R.id.titleTv);
        googleIv.setOnClickListener(this);
        facebookIv.setOnClickListener(this);
        myshopTv = view.findViewById(R.id.myshopTv);
        myshopTv.setOnClickListener(this);
        instaIv.setOnClickListener(this);
        titleTv.setText("" + GlobalClass.getInstance().returntitle());
        titleTv.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googleIv:
                share("com.google.android.apps.plus");
                break;
            case R.id.facebookIv:
                share("com.facebook.katana");
                break;
            case R.id.instaIv:
                share("com.twitter.android");
                break;
            case R.id.titleTv:
                Intent intent = new Intent(getActivity(), BottomNavigationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
                break;
            case R.id.myshopTv:
                Intent intent1 = new Intent(getActivity(), MyshopActivity.class);
                startActivity(intent1);
                break;
        }
    }


    private void share(String value) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());

        startActivity(Intent.createChooser(share, "Share link!"));

    }

}
