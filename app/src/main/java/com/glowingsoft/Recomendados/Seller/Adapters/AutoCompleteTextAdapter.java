package com.glowingsoft.Recomendados.Seller.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.Recomendados.R;
import com.glowingsoft.Recomendados.Seller.Models.LanguageModel;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteTextAdapter extends ArrayAdapter<LanguageModel> {
    List<LanguageModel> modelList, tempItems, suggestions;
    Context context;


    public AutoCompleteTextAdapter(Context context, int resource, List<LanguageModel> languageModels) {
        super(context, resource);
        this.context = context;
        this.modelList = languageModels;
        tempItems = new ArrayList<LanguageModel>(modelList); // this makes the difference.
        suggestions = new ArrayList<LanguageModel>();
    }

    private void UpdateList(List<LanguageModel> languageModels) {
        this.modelList.clear();
        this.tempItems.clear();
        this.suggestions.clear();
        this.modelList = languageModels;
        tempItems = new ArrayList<LanguageModel>(modelList); // this makes the difference.
        suggestions = new ArrayList<LanguageModel>();
        this.notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_adapter_spinner, parent, false);
        }
//        LanguageModel people = modelList.get(position);
//        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.valueTv);
//            if (lblName != null)
                lblName.setText("xyz");
//        }

        return view;

    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (LanguageModel languageModel : tempItems) {
                    if (languageModel.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(languageModel);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;

            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<LanguageModel> filterList = (List<LanguageModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (LanguageModel languageModel : filterList) {
                    add(languageModel);
                    notifyDataSetChanged();
                }
            }

        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((LanguageModel) resultValue).getTitle();
            return str;
        }

    };
}
