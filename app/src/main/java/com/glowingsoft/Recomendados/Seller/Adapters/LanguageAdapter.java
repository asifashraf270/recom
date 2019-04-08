package com.glowingsoft.Recomendados.Seller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.Seller.Models.LanguageModel;

import java.util.List;

public class LanguageAdapter extends BaseAdapter {
    List<LanguageModel> languageModels;
    Context context;
    LayoutInflater layoutInflater;

    public LanguageAdapter(List<LanguageModel> languageModels, Context context) {
        this.languageModels = languageModels;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return languageModels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.custom_adapter_spinner, parent, false);
        TextView textView = view.findViewById(R.id.valueTv);
        textView.setText("" + languageModels.get(position).getTitle());
        return view;
    }
}
