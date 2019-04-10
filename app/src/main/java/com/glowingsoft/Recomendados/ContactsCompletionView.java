package com.glowingsoft.Recomendados;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.Recomendados.Seller.Models.LanguageModel;
import com.tokenautocomplete.TokenCompleteTextView;

public class ContactsCompletionView extends TokenCompleteTextView<LanguageModel> {
    public ContactsCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(final LanguageModel object) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView view = (TextView) inflater.inflate(R.layout.autocomplete_view, (ViewGroup) getParent(), false);
        view.setText(object.getTitle());
        return view;
    }

    @Override
    protected LanguageModel defaultObject(String completionText) {
        int index = completionText.indexOf('@');
        return null;
//        if (index == -1) {
//            LanguageModel languageModel = new LanguageModel();
//            languageModel.setId("");
//            languageModel.setTitle("");
//            return languageModel;
//        } else {
//            return new LanguageModel();
//        }
    }


}

