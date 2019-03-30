package com.glowingsoft.Recomendados.Buyer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.glowingsoft.Recomendados.Buyer.Adapter.ChatFragmentAdapter;
import com.glowingsoft.Recomendados.R;

public class ChatFragment extends Fragment {
    ListView listView;
    ChatFragmentAdapter adapter;

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
        adapter = new ChatFragmentAdapter(getContext());
        listView.setAdapter(adapter);

    }


}
