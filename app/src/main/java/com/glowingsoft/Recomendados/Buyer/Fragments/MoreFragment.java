package com.glowingsoft.Recomendados.Buyer.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.Recomendados.GlobalClass;
import com.glowingsoft.Recomendados.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment implements View.OnClickListener {
    TextView titleTv;
    ImageView googleIv, facebookIv, instaIv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        googleIv = view.findViewById(R.id.googleIv);
        facebookIv = view.findViewById(R.id.facebookIv);
        instaIv = view.findViewById(R.id.instaIv);
        titleTv = view.findViewById(R.id.titleTv);
        googleIv.setOnClickListener(this);
        facebookIv.setOnClickListener(this);
        instaIv.setOnClickListener(this);
        titleTv.setText("" + GlobalClass.getInstance().returntitle());
        titleTv.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googleIv:
                share();
                break;
            case R.id.facebookIv:
                share();
                break;
            case R.id.instaIv:
                share();
                break;
            case R.id.titleTv:
                Toast.makeText(getActivity(), "Development in Progress", Toast.LENGTH_SHORT).show();
        }
    }

    private void share() {
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
