package com.glowingsoft.Recomendados.Seller.FragmentSeller;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.Recomendados.R;


public class SubscriptionFragment extends Fragment {
    TextView startNowBtn;

    public SubscriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscription, container, false);
        startNowBtn = view.findViewById(R.id.startNowBtn);
        startNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Development in Progress", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
