package com.glowingsoft.Recomendados.Buyer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.Recomendados.Buyer.Activities.DetailActivity;
import com.glowingsoft.Recomendados.Buyer.Models.HomeModelClass;
import com.glowingsoft.Recomendados.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolderITem> {
    List<HomeModelClass> modelClasses;
    Context context;

    public ItemRecyclerViewAdapter(List<HomeModelClass> modelClasses, Context context) {
        this.modelClasses = modelClasses;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderITem onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_views, viewGroup, false);

        return new ViewHolderITem(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderITem viewHolderITem, int i) {
        viewHolderITem.priceTv.setText("" + modelClasses.get(i).getPrice());
        viewHolderITem.typeTv.setText("" + modelClasses.get(i).getCategory_title());
        viewHolderITem.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("product_id", modelClasses.get(viewHolderITem.getAdapterPosition()).getId());
                context.startActivity(intent);
            }
        });

        Picasso.get().load(modelClasses.get(i).getImage()).fit().placeholder(R.drawable.placeholderviewplager).into(viewHolderITem.pictureIv);
    }

    @Override
    public int getItemCount() {
        return modelClasses.size();
    }

    public class ViewHolderITem extends RecyclerView.ViewHolder {
        public ImageView pictureIv;
        public TextView priceTv, typeTv;

        public ViewHolderITem(@NonNull final View itemView) {
            super(itemView);
            pictureIv = itemView.findViewById(R.id.pictureIv);
            priceTv = itemView.findViewById(R.id.priceTv);
            typeTv = itemView.findViewById(R.id.typeTv);
        }
    }
}
