package com.glowingsoft.Recomendados.Seller.FragmentSeller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.Seller.ActivitiesSeller.AddProductActivity;


public class HomeSellerFragment extends Fragment implements View.OnClickListener {
    TextView addProductTv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home_seller, container, false);
        viewBinding(view);
        return view;
    }

    private void viewBinding(View view) {
        addProductTv = view.findViewById(R.id.addProductTv);
        addProductTv.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addProductTv:
                Intent intent = new Intent(getActivity(), AddProductActivity.class);
                startActivity(intent);
                break;
        }
    }
}
