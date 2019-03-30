package com.glowingsoft.Recomendados.Seller.FragmentSeller;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glowingsoft.Recomendados.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragmentSeller extends Fragment {


    public ProfileFragmentSeller() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_fragment_seller, container, false);
    }

}
