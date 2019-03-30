package com.glowingsoft.Recomendados.Seller.FragmentSeller;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glowingsoft.Recomendados.R;


public class HomeSellerFragment extends Fragment {
    AlertDialog alertDialog;
    LayoutInflater layoutInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.congratulation_alert_dialog, container, false);
        alertDialog = new AlertDialog.Builder(getActivity()).setView(view).create();
        alertDialog.show();
        return inflater.inflate(R.layout.fragment_home_seller, container, false);
    }


}
