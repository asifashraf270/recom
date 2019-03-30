package com.glowingsoft.Recomendados.Buyer.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glowingsoft.Recomendados.Buyer.Models.HomeModelClass;
import com.glowingsoft.Recomendados.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolderITem> {
    List<HomeModelClass> modelClasses;

    public ItemRecyclerViewAdapter(List<HomeModelClass> modelClasses) {
        this.modelClasses = modelClasses;
    }

    @NonNull
    @Override
    public ViewHolderITem onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_views, viewGroup, false);
        return new ViewHolderITem(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderITem viewHolderITem, int i) {
        viewHolderITem.priceTv.setText("" + modelClasses.get(i).getPrice());
        viewHolderITem.typeTv.setText("" + modelClasses.get(i).getCategory_title());
        Picasso.get().load(modelClasses.get(i).getImage()).fit().into(viewHolderITem.pictureIv);
    }

    @Override
    public int getItemCount() {
        return modelClasses.size();
    }

    public class ViewHolderITem extends RecyclerView.ViewHolder {
        public ImageView pictureIv;
        public TextView priceTv, typeTv;

        public ViewHolderITem(@NonNull View itemView) {
            super(itemView);
            pictureIv = itemView.findViewById(R.id.pictureIv);
            priceTv = itemView.findViewById(R.id.priceTv);
            typeTv = itemView.findViewById(R.id.typeTv);
        }
    }
}
