package com.glowingsoft.Recomendados.Buyer.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.glowingsoft.Recomendados.Buyer.Models.DetailsModelClass;
import com.glowingsoft.Recomendados.R;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;

import java.util.List;

public class DetailPagerAdaper extends PagerAdapter {
    LayoutInflater layoutInflater;
    Context context;
    List<DetailsModelClass> modelClasses;

    public DetailPagerAdaper(Context context, List<DetailsModelClass> detailsModelClasses) {
        this.modelClasses = detailsModelClasses;
        this.context = context;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return modelClasses.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == ((View) o);

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.viewpager_view, container, false);
        ImageView imageView = view.findViewById(R.id.image);
        SparkButton favouirteBtn = view.findViewById(R.id.favouriteBtn);
        Picasso.get().load(modelClasses.get(position).getImageUrl()).fit().placeholder(R.drawable.placeholderviewplager).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
